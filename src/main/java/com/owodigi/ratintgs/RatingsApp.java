package com.owodigi.ratintgs;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2EpisodeStore;
import com.owodigi.ratings.store.impl.H2TitleStore;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbDownloaderCallback;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.util.IMDbTSVFormats.TitlePrincipalsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class RatingsApp {

    public static void main(final String[] args) throws IOException {
        final TitleStore titleStore = new H2TitleStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        final EpisodeStore episodeStore = new H2EpisodeStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        processTitleBasicsDataset(titleStore, episodeStore);
        processTitleRatingsDataset(titleStore, episodeStore);
        processTitlePrincipalsDataset(titleStore, episodeStore);
        processTitleEpisodeDataset(episodeStore);
    }

    private static void processTitleBasicsDataset(final TitleStore titleStore, final EpisodeStore episodeStore) throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleBasicsURL(), new TitleBasicsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (RatingsAppProperties.titleIncludeYears().contains(year)) {
                    final String tconst = record.get(TitleBasicsFormat.header.tconst);
                    final String titleType = record.get(TitleBasicsFormat.header.titleType);
                    final String primaryTitle = record.get(TitleBasicsFormat.header.primaryTitle);
                    if (titleType.equals("tvEpisode")) {
                        episodeStore.addTitle(tconst, primaryTitle);
                    } else {
                        titleStore.addTitle(tconst, titleType, primaryTitle);
                    }
                }
            }
        });
    }

    private static void processTitleRatingsDataset(final TitleStore titleStore, final EpisodeStore episodeStore) throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleRatingsURL(), new TitleRatingsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitleRatingsFormat.headers.tconst);
                final String averageRating = record.get(TitleRatingsFormat.headers.averageRating);
                final TitleRecord titleRecord = titleStore.tconst(tconst);
                if (titleRecord == null) {
                    final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                    if (episodeRecord == null) {
                        throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore: " + record);
                    }
                    episodeStore.updateRating(tconst, averageRating);
                } else {
                    titleStore.updateRating(tconst, averageRating);
                }
            }
        });
    }

    private static void processTitlePrincipalsDataset(final TitleStore titleStore, final EpisodeStore episodeStore) throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titlePrincipalsURL(), new TitlePrincipalsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitlePrincipalsFormat.headers.tconst);
                final String nconst = record.get(TitlePrincipalsFormat.headers.nconst);
                final TitleRecord titleRecord = titleStore.tconst(tconst);
                if (titleRecord == null) {
                    final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                    if (episodeRecord == null) {
                        throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore: " + record);
                    }
                    episodeStore.addNconst(tconst, nconst);
                } else {
                    titleStore.addNconst(tconst, nconst);
                }
            }
        });
    }

    private static void processTitleEpisodeDataset(final EpisodeStore episodeStore) throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleEpisodeURL(), new TitleEpisodeFormat(), new IMDbDownloaderCallback() {
            
            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitleEpisodeFormat.header.tconst);
                final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                if (episodeRecord == null) {
                    throw new IllegalStateException("Encountered a record not present in EpisdoeStore: " + record);
                }
                final String parentTconst = record.get(TitleEpisodeFormat.header.parentTconst);
                final String episodeNumber = record.get(TitleEpisodeFormat.header.episodeNumber);
                final String seasonNumber = record.get(TitleEpisodeFormat.header.seasonNumber);
                episodeStore.updateEpisode(tconst, parentTconst, seasonNumber, episodeNumber);
            }
        });
    }
}
