package com.owodigi.ratings.store.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;

/**
 *
 */
public abstract class H2StoreTest {
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "changeit";
    private static final Path DATABASE_DIRECTORY = Paths.get("./target/test-data/");
    private static final String DATABASE_FILE_PREFIX = "test";
    protected static final String DATABASE_FILE_SUFFIX = ".h2.db";
    private Path dbPath;
    
    
    @Before
    public void setupTest() {
        dbPath = uniqueDbPath();
    }
    
    @After
    public void cleanupTest() throws IOException {
        Files.deleteIfExists(Paths.get(dbPath.toString() + DATABASE_FILE_SUFFIX));
    }
    
    private Path uniqueDbPath() {
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
