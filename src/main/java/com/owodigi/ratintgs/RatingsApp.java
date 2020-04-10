package com.owodigi.ratintgs;

import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import java.io.IOException;
import com.owodigi.util.IMDbDownloaderCallback;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class RatingsApp {
    private static final String TITLE_BASICS_URL = "https://datasets.imdbws.com/title.basics.tsv.gz";
    
    public static void main(final String[] args) throws IOException {
        
        /* Read & Load title.basics.tsv.gz */
        IMDbDatasetDownloader.read(TITLE_BASICS_URL, new TitleBasicsFormat(), new IMDbDownloaderCallback() {
            
            @Override
            public void read(final CSVRecord record) {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (year.equals("2019")) {
                    
                }
            }
        });
    }
}
