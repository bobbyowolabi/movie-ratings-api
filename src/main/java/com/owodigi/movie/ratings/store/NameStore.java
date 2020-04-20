package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

public interface NameStore extends DatasetStore {

    /**
     * Adds the given name attributes to this Store.
     * 
     * @param nconst
     * @param primaryName
     * @throws IOException 
     */
    public void add(String nconst, String primaryName) throws IOException;
    
    /**
     * Queries this Store for all the given name identifiers and returns a 
     * corresponding list of resolved primary names.
     * 
     * @param nconsts
     * @return
     * @throws IOException 
     */
    public List<String> names(final List<String> nconsts) throws IOException;
    
    /**
     * Queries this Store for the given name identifier and returns the corresponding
     * record.
     * 
     * @param nconst
     * @return 
     * @throws java.io.IOException 
     */
    public NameRecord nconst(final String nconst) throws IOException;
}
