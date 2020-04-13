package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.EpisodeRecord;

/**
 *
 */
public interface EpisodeStore {

    /**
     * 
     * @param tconst
     * @param primaryTitle 
     */
    public void add(final String tconst, String primaryTitle);
    
    /**
     * 
     * @param title
     * @return 
     */
    public EpisodeRecord title(final String title);
}
