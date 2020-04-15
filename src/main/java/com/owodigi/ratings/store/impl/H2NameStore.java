package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.NameRecord;
import com.owodigi.ratings.store.NameStore;
import com.owodigi.ratings.store.impl.util.ColumnConfig;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class H2NameStore extends H2Store implements NameStore {

    /**
     * 
     * @param username
     * @param password
     * @param databasePath
     * @throws IOException 
     */
    public H2NameStore(final String username, final String password, final Path databasePath) throws IOException {
        super(username, password, databasePath);
    }

    @Override
    public void addNconst(final String nconst) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clear() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected List<ColumnConfig> columnConfigs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NameRecord nconst(final String nconst) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected String tableName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateName(final String nconst, final String primaryName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
