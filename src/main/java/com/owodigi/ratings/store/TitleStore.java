package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.TitleRecord;
import java.io.IOException;

/**
 *
 */
public interface TitleStore {

    /**
     * 
     * @param tconst
     * @param titleType
     * @param primaryTitle 
     * @throws java.io.IOException 
     */
    public void add(String tconst, String titleType, String primaryTitle) throws IOException;
    
    /**
     * 
     * @param title
     * @return 
     * @throws java.io.IOException 
     */
    public TitleRecord title(String title) throws IOException;

    /**
     * 
     * @param tconst
     * @param averageRating 
     */
    public void updateRating(String tconst, String averageRating) throws IOException;
}
