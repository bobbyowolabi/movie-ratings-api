package com.owodigi.imdb.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMDbDatasetDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMDbDatasetDownloader.class);
    private static final int MAX_COUNT = 1_000_000;

    private static void count(final int count, final long start) {
        final long end = System.currentTimeMillis();
        LOGGER.info("Processed " + count + " records in " + (end - start) + " ms");
    }
    
    /**
     * Downloads and processes the dataset.
     * 
     * @param <E>
     * @param url
     * @param tsvFormat
     * @param callback
     * @throws IOException
     */
    public static <E extends TSVFormat> void read(final URL url, E tsvFormat, final IMDbDownloaderCallback callback) throws IOException {
        LOGGER.info("Processing Dataset "+ url + " dataset");
        int count = 0;
        try (final CSVParser parser = tsvFormat.format().parse(new InputStreamReader(new GZIPInputStream(new BufferedInputStream(url.openStream()))))) {
            validate(tsvFormat, parser);
            long start = System.currentTimeMillis();
            for (final CSVRecord record : parser) {
                callback.read(record);
                if (++count == MAX_COUNT) {
                    count(count, start);
                    count = 0;
                    start = System.currentTimeMillis();
                }
            }
            if (count > 0) {
                count(count, start);
            }
        } catch (final IOException ex) {
            throw new IOException("Unable to download " + url + " due to " + ex.getMessage(), ex);
        }
    }

    /**
     * Ensures that the header in the given Parser matches the expected header
     * values.
     * 
     * @param <E>
     * @param tsvFormat
     * @param parser 
     */
    private static <E extends TSVFormat> void validate(final E tsvFormat, final CSVParser parser) {
        final List<String> expected = Arrays.asList(tsvFormat.headerClass().getEnumConstants()).stream().map(e -> e.name()).collect(Collectors.toList());
        final List<String> actual = parser.getHeaderNames();
        if (expected.equals(actual) == false) {
            throw new IllegalArgumentException("Expected Header: " + expected + " Actual Header: " + actual);
        }
    }
}
