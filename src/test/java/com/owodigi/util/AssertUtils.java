package com.owodigi.util;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.domain.NameRecord;
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
        Assert.assertEquals("primaryTitle", expected.primaryTitle(), actual.primaryTitle());
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("titleType", expected.titleType(), actual.titleType());
        Assert.assertEquals("nConstList", expected.nconstList(), actual.nconstList());
    }    
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    public static void assertEquals(final EpisodeRecord expected, final EpisodeRecord actual) {
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("parentConst", expected.parentConst(), actual.parentConst());  
        Assert.assertEquals("primaryTitle", expected.primaryTitle(), actual.primaryTitle());
        Assert.assertEquals("averageRating", expected.averageRating(), actual.averageRating());
        Assert.assertEquals("episodeNumber", expected.episodeNumber(), actual.episodeNumber());        
        Assert.assertEquals("seasonNumber", expected.seasonNumber(), actual.seasonNumber());             
        Assert.assertEquals("nConstList", expected.nconstList(), actual.nconstList());
    }    
    
    /**
     * 
     * @param expected
     * @param actual 
     */
    public static void assertEquals(final NameRecord expected, final NameRecord actual) {
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
    
    public static EpisodeRecord newEpisodeRecord(
            final String tconst, 
            final String parentTconst, 
            final String primaryTitle, 
            final String averageRating, 
            final String seasonNumber, 
            final String episodeNumber, 
            final List<String> nConstList){
        final EpisodeRecord record = new EpisodeRecord();
        record.setAverageRating(averageRating);
        record.setEpisodeNumber(episodeNumber);
        record.setNconstList(nConstList);
        record.setParentConst(parentTconst);
        record.setSeasonNumber(seasonNumber);
        record.setTconst(tconst);
        record.setPrimaryTitle(primaryTitle);
        return record;
    }
    
    /**
     * 
     * @param nconst
     * @param primaryName
     * @return 
     */
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
     * @param tconst
     * @param primaryTitle
     * @return 
     */
    public static EpisodeRecord newEpisodeRecord(final String tconst, final String primaryTitle) {
        final EpisodeRecord record = new EpisodeRecord();
        record.setTconst(tconst);
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
