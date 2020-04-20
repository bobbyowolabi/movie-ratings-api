package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EpisodeTable extends DatabaseTable implements EpisodeStore {
    private static final String TABLE_NAME = "EPISODE_STORE";
    protected enum columns {tconst, parentTconst, seasonNumber, episodeNumber};

    public EpisodeTable(final Connection connection) throws IOException {
        super(connection);
    }
    
    @Override
    public void add(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException {
        final String sql = insertSql(
            tconst,
            parentTconst,
            seasonNumber,
            episodeNumber
        );
        executeUpdate(sql);        
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.parentTconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.seasonNumber.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.episodeNumber.name(), "VARCHAR(255)")
        );
    }

    /**
     * Executes the given query and returns the corresponding result.
     * 
     * @param sql
     * @return
     * @throws IOException 
     */    
    private LinkedList<EpisodeRecord> executeQuery(final String sql) throws IOException {
        final LinkedList<EpisodeRecord> records = new LinkedList<>();
        executeQuery(sql, new ResultCallback() {

            @Override
            public void process(final ResultSet result) throws SQLException {
                final EpisodeRecord record = new EpisodeRecord();
                record.setTconst(result.getString(columns.tconst.name()));
                record.setParentConst(result.getString(columns.parentTconst.name()));
                record.setEpisodeNumber(result.getString(columns.episodeNumber.name()));
                record.setSeasonNumber(result.getString(columns.seasonNumber.name()));
                records.add(record);
            }
        });
        return records;
    }

    @Override
    public EpisodeRecord tconst(final String tconst) throws IOException {
        final String sql = selectAllSql(columns.tconst.name(), tconst);
        final LinkedList<EpisodeRecord> results = executeQuery(sql);
        return results.isEmpty() ? null : results.get(results.size() - 1);
    }

    @Override
    public List<EpisodeRecord> parentTconst(final String parentTconst) throws IOException {
        final String sql = selectAllSql(columns.parentTconst.name(), parentTconst);
        return executeQuery(sql);
    }    

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }
}
