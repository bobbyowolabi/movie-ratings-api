package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.store.EpisodeStore;
import static com.owodigi.ratings.store.impl.H2StoreTest.DATABASE_FILE_SUFFIX;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class H2EpisodeStoreTest extends H2StoreTest {
       
    @Test
    public void add() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        testAdd(newEpisodeRecord("tt0000001", "Animation Film Title"), store);
        testAdd(newEpisodeRecord("tt0000002", "Animation Film Title2"), store);
    }

    @Test
    public void update() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        final EpisodeRecord originalRecord = newEpisodeRecord("tt0000001", "Animation Film Title");
        testAdd(originalRecord, store);
        store.updateRating(originalRecord.tconst(), "5.5");
        final EpisodeRecord updatedRecord = store.title(originalRecord.primaryTitle());
        Assert.assertEquals("averageRating", "5.5", updatedRecord.averageRating());
    }    
    
    @Test
    public void dbDirectoryDoesNotExist() throws IOException {
        final Path databasePath = Paths.get("./target/test-data3/foo");
        final Path databaseFile = Paths.get(databasePath.toString() + DATABASE_FILE_SUFFIX);
        final Path databaseTraceFile = Paths.get(databasePath.toString() + DATABASE_TRACE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        Files.deleteIfExists(databaseTraceFile);
        try {
            new H2EpisodeStore(userName(), password(), databasePath);
            Assert.assertEquals(databaseFile + "exists", true, Files.exists(databaseFile));
        } finally {
            Files.deleteIfExists(databaseFile);
            Files.deleteIfExists(databaseTraceFile);
        }
    }
    
    /**
     * 
     * @param expected
     * @param store
     * @throws IOException 
     */
    private void testAdd(final EpisodeRecord expected, final EpisodeStore store) throws IOException {
        store.add(expected.tconst(), expected.primaryTitle());
        EpisodeRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
        actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
}
