package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.store.impl.util.ColumnConfig;
import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import com.owodigi.ratings.store.impl.util.ResultCallback;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class H2TitleStore extends H2Store implements TitleStore {
    private static final String TABLE_NAME = "TITLE_STORE";
    protected enum columns {tconst, primaryTitle, averageRating, titleType, nconstList}
    
    /**
     * 
     * @param username
     * @param password
     * @param databasePath
     * @throws IOException 
     */
    public H2TitleStore(final String username, final String password, final Path databasePath) throws IOException {
        super(username, password, databasePath);
    }

    @Override
    public void addNconst(final String tconst, final String nconst) throws IOException {
        final TitleRecord record = tconst(tconst);
        if (record == null) {
            throw new IllegalStateException("Unable to add nconst " + nconst + " to this Store because tconst " + tconst + " does not exists");
        }
        final List<String> nconstList = record.nconstList();
        final List<String> updatedNconstList;
        if (nconstList == null) {
            updatedNconstList = Arrays.asList(nconst);
        } else {
            updatedNconstList = new ArrayList<>(nconstList);
            updatedNconstList.add(nconst);
        }
        updateNconstList(tconst, updatedNconstList);
    }    
    
    @Override
    public void addTitle(final String tconst, final String titleType, final String primaryTitle) throws IOException {
        final String sql = insertSql(
            tconst,
            primaryTitle,
            titleType,
            "NULL",
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
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryTitle.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.titleType.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.averageRating.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.nconstList.name(), "VARCHAR(255)")
        );
    }

    /**
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
                record.setAverageRating(result.getString(columns.averageRating.name()));
                final String nconstList = result.getString(columns.nconstList.name());
                record.setNconstList(nconstList == null ? null : Arrays.asList(nconstList.split("[,]")));
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
    
    /**
     * 
     * @param tconst
     * @param updatedNconstList 
     */
    private void updateNconstList(final String tconst, final List<String> nconstList) throws IOException {
        final String sql = updateSql(columns.nconstList.name(), String.join(",", nconstList), columns.tconst.name(), tconst);
        executeUpdate(sql);
    }    
    
    @Override
    public void updateRating(final String tconst, final String averageRating) throws IOException {
        final String sql = updateSql(columns.averageRating.name(), averageRating, columns.tconst.name(), tconst);
        executeUpdate(sql);
    }
}
