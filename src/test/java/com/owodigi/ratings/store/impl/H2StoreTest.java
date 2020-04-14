package com.owodigi.ratings.store.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public abstract class H2StoreTest {
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "changeit";
    private static final Path DATABASE_DIRECTORY = Paths.get("./target/test-data/");
    private static final String DATABASE_FILE_PREFIX = "test-h2";
    protected static final String DATABASE_FILE_SUFFIX = ".mv.db";
    protected static final String DATABASE_TRACE_FILE_SUFFIX = ".trace.db";
    private Path dbPath;
    
    
    @Before
    public void setupTest() {
        dbPath = uniqueDbPath();
    }
    
    @After
    public void cleanupTest() throws IOException {
        Files.deleteIfExists(Paths.get(dbPath.toString() + DATABASE_FILE_SUFFIX));
        Files.deleteIfExists(Paths.get(dbPath.toString() + DATABASE_TRACE_FILE_SUFFIX));
    }
    
    private Path uniqueDbPath() {
        final Random random = new Random();
        return DATABASE_DIRECTORY.resolve(DATABASE_FILE_PREFIX + "-" + random.nextInt(10000000));
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
