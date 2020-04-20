package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface NameStore extends DatasetStore {

    /**
     * 
     * @param nconst
     * @param primaryName
     * @throws IOException 
     */
    public void add(String nconst, String primaryName) throws IOException;
    
    public List<String> names(final List<String> nconsts) throws IOException;
    
    /**
     * 
     * @param nconst
     * @return 
     * @throws java.io.IOException 
     */
    public NameRecord nconst(final String nconst) throws IOException;
}
