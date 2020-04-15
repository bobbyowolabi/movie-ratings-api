package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.NameRecord;
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
