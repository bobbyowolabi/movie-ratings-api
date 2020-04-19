package com.owodigi.movie.ratings.store.domain;

import com.owodigi.movie.ratings.api.domain.RatingRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

/**
 *
 */
public interface RatingStore extends DatasetStore {

    public void addNconst(String tconst, String nconst) throws IOException;
    
    public void addTitle(String tconst, String titleType, String primaryTitle) throws IOException;
    
    public RatingRecord title(String tile) throws IOException;
    
    public void updateEpisode(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException;

    public void updateName(String nconst, String primaryName) throws IOException;
    
    public void updateRating(String tconst, String averageRating) throws IOException;
}
