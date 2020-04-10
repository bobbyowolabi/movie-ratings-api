package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.TitleRecord;

/**
 *
 */
public interface TitleStore {

    /**
     * 
     * @param tconst
     * @param titleType
     * @param primaryTitle 
     */
    public void add(String tconst, String titleType, String primaryTitle);
    
    /**
     * 
     * @param title
     * @return 
     */
    public TitleRecord title(final String title);
}
