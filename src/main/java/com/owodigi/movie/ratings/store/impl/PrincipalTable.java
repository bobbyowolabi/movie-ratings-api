package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.PrincipalStore;
import com.owodigi.movie.ratings.store.domain.PrincipalRecord;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PrincipalTable extends DatabaseTable implements PrincipalStore {
    private static final String TABLE_NAME = "PRINCIPAL_STORE";
    protected enum columns {tconst, nconst, ordering}

    public PrincipalTable(Connection connection) throws IOException {
        super(connection);
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.nconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.ordering.name(), "VARCHAR(255)")
        );
    }

    /**
     * Executes the given query and returns the corresponding result.
     * 
     * @param sql
     * @return
     * @throws IOException 
     */        
    private LinkedList<PrincipalRecord> executeQuery(final String sql) throws IOException {
        final LinkedList<PrincipalRecord> records = new LinkedList<>();
        executeQuery(sql, new ResultCallback() {

            @Override
            public void process(final ResultSet result) throws SQLException {
                final PrincipalRecord record = new PrincipalRecord();
                record.setTconst(result.getString(columns.tconst.name()));
                record.setNconst(result.getString(columns.nconst.name()));
                record.setOrdering(result.getString(columns.ordering.name()));
                records.add(record);
            }
        });
        return records;
    }    
    
    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    @Override
    public void add(final String tconst, final String nconst, final String ordering) throws IOException {
        final String sql = insertSql(
            tconst,
            nconst,
            ordering
        );
        executeUpdate(sql);
    }
    
    @Override
    public List<PrincipalRecord> tconst(final String tconst) throws IOException {
        final String sql = selectAllSql(columns.tconst.name(), tconst);
        return executeQuery(sql);
    }    
}
