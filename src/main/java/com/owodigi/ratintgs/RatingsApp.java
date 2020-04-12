package com.owodigi.ratintgs;

import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2TitleStore;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbDownloaderCallback;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class RatingsApp {
    
    public static void main(final String[] args) throws IOException {
        final TitleStore titleStore = new H2TitleStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        final EpisodeStore episodeStore = null; //new H2EpisodeStore();
        
        
        /* Read & Load title.basics.tsv.gz */
        IMDbDatasetDownloader.read(RatingsAppProperties.titleBasicsURL(), new TitleBasicsFormat(), new IMDbDownloaderCallback() {
            @Override
            public void read(CSVRecord record) {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (year.equals(RatingsAppProperties.titleYearInclude())) {
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
