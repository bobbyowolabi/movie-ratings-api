package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class H2TitleStoreTest extends H2StoreTest {
       
    @Test
    public void add() throws IOException {
        final TitleStore store = new H2TitleStore(userName(), password(), databasePath());
        testAdd(newTitleRecord("tt0000001", "short", "Animation Film Title"), store);
        testAdd(newTitleRecord("tt0000002", "short2", "Animation Film Title2"), store);
    }
    
    @Test
    public void update() throws IOException {
        final TitleStore store = new H2TitleStore(userName(), password(), databasePath());
        final TitleRecord originalRecord = newTitleRecord("tt0000001", "short", "Animation Film Title");
        testAdd(originalRecord, store);
        store.updateRating(originalRecord.tconst(), "5.5");
        final TitleRecord updatedRecord = store.title(originalRecord.primaryTitle());
        Assert.assertEquals("averageRating", "5.5", updatedRecord.averageRating());
    }
    
    private void testAdd(final TitleRecord expected, final TitleStore store) throws IOException {
        store.add(expected.tconst(), expected.titleType(), expected.primaryTitle());
        TitleRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
        actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
    
    @Test
    public void dbDirectoryDoesNotExist() throws IOException {
        final Path databasePath = Paths.get("./target/test-data2/foo");
        final Path databaseFile = Paths.get(databasePath.toString() + DATABASE_FILE_SUFFIX);
        final Path databaseTraceFile = Paths.get(databasePath.toString() + DATABASE_TRACE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        Files.deleteIfExists(databaseTraceFile);
        try {
            new H2TitleStore(userName(), password(), databasePath);
            Assert.assertEquals(databaseFile + "exists", true, Files.exists(databaseFile));
        } finally {
            Files.deleteIfExists(databaseFile);
            Files.deleteIfExists(databaseTraceFile);
        }
    }
}
