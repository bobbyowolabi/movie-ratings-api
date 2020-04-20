package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.api.domain.MovieRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;

/**
 *
 */
public interface MovieStore extends DatasetStore {

    public void addEpisode(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException;    
    
    public void addName(String nconst, String primaryName) throws IOException;
    
    public void addPrincipal(String tconst, String nconst, String ordering) throws IOException;
    
    public void addRating(String tconst, String averageRating) throws IOException;
    
    public void addTitle(String tconst, String titleType, String primaryTitle) throws IOException;
    
    public MovieRecord title(String tile) throws IOException;
}
