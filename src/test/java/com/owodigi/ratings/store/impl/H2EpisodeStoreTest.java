package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class H2EpisodeStoreTest {
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "changeit";
    private static final Path DB_PATH = Paths.get("./target/test-data/test-h2");
    private static final Path TEST_DB_PATH = Paths.get("./target/test-data/test-h2.mv.db");
    private static final Path TEST_DB_TRACE_PATH = Paths.get("./target/test-data/test-h2.trace.db");
    
    @Before
    public void setupTest() throws IOException {
        Files.deleteIfExists(TEST_DB_PATH);
        Files.deleteIfExists(TEST_DB_TRACE_PATH);
    }
       
    @Test
    public void add() throws IOException {
        if (Files.exists(DB_PATH) == false) {
            Files.createDirectory(DB_PATH);
        }
        final EpisodeStore store = new H2EpisodeStore(USER_NAME, PASSWORD, DB_PATH);
        testAdd(newEpisodeRecord("tt0000001", "Animation Film Title"), store);
        testAdd(newEpisodeRecord("tt0000002", "Animation Film Title2"), store);
    }

    @Test
    public void dbDirectoryDoesNotExist() {
        Assert.fail("Not Implemented");
    }
    
    /**
     * 
     * @param expected
     * @param store
     * @throws IOException 
     */
    private void testAdd(final EpisodeRecord expected, final EpisodeStore store) throws IOException {
        store.add(expected.tconst(), expected.primaryTitle());
        final EpisodeRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
    }
}
