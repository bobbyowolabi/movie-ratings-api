package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.PrincipalRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

public interface PrincipalStore extends DatasetStore {

    /**
     * Adds the given name attributes to this Store.
     * 
     * @param tconst
     * @param nconst
     * @param ordering
     * @throws IOException 
     */
    public void add(String tconst, String nconst, String ordering) throws IOException;
    
    /**
     * Queries this store and returns the principals associated with the given title
     * identifier.
     * 
     * @param tconst
     * @return
     * @throws IOException 
     */
    public List<PrincipalRecord> tconst(String tconst) throws IOException;    
}
