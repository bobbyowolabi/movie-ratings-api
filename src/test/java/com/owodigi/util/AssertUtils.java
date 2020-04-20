package com.owodigi.util;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;

/**
 *
 */
public class AssertUtils {
    /**
     * 
     * @param <E>
     * @param list
     * @return 
     */
    public static <E> List<List<E>> asList(final List<E>...list) {
        return Arrays.asList(list);
    }
    
    /**
     * 
     * @param expected
     * @param actual
     * @throws AssertionError 
     */
    public static void assertEquals(final TitleRecord expected, final TitleRecord actual) throws AssertionError {
        Assert.assertEquals("primaryTitle", expected.primaryTitle(), actual.primaryTitle());
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("titleType", expected.titleType(), actual.titleType());
    }    
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    public static void assertEquals(final EpisodeRecord expected, final EpisodeRecord actual) {
        if (assertEqualsIfNull("EpisodeRecord", expected, actual)) {
            return;
        }
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("parentConst", expected.parentConst(), actual.parentConst());  
        Assert.assertEquals("episodeNumber", expected.episodeNumber(), actual.episodeNumber());        
        Assert.assertEquals("seasonNumber", expected.seasonNumber(), actual.seasonNumber());             
    }    
    
    private static boolean assertEqualsIfNull(final String message, final Object expected, final Object actual) {
        if (expected == null) {
            Assert.assertNull("Actual is not null: " + message, actual);
        } else {
            Assert.assertNotNull("Actual is null: " + message, actual);
        }
        return expected == null || actual == null;
    }
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    public static void assertEquals(final NameRecord expected, final NameRecord actual) {
        if(assertEqualsIfNull("NameRecord", expected, actual)) {
            return;
        }
        Assert.assertEquals("nconst", expected.nconst(), actual.nconst());
        Assert.assertEquals("primaryName", expected.primaryName(), actual.primaryName());
    }
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    public static void assertEquals(final List<String> expected, final CSVRecord actual) throws AssertionError {
        Assert.assertEquals(expected, toList(actual));
    }

    /**
     * 
     * @param expected
     * @param actual
     * @throws AssertionError 
     */    
    public static void assertEquals(final List<List<String>> expected, final CSVParser actual) throws AssertionError {
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
    
    public static void assertQuery(final String message, final String url, final String expected) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            final int statusCode = connection.getResponseCode();
            if (statusCode <= 299) {
                final String actual = toString(connection.getInputStream());
                Assert.assertEquals("json response", expected, actual);
            } else {
                Assert.fail(message + ": Unexpected response code " + statusCode + toString(connection.getErrorStream()));
            }
        } catch (final MalformedURLException ex) {
            Assert.fail(message + ": URL to movie API is invalid due to " + ex);
        } catch (final ProtocolException ex) {
            Assert.fail(message + ": The request method is invalud due to " + ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }    
    
    /**
     * Does not maintain original line ending characters
     * 
     * @param inputStream
     * @return
     * @throws IOException 
     */
    private static String toString(final InputStream inputStream) throws IOException {
        final StringBuilder value = new StringBuilder();
        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            value.append(reader.readLine()).append("\n");
        }
        value.deleteCharAt(value.length() - 1);
        return value.toString();
    }     
    
    public static EpisodeRecord newEpisodeRecord(
            final String tconst, 
            final String parentTconst, 
            final String seasonNumber, 
            final String episodeNumber){
        final EpisodeRecord record = new EpisodeRecord();
        record.setEpisodeNumber(episodeNumber);
        record.setParentConst(parentTconst);
        record.setSeasonNumber(seasonNumber);
        record.setTconst(tconst);
        return record;
    }
    
    public static NameRecord newNameRecord(final String nconst, final String primaryName) {
        final NameRecord record = new NameRecord();
        record.setNconst(nconst);
        record.setPrimaryName(primaryName);
        return record;
    }
    
    public static TitleRecord newTitleRecord(final String tcosnt, final String titleType, final String primaryTitle) {
        final TitleRecord record = new TitleRecord();
        record.setTconst(tcosnt);
        record.setTitleType(titleType);
        record.setPrimaryTitle(primaryTitle);
        return record;
    }    
    
    /**
     *
     * @param record
     * @return
     */
    public static List<String> toList(final CSVRecord record) {
        final List<String> list = new ArrayList<>(record.size());
        record.forEach(token -> list.add(token));
        return list;
    }    
}
