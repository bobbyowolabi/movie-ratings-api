package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.store.EpisodeStore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class H2EpisodeStore extends H2Store implements EpisodeStore {

    public H2EpisodeStore(final String username, final String password, final Path databasePath) throws IOException {
        super(username, password, databasePath);
    }

    @Override
    public void add(final String tconst, final String primaryTitle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<ColumnConfig> columnConfigs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EpisodeRecord title(final String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    protected String tableName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
