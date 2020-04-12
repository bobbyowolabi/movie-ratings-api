package com.owodigi.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 */
public class IMDbDatasetDownloader {

    /**
     * 
     * @param <E>
     * @param url
     * @param tsvFormat
     * @param callback
     * @throws IOException 
     */
    public static <E extends TSVFormat> void read(final URL url, E tsvFormat, final IMDbDownloaderCallback callback) throws IOException {
        try (final CSVParser parser = tsvFormat.format().parse(new InputStreamReader(new GZIPInputStream(new BufferedInputStream(url.openStream()))))) {
            for (final CSVRecord record : parser) {
                callback.read(record);
            }
        } catch (final IOException ex) {
            throw new IOException("Unable to download " + url + " due to " + ex.getMessage(), ex);
        }
    }
}
