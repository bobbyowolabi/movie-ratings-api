package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.RatingRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

public interface RatingStore extends DatasetStore {

    /**
     * Adds the given rating to this Store.
     * @param tconst
     * @param averageRating
     * @throws IOException 
     */
    public void addRating(String tconst, final String averageRating) throws IOException;
    
    /**
     * Queries this store and returns the rating associated with the given title
     * identifier.
     * 
     * @param tconst
     * @return
     * @throws IOException 
     */
    public RatingRecord rating(String tconst) throws IOException;
    
    /**
     * Queries this store and returns the ratings associated with the given collection of
     * identifiers.
     * 
     * @param tconst
     * @return
     * @throws IOException 
     */
    public List<RatingRecord> ratings(List<String> tconst) throws IOException;
}
