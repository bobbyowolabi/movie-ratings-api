package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.RatingRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface RatingStore extends DatasetStore {

    public void addRating(String tconst, final String averageRating) throws IOException;
    
    public RatingRecord rating(String tconst) throws IOException;
    
    public List<RatingRecord> ratings(List<String> tconst) throws IOException;
}
