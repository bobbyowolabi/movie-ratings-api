package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class H2TitleStoreTest {
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
        final TitleStore store = new H2TitleStore(USER_NAME, PASSWORD, DB_PATH);
        testAdd(newTitleRecord("tt0000001", "short", "Animation Film Title"), store);
        testAdd(newTitleRecord("tt0000002", "short2", "Animation Film Title2"), store);
    }
    
    private void testAdd(final TitleRecord expected, final TitleStore store) throws IOException {
        store.add(expected.tconst(), expected.titleType(), expected.primaryTitle());
        TitleRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
    }
    
    // dir does not existx
}
