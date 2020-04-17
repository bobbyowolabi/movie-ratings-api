package com.owodigi.movie.ratings.store;

import com.owodigi.movie.ratings.api.domain.RatingRecord;
import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.impl.DatasetStore;
import com.owodigi.movie.ratings.store.impl.H2EpisodeStore;
import com.owodigi.movie.ratings.store.impl.H2NameStore;
import com.owodigi.movie.ratings.store.impl.H2TitleStore;
import com.owodigi.movie.ratings.util.MovieRatingsAppProperties;
import java.io.IOException;

/**
 *
 */
public class RatingStore implements DatasetStore {
    private final TitleStore titleStore;
    private final EpisodeStore episodeStore;
    private final NameStore nameStore;

    public RatingStore() throws IOException {
        this.titleStore = new H2TitleStore(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath());
        this.episodeStore = new H2EpisodeStore(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath());
        this.nameStore = new H2NameStore(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath());
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
    
    @Override
    public void clear() throws IOException {
        titleStore.clear();
        episodeStore.clear();
        nameStore.clear();
    }    
    
    public RatingRecord title(final String tile) throws IOException {
        final TitleRecord titleRecord = titleStore.title(tile);
        return titleRecord == null ? null : toRatingRecord(titleRecord);
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
    
    private RatingRecord toRatingRecord(final TitleRecord titleRecord) throws IOException {
        final RatingRecord ratingRecord = new RatingRecord();
        ratingRecord.setTitle(titleRecord.primaryTitle());
//        record.setCalculatedRating();
        ratingRecord.setCastList(String.join(", ", nameStore.names(titleRecord.nconstList())));
//        record.setEpisodes(episodes);
        ratingRecord.setType(titleRecord.titleType());
        ratingRecord.setUserRating(titleRecord.averageRating());
        return ratingRecord;
    }
}
