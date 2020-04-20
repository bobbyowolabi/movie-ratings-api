package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.impl.util.H2Connection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
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
    protected static final String DATABASE_LOCK_FILE_SUFFIX = ".lock.db";
    private Path dbPath;
    
    protected Connection connection() throws IOException {
        return connection(databasePath().toString());
    }
    
    protected Connection connection(final String path) throws IOException {
        try {
            H2Connection.instance(username(), password(), path).close();
        } catch (SQLException ex) {}
        return H2Connection.instance(username(), password(), path);
    }
    
    @Before
    public void setupTest() {
        dbPath = uniqueDbPath();
    }
    
    @After
    public void cleanupTest() throws IOException {
        final Path databaseFile = Paths.get(dbPath.toString() + DATABASE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
    }
    
    protected static Path uniqueDbPath() {
        return DATABASE_DIRECTORY.resolve(DATABASE_FILE_PREFIX + "-" + UUID.randomUUID());
    }

    protected Path databasePath() {
        return dbPath;
    }
    
    private static String username() {
        return USER_NAME;
    }
    
    private String password() {
        return PASSWORD;
    }
}
