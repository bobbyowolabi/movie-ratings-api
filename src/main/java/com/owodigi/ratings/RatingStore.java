package com.owodigi.ratings;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.domain.NameRecord;
import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.NameStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2EpisodeStore;
import com.owodigi.ratings.store.impl.H2NameStore;
import com.owodigi.ratings.store.impl.H2TitleStore;
import com.owodigi.ratings.util.RatingsAppProperties;
import com.owodigi.util.IMDbTSVFormats;
import java.io.IOException;

/**
 *
 */
public class RatingStore {
    private final TitleStore titleStore;
    private final EpisodeStore episodeStore;
    private final NameStore nameStore;

    public RatingStore() throws IOException {
        this.titleStore = new H2TitleStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        this.episodeStore = new H2EpisodeStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        this.nameStore = new H2NameStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
    }
    
    public void addNconst(final String tconst, final String nconst) throws IOException {
        final TitleRecord titleRecord = titleStore.tconst(tconst);
        if (titleRecord == null) {
            final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
            if (episodeRecord == null) {
                throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore; tconst: " + tconst);
            }
            episodeStore.addNconst(tconst, nconst);
            nameStore.addNconst(nconst);
        } else {
            titleStore.addNconst(tconst, nconst);
            nameStore.addNconst(nconst);
        }
        
    }
    
    public void addTitle(final String tconst, final String titleType, final String primaryTitle) throws IOException {
        if (titleType.equals("tvEpisode")) {
            episodeStore.addTitle(tconst, primaryTitle);
        } else {
            titleStore.addTitle(tconst, titleType, primaryTitle);
        }
        
    }
    
    public void clear() throws IOException {
        titleStore.clear();
        episodeStore.clear();
        nameStore.clear();
    }    
    
    public void updateEpisode(final String tconst, final String parentTconst, final String seasonNumber, final String episodeNumber) throws IOException {
        final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
        if (episodeRecord == null) {
            throw new IllegalStateException("Encountered a record not present in EpisdoeStore; tconst: " + tconst);
        }
        episodeStore.updateEpisode(tconst, parentTconst, seasonNumber, episodeNumber);        
    }
    
    public void updateName(final String nconst, final String primaryName) throws IOException {
        final NameRecord nameRecord = nameStore.nconst(nconst);
        if (nameRecord != null) {
            nameStore.updateName(nconst, primaryName);
        }
    }
    
    public void updateRating(final String tconst, final String averageRating) throws IOException {
        final TitleRecord titleRecord = titleStore.tconst(tconst);
        if (titleRecord == null) {
            final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
            if (episodeRecord == null) {
                throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore; tconst: " + tconst);
            }
            episodeStore.updateRating(tconst, averageRating);
        } else {
            titleStore.updateRating(tconst, averageRating);
        }
    }
    


}
