package com;

import com.owodigi.util.IMDbTSVFormats.NameBasicFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;


public class IMDbTSVFormatsTest {
    private static final String TEST_RESOURCES_DIR = "src/test/resources/";
            
    /**
     * 
     * @param <E>
     * @param list
     * @return 
     */
    private <E> List<List<E>> asList(final List<E>...list) {
        return Arrays.asList(list);
    }
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    private void assertEquals(final List<String> expected, final CSVRecord actual) throws AssertionError {
        Assert.assertEquals(expected, toList(actual));
    }

    /**
     * 
     * @param expected
     * @param actual
     * @throws AssertionError 
     */    
    private void assertEquals(final List<List<String>> expected, final CSVParser actual) throws AssertionError {
        final Iterator<CSVRecord> actualRecords = actual.iterator();
        try {
            for (final List<String> expectedRecord : expected) {
                Assert.assertEquals("Expected record " + expectedRecord + "; however, there are no more records", true, actualRecords.hasNext());
                final CSVRecord actualRecord = actualRecords.next();
                assertEquals(expectedRecord, actualRecord);
            }
            if (actualRecords.hasNext()) {
                Assert.fail("Did not expect anymore records; however found record " + actualRecords.next());
            }
        } catch (final IllegalStateException ex) {
            Assert.fail("Unable to parse TSV File due to " + ex);
        } 
    }
    
    @Test
    public void nameBaiscsTSV() {
        final String input = TEST_RESOURCES_DIR + "name.basics-sample.tsv";
        final List<List<String>> expected = asList(
            Arrays.asList("nm0000001", "Fred Astaire", "1899", "1987", "soundtrack,actor,miscellaneous", "tt0053137,tt0050419,tt0072308,tt0043044"),
            Arrays.asList("nm0000002", "Lauren Bacall", "1924", "2014", "actress,soundtrack", "tt0071877,tt0038355,tt0037382,tt0117057"),
            Arrays.asList("nm0000003", "Brigitte Bardot", "1934", "\\N", "actress,soundtrack,producer", "tt0049189,tt0057345,tt0054452,tt0059956")
        );
        try (final Reader reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8);
             final CSVParser actual = new NameBasicFormat().format().parse(reader)) {
            assertEquals(expected, actual);
        } catch (final IOException ex) {
            Assert.fail("Unable to parse " + input + " due to " + ex);
        }
    }
    
    @Test
    public void titleRatingsTSV() {
        final String input = TEST_RESOURCES_DIR + "title.ratings-sample.tsv";
        final List<List<String>> expected = asList(
            Arrays.asList("tt0000001", "5.6", "1593"),
            Arrays.asList("tt0000002", "6.0", "195"),
            Arrays.asList("tt0000003", "6.5", "1266")
        );
        try (final Reader reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8);
             final CSVParser actual = new TitleRatingsFormat().format().parse(reader)) {
            assertEquals(expected, actual);
        } catch (final IOException ex) {
            Assert.fail("Unable to parse " + input + " due to " + ex);
        }
    }    
    
    /**
     * 
     * @param actual
     * @return 
     */
    private List<String> toList(final CSVRecord record) {
        final List<String> list = new ArrayList<>(record.size());
        record.forEach(token -> list.add(token));
        return list;
    }
}
