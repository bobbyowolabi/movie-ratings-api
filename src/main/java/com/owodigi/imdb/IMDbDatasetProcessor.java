package com.owodigi.imdb;

import com.owodigi.movie.ratings.util.MovieRatingsAppProperties;
import com.owodigi.imdb.util.IMDbDatasetDownloader;
import com.owodigi.imdb.util.IMDbDownloaderCallback;
import com.owodigi.imdb.util.IMDbTSVFormats;
import com.owodigi.imdb.util.IMDbTSVFormats.NameBasicFormat;
import com.owodigi.imdb.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.imdb.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.imdb.util.IMDbTSVFormats.TitlePrincipalsFormat;
import com.owodigi.imdb.util.IMDbTSVFormats.TitleRatingsFormat;
import com.owodigi.movie.ratings.store.domain.RatingStore;
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
        IMDbDatasetDownloader.read(MovieRatingsAppProperties.titleBasicsURL(), new TitleBasicsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (MovieRatingsAppProperties.titleIncludeYears().contains(year)) {
                    final String tconst = record.get(TitleBasicsFormat.header.tconst);
                    final String titleType = record.get(TitleBasicsFormat.header.titleType);
                    final String primaryTitle = record.get(TitleBasicsFormat.header.primaryTitle);
                    store.addTitle(tconst, titleType, primaryTitle);
                }
            }
        });
    }

    private void processTitleRatingsDataset() throws IOException {
        IMDbDatasetDownloader.read(MovieRatingsAppProperties.titleRatingsURL(), new TitleRatingsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitleRatingsFormat.headers.tconst);
                final String averageRating = record.get(TitleRatingsFormat.headers.averageRating);
                store.updateRating(tconst, averageRating);
            }
        });
    }

    private void processTitlePrincipalsDataset() throws IOException {
        IMDbDatasetDownloader.read(MovieRatingsAppProperties.titlePrincipalsURL(), new TitlePrincipalsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitlePrincipalsFormat.headers.tconst);
                final String nconst = record.get(TitlePrincipalsFormat.headers.nconst);
                store.addNconst(tconst, nconst);
            }
        });
    }

    private void processTitleEpisodeDataset() throws IOException {
        IMDbDatasetDownloader.read(MovieRatingsAppProperties.titleEpisodeURL(), new TitleEpisodeFormat(), new IMDbDownloaderCallback() {

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
        IMDbDatasetDownloader.read(MovieRatingsAppProperties.nameBasicsURL(), new NameBasicFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String nconst = record.get(IMDbTSVFormats.NameBasicFormat.headers.nconst);
                final String primaryName = record.get(IMDbTSVFormats.NameBasicFormat.headers.primaryName);
                store.updateName(nconst, primaryName);
            }
        });
    }    
}
