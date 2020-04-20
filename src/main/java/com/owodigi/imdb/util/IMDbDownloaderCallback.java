package com.owodigi.imdb.util;

import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

public interface IMDbDownloaderCallback {

    /**
     * Provides access to the next Record in a Stream of CSV records.
     * 
     * @param record
     * @throws IOException 
     */
    public void read(final CSVRecord record) throws IOException;
}
