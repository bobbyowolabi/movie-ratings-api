package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.api.domain.Episode;
import com.owodigi.movie.ratings.api.domain.RatingRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.movie.ratings.store.TitleStore;
import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.domain.RatingStore;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.impl.util.H2Connection;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class H2RatingStore implements RatingStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2RatingStore.class);
    private final TitleStore titleStore;
    private final EpisodeStore episodeStore;
    private final NameStore nameStore;

    public H2RatingStore(final String username, final String password, final Path path) throws IOException {
        final Connection connection = H2Connection.instance(username, password, path.toString());
        LOGGER.info("Creating TitleStore");
        this.titleStore = new TitleTable(connection);
        LOGGER.info("Creating EpisodeStore");
        this.episodeStore = new EpisodeTable(connection);
        LOGGER.info("Creating NameRecordStore");
        this.nameStore = new NameTable(connection);
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public void close() throws Exception {
        titleStore.close();
        episodeStore.close();
        nameStore.close();
    }    

    @Override
    public RatingRecord title(final String tile) throws IOException {
        final TitleRecord titleRecord = titleStore.title(tile);
        return titleRecord == null ? null : toRatingRecord(titleRecord);
    }
    
    @Override
    public void updateEpisode(final String tconst, final String parentTconst, final String seasonNumber, final String episodeNumber) throws IOException {
        final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
        if (episodeRecord == null) {
            throw new IllegalStateException("Encountered a record not present in EpisdoeStore; tconst: " + tconst);
        }
        episodeStore.updateEpisode(tconst, parentTconst, seasonNumber, episodeNumber);        
    }
    
    @Override
    public void updateName(final String nconst, final String primaryName) throws IOException {
        final NameRecord nameRecord = nameStore.nconst(nconst);
        if (nameRecord != null) {
            nameStore.updateName(nconst, primaryName);
        }
    }
    
    @Override
    public void updateRating(final String tconst, final String averageRating) throws IOException {
        final TitleRecord titleRecord = titleStore.tconst(tconst);
        if (titleRecord == null) {
            final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
            if (episodeRecord == null) {
                LOGGER.debug("Encountered record not found in both the TitleStore and EpisodeStore; tconst: " + tconst);
            }
            episodeStore.updateRating(tconst, averageRating);
        } else {
            titleStore.updateRating(tconst, averageRating);
        }
    }
    
    private String calculateAverageRating(final List<EpisodeRecord> episodes) {
        return episodes
            .stream()
            .map(episode -> episode.averageRating())
            .filter(averageRating -> averageRating != null)
            .map(averageRating -> Double.parseDouble(averageRating))
            .collect(Collectors.averagingDouble(d -> d))
            .toString();
    }
    
    private RatingRecord toRatingRecord(final TitleRecord titleRecord) throws IOException {
        final RatingRecord ratingRecord = new RatingRecord();
        ratingRecord.setTitle(titleRecord.primaryTitle());
        final List<EpisodeRecord> episodes = episodeStore.parentTconst(titleRecord.tconst());
        if (episodes.isEmpty() == false) {
            ratingRecord.setCalculatedRating(calculateAverageRating(episodes));
        }
        ratingRecord.setCastList(String.join(", ", nameStore.names(titleRecord.nconstList())));
        final Episode[] episodeArray = new Episode[episodes.size()];
        for (int i = 0; i < episodes.size(); ++i) {
            final EpisodeRecord episodeRecord = episodes.get(i);
            final Episode episode = new Episode();
            episode.setUserRating(episodeRecord.averageRating());
            episode.setCastList(String.join(", ", nameStore.names(episodeRecord.nconstList())));
            episode.setEpisodeNumber(episodeRecord.episodeNumber());
            episode.setSeasonNumber(episodeRecord.seasonNumber());
            episode.setTitle(episodeRecord.primaryTitle()); 
            episodeArray[i] = episode;
        }
        ratingRecord.setEpisodes(episodeArray);
        ratingRecord.setType(titleRecord.titleType());
        ratingRecord.setUserRating(titleRecord.averageRating());
        return ratingRecord;
    }
}
