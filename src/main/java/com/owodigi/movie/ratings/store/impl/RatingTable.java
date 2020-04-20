package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.RatingStore;
import com.owodigi.movie.ratings.store.domain.RatingRecord;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import com.owodigi.movie.ratings.store.impl.util.ResultCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RatingTable extends DatabaseTable implements RatingStore {
    private static final String TABLE_NAME = "RATING_STORE";
    protected enum columns {tconst, averageRating}

    public RatingTable(final Connection connection) throws IOException {
        super(connection);
    }

    @Override
    public void addRating(final String tconst, final String averageRating) throws IOException {
        final String sql = insertSql(
            tconst,
            averageRating
        );
        executeUpdate(sql);
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        return Arrays.asList(
            new ColumnConfig(columns.tconst.name(), "VARCHAR(255)"),
            new ColumnConfig(columns.averageRating.name(), "VARCHAR(255)")
        );
    }

    /**
     * Executes the given query and returns the corresponding result.
     * 
     * @param sql
     * @return
     * @throws IOException 
     */    
    private LinkedList<RatingRecord> executeQuery(final String sql) throws IOException {
        final LinkedList<RatingRecord> records = new LinkedList<>();
        executeQuery(sql, new ResultCallback() {

            @Override
            public void process(final ResultSet result) throws SQLException {
                final RatingRecord record = new RatingRecord();
                record.setTconst(result.getString(columns.tconst.name()));
                record.setAverageRating(result.getString(columns.averageRating.name()));                
                records.add(record);
            }
        });
        return records;
    }    
    
    @Override
    public RatingRecord rating(final String tconst) throws IOException {
        final String sql = selectAllSql(columns.tconst.name(), tconst);
        final LinkedList<RatingRecord> results = executeQuery(sql);
        return results.isEmpty() ? null : results.get(results.size() - 1);
    }    
    
    @Override
    public List<RatingRecord> ratings(final List<String> tconsts) throws IOException {
        final String sql = selectAllIn(columns.tconst.name(), tconsts);
        return executeQuery(sql);
    }    
    
    @Override
    protected String tableName() {
        return TABLE_NAME;
    }
}
