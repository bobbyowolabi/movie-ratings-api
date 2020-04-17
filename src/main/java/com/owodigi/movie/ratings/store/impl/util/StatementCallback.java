package com.owodigi.movie.ratings.store.impl.util;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public interface StatementCallback {
    public static final StatementCallback EXECUTE = (final String sql, final Statement statement) -> {
        statement.execute(sql);
    };
    
    public static final StatementCallback EXECUTE_UPDATE = (final String sql, final Statement statement) -> {
        statement.executeUpdate(sql);
    };    
    
    public static final StatementCallback EXECUTE_QUERY = (final String sql, final Statement statement) -> {
        statement.executeQuery(sql);
    };      
    

    public void execute(final String sql, final Statement statement) throws SQLException;
}
