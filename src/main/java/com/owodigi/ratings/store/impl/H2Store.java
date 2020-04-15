package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.store.impl.util.ColumnConfig;
import com.owodigi.ratings.store.impl.util.ResultCallback;
import static com.owodigi.ratings.store.impl.util.ResultCallback.NO_OP_RESULT_CALLBACK;
import com.owodigi.ratings.store.impl.util.StatementCallback;
import static com.owodigi.ratings.store.impl.util.StatementCallback.EXECUTE;
import static com.owodigi.ratings.store.impl.util.StatementCallback.EXECUTE_QUERY;
import static com.owodigi.ratings.store.impl.util.StatementCallback.EXECUTE_UPDATE;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class H2Store {
    private static final String DB_URL = "jdbc:h2:";
    private static final String DB_DRIVER = "org.h2.Driver";
    private final String username;
    private final String password;
    private final Path path;

    /**
     *
     * @param username
     * @param password
     * @param databasePath
     * @throws IOException
     */
    public H2Store(final String username, final String password, final Path databasePath) throws IOException {
        loadDbDriver();
        this.username = username;
        this.password = password;
        this.path = databasePath;
        createTableIfNotExists();
    }

    /**
     * Clears all the contents of this Store.
     * 
     * @throws IOException 
     */
    protected void clearTable() throws IOException {
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
        try (final Connection connection = DriverManager.getConnection(DB_URL + path + ";mv_store=false", username, password);
             final Statement statement = connection.createStatement()) {
            statementCallback.execute(sql, statement);
            final ResultSet result = statement.getResultSet();
            int size = 0;
            while ((result != null) && result.next()) {
                ++size;
                resultCallback.process(result);
            }
            return size;
        } catch (final SQLException ex) {
            throw new IOException("Unable to query Title Store due to " + ex.getMessage(), ex);
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
        for (final String value : values) {
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
     */
    private void loadDbDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (final ClassNotFoundException ex) {
            throw new IllegalStateException("Unable to Load database driver", ex);
        }
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
