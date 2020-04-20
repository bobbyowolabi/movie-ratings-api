package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

public interface TitleStore extends DatasetStore {

    /**
     * Adds the given title attributes to this Store.
     * 
     * @param tconst
     * @param titleType
     * @param primaryTitle 
     * @throws java.io.IOException 
     */
    public void addTitle(String tconst, String titleType, String primaryTitle) throws IOException;

    /**
     * Queries this store and returns the title associated with the given title
     * identifier.
     * 
     * @param tconst
     * @return 
     * @throws java.io.IOException 
     */
    public TitleRecord tconst(String tconst) throws IOException;
    
    /**
     * Queries this store and returns the title associated with the given primary
     * title.
     * 
     * @param primaryTitle
     * @return 
     * @throws java.io.IOException 
     */
    public TitleRecord title(String primaryTitle) throws IOException;
}
