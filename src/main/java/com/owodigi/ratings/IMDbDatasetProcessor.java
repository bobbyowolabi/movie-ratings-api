package com.owodigi.ratings;

import com.owodigi.ratings.util.RatingsAppProperties;
import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbDownloaderCallback;
import com.owodigi.util.IMDbTSVFormats;
import com.owodigi.util.IMDbTSVFormats.NameBasicFormat;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.util.IMDbTSVFormats.TitlePrincipalsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class IMDbDatasetProcessor {
    private final RatingStore store;
    
    public IMDbDatasetProcessor(final RatingStore store) {
        this.store = store;
    }
    
    public void process() throws IOException {
        processTitleBasicsDataset();
        processTitleRatingsDataset();
        processTitlePrincipalsDataset();
        processTitleEpisodeDataset();
        processNameBasicsDataset();
    }
    
    private void processTitleBasicsDataset() throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleBasicsURL(), new TitleBasicsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String year = record.get(IMDbTSVFormats.TitleBasicsFormat.header.startYear);
                if (RatingsAppProperties.titleIncludeYears().contains(year)) {
                    final String tconst = record.get(IMDbTSVFormats.TitleBasicsFormat.header.tconst);
                    final String titleType = record.get(IMDbTSVFormats.TitleBasicsFormat.header.titleType);
                    final String primaryTitle = record.get(IMDbTSVFormats.TitleBasicsFormat.header.primaryTitle);
                    store.addTitle(tconst, titleType, primaryTitle);
                }
            }
        });
    }

    private void processTitleRatingsDataset() throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleRatingsURL(), new TitleRatingsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(IMDbTSVFormats.TitleRatingsFormat.headers.tconst);
                final String averageRating = record.get(IMDbTSVFormats.TitleRatingsFormat.headers.averageRating);
                store.updateRating(tconst, averageRating);
            }
        });
    }

    private void processTitlePrincipalsDataset() throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titlePrincipalsURL(), new TitlePrincipalsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(IMDbTSVFormats.TitlePrincipalsFormat.headers.tconst);
                final String nconst = record.get(IMDbTSVFormats.TitlePrincipalsFormat.headers.nconst);
                store.updateName(tconst, nconst);
            }
        });
    }

    private void processTitleEpisodeDataset() throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.titleEpisodeURL(), new TitleEpisodeFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(IMDbTSVFormats.TitleEpisodeFormat.header.tconst);
                final String parentTconst = record.get(IMDbTSVFormats.TitleEpisodeFormat.header.parentTconst);
                final String episodeNumber = record.get(IMDbTSVFormats.TitleEpisodeFormat.header.episodeNumber);
                final String seasonNumber = record.get(IMDbTSVFormats.TitleEpisodeFormat.header.seasonNumber);
                store.updateEpisode(tconst, parentTconst, seasonNumber, episodeNumber);
            }
        });
    }

    private void processNameBasicsDataset() throws IOException {
        IMDbDatasetDownloader.read(RatingsAppProperties.nameBasicsURL(), new NameBasicFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String nconst = record.get(IMDbTSVFormats.NameBasicFormat.headers.nconst);
                final String primaryName = record.get(IMDbTSVFormats.NameBasicFormat.headers.primaryName);
                store.updateName(nconst, primaryName);
            }
        });
    }    
}
