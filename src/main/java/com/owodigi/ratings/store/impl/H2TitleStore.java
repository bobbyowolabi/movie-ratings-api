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
import java.util.List;

/**
 *
 */
public class H2TitleStore extends H2Store implements TitleStore {
    private static final String TABLE_NAME = "TITLE_STORE";

    private enum columns {tconst, primaryTitle, averageRating, titleType, nconstList}
    
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
    public void add(final String tconst, final String titleType, final String primaryTitle) throws IOException {
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
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryTitle.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.titleType.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.averageRating.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.nconstList.toString(), "VARCHAR(255)")
        );
    }

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }
    
    @Override
    public TitleRecord title(final String title) throws IOException {
        final String sql = selectAllSql(columns.primaryTitle.toString(), title);
        final TitleRecord record = new TitleRecord();
        executeQuery(sql, new ResultCallback() {
            
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
        return record;
    }
    
    @Override
    public void updateRating(final String tconst, final String averageRating) throws IOException {
        final String sql = updateSql(columns.averageRating.toString(), averageRating, columns.tconst.toString(), tconst);
        executeUpdate(sql);
    }
}
