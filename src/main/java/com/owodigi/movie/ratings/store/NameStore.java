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
     * @throws java.io.IOException 
     */
    public void addNconst(final String nconst) throws IOException;
    
    /**
     * 
     * @param nconsts
     * @return
     * @throws IOException 
     */
    public List<String> names(final List<String> nconsts) throws IOException;
    
    /**
     * 
     * @param nconst
     * @return 
     * @throws java.io.IOException 
     */
    public NameRecord nconst(final String nconst) throws IOException;
    
    /**
     * 
     * @param nconst
     * @param primaryName 
     * @throws java.io.IOException 
     */
    public void updateName(final String nconst, final String primaryName) throws IOException;
}
