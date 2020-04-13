package com.owodigi.util;

import com.owodigi.ratings.domain.TitleRecord;
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
        Assert.assertEquals("averageRating", expected.averageRating(), actual.averageRating());
        Assert.assertEquals("nConstList", expected.nConstList(), actual.nConstList());
        Assert.assertEquals("primaryTitle", expected.primaryTitle(), actual.primaryTitle());
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("titleType", expected.titleType(), actual.titleType());
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
    
    public static TitleRecord newTitleRecord(final String tcosnt, final String titleType, final String primaryTitle) {
        final TitleRecord record = new TitleRecord();
        record.setTconst(tcosnt);
        record.setTitleType(titleType);
        record.setPrimaryTitle(primaryTitle);
        return record;
    }    
    
    public static TitleRecord newTitleRecord (
            final String averageRating, 
            final List<String> nConstList, 
            final String primaryTitle, 
            final String tconst, 
            final String titleType) {
        final TitleRecord record = new TitleRecord();
        record.setAverageRating(averageRating);
        record.setNconstList(nConstList);
        record.setPrimaryTitle(primaryTitle);
        record.setTconst(tconst);
        record.setTitleType(titleType); 
        return record;
    }    
    
    /**
     *
     * @param actual
     * @return
     */
    public static List<String> toList(final CSVRecord record) {
        final List<String> list = new ArrayList<>(record.size());
        record.forEach(token -> list.add(token));
        return list;
    }    
}