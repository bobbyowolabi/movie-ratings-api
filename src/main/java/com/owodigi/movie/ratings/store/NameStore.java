package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

/**
 *
 */
public interface NameStore extends DatasetStore {

    /**
     * 
     * @param nconst 
     */
    public void addNconst(final String nconst) throws IOException;
    
    /**
     * 
     * @param nconst
     * @return 
     */
    public NameRecord nconst(final String nconst) throws IOException;
    
    /**
     * 
     * @param nconst
     * @param primaryName 
     */
    public void updateName(final String nconst, final String primaryName) throws IOException;
}
