package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.NameRecord;

/**
 *
 */
public interface NameStore extends DatasetStore {

    /**
     * 
     * @param nconst 
     */
    public void addNconst(final String nconst);
    
    /**
     * 
     * @param nconst
     * @return 
     */
    public NameRecord nconst(final String nconst);
    
    /**
     * 
     * @param nconst
     * @param primaryName 
     */
    public void updateName(final String nconst, final String primaryName);
}
