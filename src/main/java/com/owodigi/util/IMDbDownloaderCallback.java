package com.owodigi.util;

import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public interface IMDbDownloaderCallback {

    public void read(final CSVRecord record);
}
