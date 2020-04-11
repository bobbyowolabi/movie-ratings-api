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
     */
    public void add(String tconst, String titleType, String primaryTitle) throws IOException;
    
    /**
     * 
     * @param title
     * @return 
     */
    public TitleRecord title(final String title) throws IOException;
}
