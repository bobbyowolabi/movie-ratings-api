package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.util.MovieStoreUpdateCallback;
import com.owodigi.movie.ratings.api.domain.MovieRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

public interface MovieStore extends DatasetStore {

    /**
     * Queries this MovieStore and returns the result associated with the given
     * title identifier.
     * 
     * @param tile
     * @return
     * @throws IOException 
     */
    public MovieRecord title(String tile) throws IOException;
    
    /**
     * Provides clients with a mechanism to update this MovieStore.
     * 
     * @param callback
     * @throws IOException 
     */
    public void update(MovieStoreUpdateCallback callback) throws IOException;
}
