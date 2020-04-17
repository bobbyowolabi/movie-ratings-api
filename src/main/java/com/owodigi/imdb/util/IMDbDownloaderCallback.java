package com.owodigi.imdb.util;

import java.io.IOException;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public interface IMDbDownloaderCallback {

    public void read(final CSVRecord record) throws IOException;
}
