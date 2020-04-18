package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface EpisodeStore extends DatasetStore {

    /**
     * 
     * @param tconst
     * @param primaryTitle 
     * @throws java.io.IOException 
     */
    public void addTitle(String tconst, String primaryTitle) throws IOException;
 
    /**
     * 
     * @param tconst
     * @param nconst 
     * @throws java.io.IOException 
     */
    public void addNconst(String tconst, String nconst) throws IOException;    
    
    /**
     * 
     * @param tconst
     * @return 
     * @throws java.io.IOException 
     */
    public EpisodeRecord tconst(String tconst) throws IOException;    
    
    
    public List<EpisodeRecord> parentTconst(String parentTconst) throws IOException;
    
    /**
     * 
     * @param primaryTitle
     * @return 
     * @throws java.io.IOException 
     */
    public EpisodeRecord title(String primaryTitle) throws IOException;
    
    /**
     * 
     * @param tconst
     * @param averageRating 
     * @throws java.io.IOException 
     */
    public void updateRating(String tconst, String averageRating) throws IOException;    

    /**
     * 
     * @param tconst
     * @param parentTconst
     * @param seasonNumber
     * @param episodeNumber 
     */
    public void updateEpisode(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException;
}
