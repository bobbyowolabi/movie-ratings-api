package com.owodigi.imdb.util;

import com.owodigi.movie.ratings.MovieRatingsApp;
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

/**
 *
 */
public class IMDbDatasetDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMDbDatasetDownloader.class);

    /**
     *
     * @param <E>
     * @param url
     * @param tsvFormat
     * @param callback
     * @throws IOException
     */
    public static <E extends TSVFormat> void read(final URL url, E tsvFormat, final IMDbDownloaderCallback callback) throws IOException {
        LOGGER.info("Processing Dataset "+ url + " dataset");
        try (final CSVParser parser = tsvFormat.format().parse(new InputStreamReader(new GZIPInputStream(new BufferedInputStream(url.openStream()))))) {
            validate(tsvFormat, parser);
            for (final CSVRecord record : parser) {
                callback.read(record);
            }
        } catch (final IOException ex) {
            throw new IOException("Unable to download " + url + " due to " + ex.getMessage(), ex);
        }
    }

    private static <E extends TSVFormat> void validate(final E tsvFormat, final CSVParser parser) {
        final List<String> expected = Arrays.asList(tsvFormat.headerClass().getEnumConstants()).stream().map(e -> e.name()).collect(Collectors.toList());
        final List<String> actual = parser.getHeaderNames();
        if (expected.equals(actual) == false) {
            throw new IllegalArgumentException("Expected Header: " + expected + " Actual Header: " + actual);
        }
    }
}
