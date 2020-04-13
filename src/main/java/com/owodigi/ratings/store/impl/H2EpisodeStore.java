package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.impl.util.ColumnConfig;
import com.owodigi.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class H2EpisodeStore extends H2Store implements EpisodeStore {
    private static final String TABLE_NAME = "EPISODE_STORE";
    private enum columns {tconst, parentTconst, primaryTitle, averageRating, seasonNumber, episodeNumber, nconstList};

    /**
     * Create new H2EpisodeStore instance.
     * 
     * @param username database username
     * @param password database password
     * @param databasePath path to database file (minus extension)
     * @throws IOException If there was an error connecting to database or creating
     * tables
     */
    public H2EpisodeStore(final String username, final String password, final Path databasePath) throws IOException {
        super(username, password, databasePath);
    }

    @Override
    public void add(final String tconst, final String primaryTitle) throws IOException {
        final String sql = insertSql(
            tconst,
            "NULL",
            primaryTitle,
            "NULL",
            "NULL",
            "NULL",
            "NULL"
        );
        executeUpdate(sql);
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.parentTconst.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryTitle.toString(), "VARCHAR(255)"),            
            new ColumnConfig(columns.averageRating.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.seasonNumber.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.episodeNumber.toString(), "VARCHAR(255)"),
            new ColumnConfig(columns.nconstList.toString(), "VARCHAR(255)")
        );
    }

    @Override
    public EpisodeRecord title(final String title) throws IOException {
        final String sql = selectAllSql(columns.primaryTitle.toString(), title);
        final EpisodeRecord record = new EpisodeRecord();
        executeQuery(sql, new ResultCallback() {
            
            @Override
            public void process(final ResultSet result) throws SQLException {
                record.setTconst(result.getString(columns.tconst.name()));
                record.setParentConst(result.getString(columns.parentTconst.name()));
                record.setPrimaryTitle(result.getString(columns.primaryTitle.name()));                
                record.setAverageRating(result.getString(columns.averageRating.name()));
                record.setEpisodeNumber(result.getString(columns.episodeNumber.name()));
                record.setSeasonNumber(result.getString(columns.seasonNumber.name()));
                final String nconstList = result.getString(columns.nconstList.name());
                record.setNconstList(nconstList == null ? null : Arrays.asList(nconstList.split("[,]")));
                
            }
        });
        return record;
    }    
    
    @Override
    protected String tableName() {
        return TABLE_NAME;
    }
}
