package com.owodigi.movie.ratings.store.impl.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class H2Connection {
    private static final String DB_URL = "jdbc:h2:";
    private static final String DB_DRIVER = "org.h2.Driver";
    private static Connection connection = null;
    
    protected H2Connection(){}
    
    protected static Connection connection(final String username, final String password, final String path) throws IOException {
        loadDbDriver();
        try {
            return DriverManager.getConnection(DB_URL + path + ";mv_store=false", username, password);
        } catch (final SQLException ex) {
            throw new IOException("Unable to establish connection to DB due to " + ex.getMessage(), ex);
        }        
    }
    
    public static boolean isClosed() throws IOException {
        try {
            return connection == null ? false : connection.isClosed();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
    
    /**
     *
     */
    private static void loadDbDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (final ClassNotFoundException ex) {
            throw new IllegalStateException("Unable to Load database driver", ex);
        }
    }    
    
    public static Connection instance(final String username, final String password, final String path) throws IOException {
        return (connection == null) || isClosed() ? (connection = connection(username, password, path)) : connection;
    }
}
