package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import java.io.IOException;
import java.util.List;

public interface EpisodeStore extends DatasetStore {

    /**
     * Adds the given episode attributes to this Store.
     * 
     * @param tconst
     * @param parentTconst
     * @param seasonNumber
     * @param episodeNumber
     * @throws IOException 
     */
    public void add(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException;
 
    /**
     * Queries this Store for the given name identifier and returns the corresponding
     * record.
     * 
     * @param tconst
     * @return 
     * @throws IOException 
     */
    public EpisodeRecord tconst(String tconst) throws IOException;    
    
    /**
     * Queries this Store for all the given name parent title identifier and 
     * returns a corresponding list of all associated episodes.
     * 
     * @param parentTconst
     * @return
     * @throws IOException 
     */
    public List<EpisodeRecord> parentTconst(String parentTconst) throws IOException;
}
