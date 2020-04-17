package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import static com.owodigi.movie.ratings.store.impl.H2StoreTest.DATABASE_FILE_SUFFIX;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
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
public class H2EpisodeStoreTest extends H2StoreTest {
       
    @Test
    public void addTitle() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        testAddTitle(newEpisodeRecord("tt0000001", "Animation Film Title"), store);
        testAddTitle(newEpisodeRecord("tt0000002", "Animation Film Title2"), store);
    }
    
    @Test
    public void addNconst() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        final String tconst = "tt0000001";
        store.addTitle(tconst, "Animation Film Title");
        testAddNconst(tconst, store, "n1", "n2", "n3", "n4", "n5");
    }

    @Test(expected = IllegalStateException.class)
    public void addNconstWhereTconstNonExistent() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        final String tconst = "tt0000001";
        store.addTitle(tconst, "Animation Film Title");
        testAddNconst("tt0000002", store, "n1");
    }
    
    @Test
    public void updateRating() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(userName(), password(), databasePath());
        final EpisodeRecord originalRecord = newEpisodeRecord("tt0000001", "Animation Film Title");
        testAddTitle(originalRecord, store);
        store.updateRating(originalRecord.tconst(), "5.5");
        final EpisodeRecord updatedRecord = store.title(originalRecord.primaryTitle());
        Assert.assertEquals("averageRating", "5.5", updatedRecord.averageRating());
    }    
    
    @Test
    public void dbDirectoryDoesNotExist() throws IOException {
        final Path databasePath = Paths.get("./target/test-data3/foo");
        final Path databaseFile = Paths.get(databasePath.toString() + DATABASE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        try {
            new H2EpisodeStore(userName(), password(), databasePath);
            Assert.assertEquals(databaseFile + "exists", true, Files.exists(databaseFile));
        } finally {
            Files.deleteIfExists(databaseFile);
            Files.deleteIfExists(databaseFile.getParent());
        }
    }
    
    private void testAddNconst(final String tconst, final EpisodeStore store, final String...nconsts) throws IOException {
        final List<String> expected = new ArrayList<>();
        for (final String nconst : nconsts) {
            expected.add(nconst);
            store.addNconst(tconst, nconst);
            final List<String> actual = store.tconst(tconst).nconstList();
            Assert.assertEquals("nconstList", expected, actual);
        }
    }
    
    /**
     * 
     * @param expected
     * @param store
     * @throws IOException 
     */
    private void testAddTitle(final EpisodeRecord expected, final EpisodeStore store) throws IOException {
        store.addTitle(expected.tconst(), expected.primaryTitle());
        EpisodeRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
        actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
}
