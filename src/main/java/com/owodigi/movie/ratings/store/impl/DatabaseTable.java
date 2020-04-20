package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import static com.owodigi.movie.ratings.store.impl.util.ResultCallback.NO_OP_RESULT_CALLBACK;
import com.owodigi.movie.ratings.store.impl.util.StatementCallback;
import static com.owodigi.movie.ratings.store.impl.util.StatementCallback.EXECUTE;
import static com.owodigi.movie.ratings.store.impl.util.StatementCallback.EXECUTE_QUERY;
import static com.owodigi.movie.ratings.store.impl.util.StatementCallback.EXECUTE_UPDATE;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class DatabaseTable {
    private final Connection connection;

    /**
     *
     * @param connection
     * @throws IOException
     */
    public DatabaseTable(final Connection connection) throws IOException {
        this.connection = connection;
        createTableIfNotExists();
    }
    
    /**
     *
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
        }
    }
    
    /**
     * Clears all the contents of this Store.
     * 
     * @throws IOException 
     */
    public void clear() throws IOException {
        dropTable();
        createTableIfNotExists();
    }    
    
    /**
     *
     * @return
     */
    protected abstract List<ColumnConfig> columnConfigs();

    private void createTableIfNotExists() throws IOException {
        executeUpdate(createTableIfNotExistsSQL(), NO_OP_RESULT_CALLBACK);
    }    
    
    /**
     *
     * @return
     */
    private String createTableIfNotExistsSQL() {
        final StringBuilder statement = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ").append(tableName()).append("(");
        for (final ColumnConfig config : columnConfigs()) {
            statement.append(config.column()).append(" ").append(config.type()).append(",");
        }
        statement.deleteCharAt(statement.length() - 1);
        statement.append(");");
        return statement.toString();
    }

    private void dropTable() throws IOException {
        executeUpdate("DROP TABLE IF EXISTS " + tableName(), NO_OP_RESULT_CALLBACK);
    }

    /**
     *
     * @param sql
     * @throws IOException
     */
    protected void execute(final String sql) throws IOException {
        execute(sql, EXECUTE, NO_OP_RESULT_CALLBACK);
    }

    /**
     *
     * @param sql
     * @param statementCallback
     * @param resultCallback
     * @return
     * @throws IOException
     */
    private int execute(final String sql, final StatementCallback statementCallback, final ResultCallback resultCallback) throws IOException {
        try (final Statement statement = connection.createStatement()) {
            statementCallback.execute(sql, statement);
            final ResultSet result = statement.getResultSet();
            int size = 0;
            while ((result != null) && result.next()) {
                ++size;
                resultCallback.process(result);
            }
            return size;
        } catch (final SQLException ex) {
            throw new IOException("Unable to query " + tableName() + " due to " + ex.getMessage(), ex);
        }
    }

    protected int executeQuery(final String sql, final ResultCallback resultCallback) throws IOException {
        return execute(sql, EXECUTE_QUERY, resultCallback);
    }

    protected void executeUpdate(final String sql) throws IOException {
        execute(sql, EXECUTE_UPDATE, NO_OP_RESULT_CALLBACK);
    }
    /**
     *
     * @param sql
     * @param resultCallback
     * @throws IOException
     */
    private void executeUpdate(final String sql, final ResultCallback resultCallback) throws IOException {
        execute(sql, EXECUTE_UPDATE, resultCallback);
    }

    private String escape(final String value) {
        return value.replace("'", "''");
    }
    
    /**
     *
     * @param values
     * @return
     */
    protected String insertSql(final String...values) {
        final StringBuilder statement = new StringBuilder()
            .append("INSERT INTO ").append(tableName()).append(" (");
        for (final ColumnConfig config : columnConfigs()) {
            statement.append(config.column()).append(", ");
        }
        statement
            .delete(statement.length() - 2, statement.length())
            .append(") VALUES (");
        for (String value : values) {
            value = escape(value);
            if (value.equals("NULL")) {
                statement.append(value).append(", ");
            } else {
                statement.append("'").append(value).append("', ");
            }
        }
        statement
            .delete(statement.length() - 2, statement.length())
            .append(")");
        return statement.toString();
    }

    /**
     *
     * @param column
     * @param value
     * @return
     */
    protected String selectAllSql(final String column, final String value) {
        return new StringBuilder()
            .append("SELECT * FROM ").append(tableName()).append(" ")
            .append("WHERE ").append(column).append("='").append(value).append("'")
            .toString();
    }

    protected String selectAllIn(String column, final Collection<String> values) {
        final StringBuilder sql = new StringBuilder()
            .append("SELECT * FROM ").append(tableName())
            .append(" WHERE ").append(column).append(" IN (");
        for (final String value : values) {
            sql.append("'").append(value).append("', ");
        }
        sql
            .delete(sql.length() - 2, sql.length())
            .append(");");
        return sql.toString();
    }
    
    /**
     *
     *
     * @return
     */
    protected abstract String tableName();

    /**
     *
     * @param column
     * @param value
     * @param conditionColumn
     * @param conditionValue
     * @return
     */
    protected String updateSql(final String column, final String value, final String conditionColumn, final String conditionValue) {
        return updateSql(conditionColumn, conditionValue, Collections.singletonMap(column, value));
    }

    /**
     *
     * @param conditionColumn
     * @param conditionValue
     * @param updateValues
     * @return
     */
    protected String updateSql(final String conditionColumn, final String conditionValue, final Map<String, String> updateValues) {
        final StringBuilder statement = new StringBuilder();
        statement.append("UPDATE ").append(tableName()).append(" SET ");
        updateValues.entrySet().forEach((entry) -> {
            final String updateColumn = entry.getKey();
            final String updateValue = entry.getValue();
            statement.append(updateColumn).append(" = ").append("'").append(updateValue).append("', ");
        });
        statement
            .delete(statement.length() - 2, statement.length())
            .append("WHERE ").append(conditionColumn).append(" = ").append("'").append(conditionValue).append("';");
        return statement.toString();
    }
}
