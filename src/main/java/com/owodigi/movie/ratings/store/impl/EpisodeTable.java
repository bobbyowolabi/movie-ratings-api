package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class EpisodeTable extends DatabaseStore implements EpisodeStore {
    private static final String TABLE_NAME = "EPISODE_STORE";
    protected enum columns {tconst, parentTconst, primaryTitle, averageRating, seasonNumber, episodeNumber, nconstList};

    /**
     * 
     * @param connection
     * @throws IOException 
     */
    public EpisodeTable(final Connection connection) throws IOException {
        super(connection);
    }

    @Override
    public void addNconst(final String tconst, final String nconst) throws IOException {
        final EpisodeRecord record = tconst(tconst);
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
    public void addTitle(final String tconst, final String primaryTitle) throws IOException {
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
    public void clear() throws IOException {
        clearTable();
    }    
    
    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.parentTconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.primaryTitle.name(), "VARCHAR(512)"),
            new ColumnConfig(columns.averageRating.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.seasonNumber.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.episodeNumber.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.nconstList.name(), "VARCHAR(255)")
        );
    }

    /**
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
                record.setPrimaryTitle(result.getString(columns.primaryTitle.name()));
                record.setAverageRating(result.getString(columns.averageRating.name()));
                record.setEpisodeNumber(result.getString(columns.episodeNumber.name()));
                record.setSeasonNumber(result.getString(columns.seasonNumber.name()));
                final String nconstList = result.getString(columns.nconstList.name());
                record.setNconstList(nconstList == null ? null : Arrays.asList(nconstList.split("[,]")));
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
    public EpisodeRecord title(final String title) throws IOException {
        final String sql = selectAllSql(columns.primaryTitle.name(), title);
        final LinkedList<EpisodeRecord> results = executeQuery(sql);
        return results.isEmpty() ? null : results.get(results.size() - 1);
    }

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    /**
     *
     * @param tconst
     * @param nconstList
     * @throws IOException
     */
    private void updateNconstList(String tconst, List<String> nconstList) throws IOException {
        final String sql = updateSql(columns.nconstList.name(), String.join(",", nconstList), columns.tconst.name(), tconst);
        executeUpdate(sql);
    }

    /**
     * 
     * @param tconst
     * @param parentTconst
     * @param seasonNumber
     * @param episodeNumber 
     */
    @Override
    public void updateEpisode(final String tconst, final String parentTconst, final String seasonNumber, final String episodeNumber) throws IOException {
        final Map<String, String> updateValues = new HashMap<String, String>() {{
            put(columns.parentTconst.name(), parentTconst);
            put(columns.seasonNumber.name(), seasonNumber);
            put(columns.episodeNumber.name(), episodeNumber);
        }};
        final String sql = updateSql(columns.tconst.name(), tconst, updateValues);
        executeUpdate(sql);
    }

    @Override
    public void updateRating(final String tconst, final String averageRating) throws IOException {
        final String sql = updateSql(columns.averageRating.name(), averageRating, columns.tconst.name(), tconst);
        executeUpdate(sql);
    }
}
