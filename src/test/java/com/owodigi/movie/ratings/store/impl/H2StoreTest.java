package com.owodigi.movie.ratings.store.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class H2StoreTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2StoreTest.class);
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "changeit";
    private static final Path DATABASE_DIRECTORY = Paths.get("./target/test-data/");
    private static final String DATABASE_FILE_PREFIX = "test";
    protected static final String DATABASE_FILE_SUFFIX = ".h2.db";
    private Path dbPath;
    
    @Before
    public void setupTest() {
        dbPath = uniqueDbPath();
        LOGGER.info("Setting unique DB path " + dbPath);
    }
    
    @After
    public void cleanupTest() throws IOException {
        final Path databaseFile = Paths.get(dbPath.toString() + DATABASE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        LOGGER.info("Deleting database file " + databaseFile);
    }
    
    protected static Path uniqueDbPath() {
        return DATABASE_DIRECTORY.resolve(DATABASE_FILE_PREFIX + "-" + UUID.randomUUID());
    }

    protected Path databasePath() {
        return dbPath;
    }
    
    protected String userName() {
        return USER_NAME;
    }
    
    protected String password() {
        return PASSWORD;
    }
}
