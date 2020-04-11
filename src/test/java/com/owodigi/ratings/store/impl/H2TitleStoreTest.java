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

    @Test
    public void add() throws IOException {
        if (Files.exists(DB_DIRECTORY) == false) {
            Files.createDirectory(DB_DIRECTORY);
        }
        final TitleStore store = new H2TitleStore(USER_NAME, PASSWORD, DB_DIRECTORY.resolve("test-h2"));
        final String expectedTconst = "tt0000001";
        final String expectedTitleType = "short";
        final String expectedPrimaryTitle = "Animation Film Title";
        store.add(expectedTconst, expectedTitleType, expectedPrimaryTitle);
        final TitleRecord record = store.title("Animation Film Title");
        Assert.assertEquals("averageRating", null, record.averageRating());
        Assert.assertEquals("nConstList", null, record.nConstList());
        Assert.assertEquals("primaryTitle", expectedPrimaryTitle, record.primaryTitle());
        Assert.assertEquals("tconst", expectedTconst, record.tconst());
        Assert.assertEquals("titleType", expectedTitleType, record.titleType());
    }
    // dir does not existx
}
