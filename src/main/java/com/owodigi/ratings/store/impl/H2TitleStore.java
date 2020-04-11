package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import com.owodigi.ratings.store.impl.util.ResultCallback;
import com.owodigi.ratings.store.impl.util.StatementCallback;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public class H2TitleStore implements TitleStore {
    private static final String DB_URL = "jdbc:h2:";
    private static final String TABLE_NAME = "TITLE_STORE";
    private enum columns {tconst, primaryTitle, averageRating, titleType, nconstSet}
    private final String username;
    private final String password;
    private final Path path;
    private static final String CREATE_TABLE_SQL = new StringBuilder()
            .append("CREATE TABLE ").append(TABLE_NAME).append("(")
            .append(columns.tconst).append(" VARCHAR(255),")
            .append(columns.primaryTitle).append(" VARCHAR(255),")
            .append(columns.averageRating).append(" VARCHAR(255),")
            .append(columns.titleType).append(" VARCHAR(255),")
            .append(columns.nconstSet).append(" VARCHAR")
            .append(");")
            .toString();
    private static final ResultCallback NO_OP_RESULT_CALLBACK = (final ResultSet result) -> {};
    private static final StatementCallback EXECUTE = (final String sql, final Statement statement) -> {
        statement.execute(sql);
    };
    private static final StatementCallback EXECUTE_UPDATE = (final String sql, final Statement statement) -> {
        statement.executeUpdate(sql);
    };    
    private static final StatementCallback EXECUTE_QUERY = (final String sql, final Statement statement) -> {
        statement.executeQuery(sql);
    };    
    
    public H2TitleStore(final String username, final String password, final Path databasePath) throws IOException {
        loadDbDriver();
        this.username = username;
        this.password = password;
        this.path = databasePath;
        if (databaseExists(this.path) == false) {
            createTable();            
        }
    }

    @Override
    public void add(final String tconst, final String titleType, final String primaryTitle) throws IOException {
        final String sql = new StringBuilder()
            .append("INSERT INTO ").append(TABLE_NAME).append(" (")
                .append(columns.tconst).append(", ")
                .append(columns.primaryTitle).append(", ")
                .append(columns.titleType).append(", ")
                .append(columns.averageRating).append(", ")
                .append(columns.nconstSet)
                .append(") ")
            .append("VALUES (")
                .append("'").append(tconst).append("', ")
                .append("'").append(primaryTitle).append("', ")
                .append("'").append(titleType).append("', ")
                .append("NULL").append(", ")
                .append("NULL")
            .append(")")
            .toString();
        executeUpdate(sql, NO_OP_RESULT_CALLBACK);
    }

    private void createTable() throws IOException {
        executeUpdate(CREATE_TABLE_SQL, NO_OP_RESULT_CALLBACK);
    }
    
    private boolean databaseExists(final Path databasePath) throws IOException {
        final Path dataBaseFile = databasePath.resolve(".mv.db");
        return Files.exists(dataBaseFile) == false || Files.size(dataBaseFile) == 0L;
    }
    
    private void execute(final String sql) throws IOException {
        execute(sql, EXECUTE, NO_OP_RESULT_CALLBACK);
    }
    
    private int execute(final String sql, final StatementCallback statementCallback, final ResultCallback resultCallback) throws IOException {
        try (final Connection connection = DriverManager.getConnection(DB_URL + path, username, password);
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
    
    private void executeUpdate(final String sql, final ResultCallback resultCallback) throws IOException {
        execute(sql, EXECUTE_UPDATE, resultCallback);
    }
    
    private void loadDbDriver() {
        try {
            Class.forName ("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Unable to Load database driver", ex);
        }        
    }
    
    @Override
    public TitleRecord title(final String title) throws IOException {
        final String sql = new StringBuilder()
            .append("SELECT * FROM ").append(TABLE_NAME).append(" ")
            .append("WHERE ").append(columns.primaryTitle).append("='").append(title).append("'")
            .toString();
        final TitleRecord record = new TitleRecord();
        execute(sql, EXECUTE_QUERY, new ResultCallback() {
            
            @Override
            public void process(final ResultSet result) throws SQLException {
                record.setAverageRating(result.getString(columns.averageRating.name()));
                final String nConstSet = result.getString(columns.nconstSet.name());
                record.setNconstList(nConstSet == null ? null : Arrays.asList(nConstSet.split("[,]")));
                record.setPrimaryTitle(result.getString(columns.primaryTitle.name()));
                record.setTitleType(result.getString(columns.titleType.name()));
                record.setTconst(result.getString(columns.tconst.name()));
            }
        });
        return record;
    }
}
