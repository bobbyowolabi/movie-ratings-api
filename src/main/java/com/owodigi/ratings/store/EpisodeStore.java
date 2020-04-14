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
     * @throws java.io.IOException 
     */
    public void add(String tconst, String primaryTitle) throws IOException;
 
    /**
     * 
     * @param tconst
     * @return 
     * @throws java.io.IOException 
     */
    public EpisodeRecord tconst(String tconst) throws IOException;    
    
    /**
     * 
     * @param title
     * @return 
     * @throws java.io.IOException 
     */
    public EpisodeRecord title(String title) throws IOException;
    
    /**
     * 
     * @param tconst
     * @param averageRating 
     * @throws java.io.IOException 
     */
    public void updateRating(String tconst, String averageRating) throws IOException;    
}
