package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class NameTable extends DatabaseStore implements NameStore {
    private static final String TABLE_NAME = "NAME_STORE";
    protected enum columns{nconst, primaryName}

    /**
     * 
     * @param connection
     * @throws IOException 
     */
    public NameTable(final Connection connection) throws IOException {
        super(connection);
    }

    @Override
    public void addNconst(final String nconst) throws IOException {
        final String sql = insertSql(
            nconst,
            "NULL"
        );
        executeUpdate(sql);        
    }
    
    @Override
    public void clear() throws IOException {
        clearTable();
    }
    
    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.nconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryName.name(), "VARCHAR(255)")
        );
    }

    /**
     * 
     * @param sql
     * @return
     * @throws IOException 
     */
    private NameRecord executeQuery(final String sql) throws IOException {
        final NameRecord record = new NameRecord();
        final int resultCount = executeQuery(sql, new ResultCallback() {

            @Override
            public void process(final ResultSet result) throws SQLException {
                record.setNconst(result.getString(columns.nconst.name()));
                record.setPrimaryName(result.getString(columns.primaryName.name()));
            }
        });
        return resultCount == 0 ? null : record;
    }    
    
    @Override
    public List<String> names(final List<String> nconsts) throws IOException {
        final List<String> names = new ArrayList<>();
        for (final String nconst : nconsts) {
            final NameRecord nameRecord = nconst(nconst);
            if (nameRecord == null) {
                names.add(nconst);
            } else {
                names.add(nameRecord.primaryName());
            }
        }
        return names;
    }    
    
    @Override
    public NameRecord nconst(final String nconst) throws IOException {
        final String sql = selectAllSql(columns.nconst.name(), nconst);
        return executeQuery(sql);
    }
    
    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    @Override
    public void updateName(final String nconst, final String primaryName) throws IOException {
        final String sql = updateSql(columns.primaryName.name(), primaryName, columns.nconst.name(), nconst);
        executeUpdate(sql);
    }
}
