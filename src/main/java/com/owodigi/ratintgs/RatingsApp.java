package com.owodigi.ratintgs;

import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbDownloaderCallback;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class RatingsApp {
    private static final String TITLE_BASICS_URL = "https://datasets.imdbws.com/title.basics.tsv.gz";
    
    public static void main(final String[] args) throws IOException {
        final TitleStore titleStore = null; //new H2TitleStore();
        final EpisodeStore episodeStore = null; //new H2EpisodeStore();
        
        
        /* Read & Load title.basics.tsv.gz */
        IMDbDatasetDownloader.read(TITLE_BASICS_URL, new TitleBasicsFormat(), new IMDbDownloaderCallback() {
            @Override
            public void read(CSVRecord record) {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (year.equals("2019")) {
                    final String tconst = record.get(TitleBasicsFormat.header.tconst);
                    final String titleType = record.get(TitleBasicsFormat.header.titleType);
                    final String primaryTitle = record.get(TitleBasicsFormat.header.primaryTitle);
                    if (titleType.equals("tvEpisode")) {
                        episodeStore.add(tconst, primaryTitle);
                    } else {
//                        titleStore.add(tconst, titleType, primaryTitle);
                    }
                }
            }
        });
    }
}
