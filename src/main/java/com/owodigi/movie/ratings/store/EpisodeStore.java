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
     * @param parentTconst
     * @param seasonNumber
     * @param episodeNumber
     * @throws IOException 
     */
    public void add(String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException;
 
    /**
     * 
     * @param tconst
     * @return 
     * @throws IOException 
     */
    public EpisodeRecord tconst(String tconst) throws IOException;    
    
    public List<EpisodeRecord> parentTconst(String parentTconst) throws IOException;
}
