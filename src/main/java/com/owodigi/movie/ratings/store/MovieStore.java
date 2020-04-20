package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.api.domain.MovieRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import com.owodigi.movie.ratings.store.impl.MovieStoreUpdateCallback;
import java.io.IOException;

/**
 *
 */
public interface MovieStore extends DatasetStore {
    
    public MovieRecord title(String tile) throws IOException;
    
    public void update(MovieStoreUpdateCallback callback) throws IOException;
}
