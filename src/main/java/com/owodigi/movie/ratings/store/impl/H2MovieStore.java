package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.PrincipalStore;
import com.owodigi.movie.ratings.api.domain.Episode;
import com.owodigi.movie.ratings.api.domain.MovieRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.movie.ratings.store.TitleStore;
import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.impl.util.H2Connection;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.owodigi.movie.ratings.store.MovieStore;
import com.owodigi.movie.ratings.store.MovieStoreUpdater;
import com.owodigi.movie.ratings.store.RatingStore;
import com.owodigi.movie.ratings.store.domain.PrincipalRecord;
import com.owodigi.movie.ratings.store.domain.RatingRecord;
import java.util.stream.Collectors;

/**
 *
 */
public class H2MovieStore implements MovieStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2MovieStore.class);
    private final TitleStore titleStore;
    private final RatingStore ratingStore;
    private final PrincipalStore principalStore;
    private final EpisodeStore episodeStore;
    private final NameStore nameStore;

    public H2MovieStore(final String username, final String password, final Path path) throws IOException {
        final Connection connection = H2Connection.instance(username, password, path.toString());
        LOGGER.info("Creating TitleStore");
        this.titleStore = new TitleTable(connection);
        LOGGER.info("Creating RatingStore");
        this.ratingStore = new RatingTable(connection);
        LOGGER.info("Creating Principal");
        this.principalStore = new PrincipalTable(connection);        
        LOGGER.info("Creating EpisodeStore");
        this.episodeStore = new EpisodeTable(connection);
        LOGGER.info("Creating NameRecordStore");
        this.nameStore = new NameTable(connection);
    }
    
    @Override
    public void clear() throws IOException {
        titleStore.clear();
        episodeStore.clear();
        nameStore.clear();
        principalStore.clear();
        ratingStore.clear();
    }    
    
    @Override
    public void close() throws Exception {
        titleStore.close();
        episodeStore.close();
        nameStore.close();
        principalStore.close();
        ratingStore.close();        
    }    

    @Override
    public MovieRecord title(final String tile) throws IOException {
        final TitleRecord titleRecord = titleStore.title(tile);
        return titleRecord == null ? null : toRatingRecord(titleRecord);
    }
    
    
    @Override
    public void update(MovieStoreUpdateCallback callback) throws IOException {
        callback.update(updater);
    }
    
    private String calculateAverageRating(final List<RatingRecord> ratings) {
        return ratings
            .stream()
            .map(rating -> rating.averageRating())
            .filter(averageRating -> averageRating != null)
            .map(averageRating -> Double.parseDouble(averageRating))
            .collect(Collectors.averagingDouble(d -> d))
            .toString();
    }
    
    private List<RatingRecord> ratings(final List<EpisodeRecord> episodes) throws IOException {
        return ratingStore.ratings(
                episodes
                    .stream()
                    .map(episode -> episode.tconst())
                    .collect(Collectors.toList())
        );
    }
    
    private Episode toEpisode(final EpisodeRecord episodeRecord) throws IOException {
        final Episode episode = new Episode();
        final TitleRecord episodeTitleRecord = titleStore.tconst(episodeRecord.tconst());
        final String episodeTitle = episodeTitleRecord == null ? null : episodeTitleRecord.primaryTitle();
        episode.setTitle(episodeTitle); 
        episode.setUserRating(userRating(episodeRecord.tconst()));
        final List<PrincipalRecord> principalRecords = principalStore.tconst(episodeRecord.tconst());
        final List<String> nconstList = 
            principalRecords
                .stream()
                .map(record -> record.getNconst())
                .collect(Collectors.toList());
        episode.setCastList(String.join(", ", nameStore.names(nconstList)));
        episode.setEpisodeNumber(episodeRecord.episodeNumber());
        episode.setSeasonNumber(episodeRecord.seasonNumber());
        return episode;
    }
    
    private String userRating(final String tconst) throws IOException {
        final RatingRecord record = ratingStore.rating(tconst);
        return record == null ? null : record.averageRating();
    }
    
    private MovieRecord toRatingRecord(final TitleRecord titleRecord) throws IOException {
        final MovieRecord ratingRecord = new MovieRecord();
        ratingRecord.setTitle(titleRecord.primaryTitle());
        final List<EpisodeRecord> episodes = episodeStore.parentTconst(titleRecord.tconst());
        if (episodes.isEmpty() == false) {
            ratingRecord.setCalculatedRating(calculateAverageRating(ratings(episodes)));
        }
        final List<PrincipalRecord> principalRecords = principalStore.tconst(titleRecord.tconst());
        final List<String> titleNconsts = principalRecords.stream().map(record -> record.getNconst()).collect(Collectors.toList());
        ratingRecord.setCastList(String.join(", ", nameStore.names(titleNconsts)));
        final Episode[] episodeArray = new Episode[episodes.size()];
        for (int i = 0; i < episodes.size(); ++i) {
            final EpisodeRecord episodeRecord = episodes.get(i);
            episodeArray[i] = toEpisode(episodeRecord);
        }
        ratingRecord.setEpisodes(episodeArray);
        ratingRecord.setType(titleRecord.titleType());
        ratingRecord.setUserRating(userRating(titleRecord.tconst()));
        return ratingRecord;
    }
    
    private final MovieStoreUpdater updater = new MovieStoreUpdater() {

        @Override
        public void addEpisode(final String tconst, String parentTconst, String seasonNumber, String episodeNumber) throws IOException {
            episodeStore.add(tconst, parentTconst, seasonNumber, episodeNumber);
        }

        @Override
        public void addName(String nconst, String primaryName) throws IOException {
            nameStore.add(nconst, primaryName);
        }

        @Override
        public void addPrincipal(final String tconst, final String nconst, final String ordering) throws IOException {
            principalStore.add(tconst, nconst, ordering);
        }

        @Override
        public void addRating(final String tconst, final String averageRating) throws IOException {
            ratingStore.addRating(tconst, averageRating);
        }

        @Override
        public void addTitle(final String tconst, final String titleType, final String primaryTitle) throws IOException {
            titleStore.addTitle(tconst, titleType, primaryTitle);
        }
    };    
}
