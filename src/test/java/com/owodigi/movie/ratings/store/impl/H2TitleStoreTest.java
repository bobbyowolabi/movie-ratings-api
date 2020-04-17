package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.TitleStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class H2TitleStoreTest extends H2StoreTest {
       
    @Test
    public void addTitle() throws IOException {
        final TitleStore store = new H2TitleStore(userName(), password(), databasePath());
        testAdd(newTitleRecord("tt0000001", "short", "Animation Film Title"), store);
        testAdd(newTitleRecord("tt0000002", "short2", "Animation Film Title2"), store);
    }
    
    @Test
    public void addNconst() throws IOException {
        final TitleStore store = new H2TitleStore(userName(), password(), databasePath());
        final String tconst = "tt0000001";
        store.addTitle(tconst, "short", "Animation Film Title");
        testAddNconst(tconst, store, "n1", "n2", "n3", "n4", "n5");
    }    
    
    @Test
    public void updateRating() throws IOException {
        final TitleStore store = new H2TitleStore(userName(), password(), databasePath());
        final TitleRecord originalRecord = newTitleRecord("tt0000001", "short", "Animation Film Title");
        testAdd(originalRecord, store);
        store.updateRating(originalRecord.tconst(), "5.5");
        final TitleRecord updatedRecord = store.title(originalRecord.primaryTitle());
        Assert.assertEquals("averageRating", "5.5", updatedRecord.averageRating());
    }
    
    private void testAdd(final TitleRecord expected, final TitleStore store) throws IOException {
        store.addTitle(expected.tconst(), expected.titleType(), expected.primaryTitle());
        TitleRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
        actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
    
    private void testAddNconst(final String tconst, final TitleStore store, final String...nconsts) throws IOException {
        final List<String> expected = new ArrayList<>();
        for (final String nconst : nconsts) {
            expected.add(nconst);
            store.addNconst(tconst, nconst);
            final List<String> actual = store.tconst(tconst).nconstList();
            Assert.assertEquals("nconstList", expected, actual);
        }
    }    
    
    @Test
    public void dbDirectoryDoesNotExist() throws IOException {
        final Path databasePath = Paths.get("./target/test-data2/foo");
        final Path databaseFile = Paths.get(databasePath.toString() + DATABASE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        try {
            new H2TitleStore(userName(), password(), databasePath);
            Assert.assertEquals(databaseFile + "exists", true, Files.exists(databaseFile));
        } finally {
            Files.deleteIfExists(databaseFile);
            Files.deleteIfExists(databaseFile.getParent());
        }
    }
}
