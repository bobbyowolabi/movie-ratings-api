package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

/**
 *
 */
public interface TitleStore extends DatasetStore {

    /**
     * 
     * @param tconst
     * @param titleType
     * @param primaryTitle 
     * @throws java.io.IOException 
     */
    public void addTitle(String tconst, String titleType, String primaryTitle) throws IOException;

    /**
     * 
     * @param tconst
     * @param nconst 
     * @throws java.io.IOException 
     */
    public void addNconst(String tconst, String nconst) throws IOException;
    
    /**
     * 
     * @param tconst
     * @return 
     * @throws java.io.IOException 
     */
    public TitleRecord tconst(String tconst) throws IOException;
    
    /**
     * 
     * @param primaryTitle
     * @return 
     * @throws java.io.IOException 
     */
    public TitleRecord title(String primaryTitle) throws IOException;
    
    /**
     * 
     * @param tconst
     * @param averageRating 
     * @throws java.io.IOException 
     */
    public void updateRating(String tconst, String averageRating) throws IOException;
}
