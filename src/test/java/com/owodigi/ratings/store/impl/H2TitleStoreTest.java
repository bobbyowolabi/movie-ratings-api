package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class H2TitleStoreTest {
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "changeit";
    private static final Path DB_DIRECTORY = Paths.get("./target/test-data");

    private void assertEquals(final TitleRecord expected, final TitleRecord actual) throws AssertionError {
        Assert.assertEquals("averageRating", expected.averageRating(), actual.averageRating());
        Assert.assertEquals("nConstList", expected.nConstList(), actual.nConstList());
        Assert.assertEquals("primaryTitle", expected.primaryTitle(), actual.primaryTitle());
        Assert.assertEquals("tconst", expected.tconst(), actual.tconst());
        Assert.assertEquals("titleType", expected.titleType(), actual.titleType());
    }
        
    @Test
    public void add() throws IOException {
        if (Files.exists(DB_DIRECTORY) == false) {
            Files.createDirectory(DB_DIRECTORY);
        }
        final TitleStore store = new H2TitleStore(USER_NAME, PASSWORD, DB_DIRECTORY.resolve("test-h2"));
        testAdd(newTitleRecord("tt0000001", "short", "Animation Film Title"), store);
        testAdd(newTitleRecord("tt0000002", "short2", "Animation Film Title2"), store);
    }
    
    private void testAdd(final TitleRecord expected, final TitleStore store) throws IOException {
        store.add(expected.tconst(), expected.titleType(), expected.primaryTitle());
        TitleRecord actual = store.title(expected.primaryTitle());
        assertEquals(expected, actual);
    }
    
    // dir does not existx
    
    private TitleRecord newTitleRecord(final String tcosnt, final String titleType, final String primaryTitle) {
        final TitleRecord record = new TitleRecord();
        record.setTconst(tcosnt);
        record.setTitleType(titleType);
        record.setPrimaryTitle(primaryTitle);
        return record;
    }
}
