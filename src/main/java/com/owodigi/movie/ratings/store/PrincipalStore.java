package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.PrincipalRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface PrincipalStore extends DatasetStore {

    public void add(String tconst, String nconst, String ordering) throws IOException;
    
    public List<PrincipalRecord> tconst(String tconst) throws IOException;    
}
