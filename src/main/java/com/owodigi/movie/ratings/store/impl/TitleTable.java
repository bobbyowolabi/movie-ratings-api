package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.TitleStore;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.sql.Connection;
import java.util.List;

public class TitleTable extends DatabaseTable implements TitleStore {
    private static final String TABLE_NAME = "TITLE_STORE";
    protected enum columns {tconst, titleType, primaryTitle}
    
    /**
     * Creates a new TitleTable instance.
     * @param connection
     * @throws java.io.IOException
     */
    public TitleTable(final Connection connection) throws IOException {
        super(connection);
    }

    @Override
    public void addTitle(final String tconst, final String titleType, final String primaryTitle) throws IOException {
        final String sql = insertSql(
            tconst,
            titleType,
            primaryTitle
        );
        executeUpdate(sql);
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.titleType.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryTitle.name(), "VARCHAR(512)")
        );
    }

    /**
     * Executes the given query and returns a TitleRecord result.
     * 
     * @param sql
     * @return
     * @throws IOException 
     */
    private TitleRecord executeQuery(final String sql) throws IOException {
        final TitleRecord record = new TitleRecord();
        final int resultCount = executeQuery(sql, new ResultCallback() {
            
            @Override
            public void process(final ResultSet result) throws SQLException {
                record.setPrimaryTitle(result.getString(columns.primaryTitle.name()));
                record.setTitleType(result.getString(columns.titleType.name()));
                record.setTconst(result.getString(columns.tconst.name()));
            }
        });
        return resultCount == 0 ? null : record;        
    }
    
    @Override
    protected String tableName() {
        return TABLE_NAME;
    }
    
    @Override
    public TitleRecord tconst(final String tconst) throws IOException {
        final String sql = selectAllSql(columns.tconst.name(), tconst);
        return executeQuery(sql);
    }    
    
    @Override
    public TitleRecord title(final String title) throws IOException {
        final String sql = selectAllSql(columns.primaryTitle.name(), title);
        return executeQuery(sql);
    }
}
