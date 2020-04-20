package com.owodigi.movie.ratings.store.impl.util;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Used to execute a query on a provided statement.
 */
public interface StatementCallback {
    /**
     * Executes the given SQL statement, which may return multiple results.
     */
    public static final StatementCallback EXECUTE = (final String sql, final Statement statement) -> {
        statement.execute(sql);
    };
    
    /**
     * Executes the given SQL statement, which may be an INSERT, UPDATE, or 
     * DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement
     */
    public static final StatementCallback EXECUTE_UPDATE = (final String sql, final Statement statement) -> {
        statement.executeUpdate(sql);
    };    
    
    /**
     * Executes the given SQL statement, which returns a single ResultSet object.
     */
    public static final StatementCallback EXECUTE_QUERY = (final String sql, final Statement statement) -> {
        statement.executeQuery(sql);
    };      

    /**
     * Executes the given SQL statement.
     * 
     * @param sql
     * @param statement
     * @throws SQLException 
     */
    public void execute(final String sql, final Statement statement) throws SQLException;
}
