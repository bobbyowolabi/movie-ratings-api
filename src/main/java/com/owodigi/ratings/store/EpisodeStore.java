package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.EpisodeRecord;
import java.io.IOException;

/**
 *
 */
public interface EpisodeStore {

    /**
     * 
     * @param tconst
     * @param primaryTitle 
     */
    public void add(final String tconst, String primaryTitle) throws IOException;
    
    /**
     * 
     * @param title
     * @return 
     */
    public EpisodeRecord title(final String title) throws IOException;
}
