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
import com.owodigi.util.IMDbDatasetDownloader;
import com.owodigi.util.IMDbDownloaderCallback;
import com.owodigi.util.IMDbTSVFormats;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.util.IMDbTSVFormats.TitlePrincipalsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
//@SpringBootApplication
//@RestController
public class MovieRatingsAPI {
    private static ConfigurableApplicationContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingsAPI.class);

    public static void main(final String[] args) throws IOException {
        LOGGER.info("Starting REST service in MovieRatingsAPI");
//        context = SpringApplication.run(MovieRatingsAPI.class, args);
        LOGGER.info("Creating Title Store");
        final TitleStore titleStore = new H2TitleStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        titleStore.clear();
        LOGGER.info("Creating EpisodeStore Store");
        final EpisodeStore episodeStore = new H2EpisodeStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        episodeStore.clear();
        LOGGER.info("Creating NameStore Store");
        final NameStore nameStore = new H2NameStore(RatingsAppProperties.databaseUserName(), RatingsAppProperties.databaseUserPassword(), RatingsAppProperties.databasePath());
        nameStore.clear();
        processTitleBasicsDataset(titleStore, episodeStore);
        processTitleRatingsDataset(titleStore, episodeStore);
        processTitlePrincipalsDataset(titleStore, episodeStore, nameStore);
        processTitleEpisodeDataset(episodeStore);
        processNameBasicsDataset(nameStore);
    }

    public static void stop() {
        if (context != null) {
            context.close();
        }
    }
    
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    private static void processTitleBasicsDataset(final TitleStore titleStore, final EpisodeStore episodeStore) throws IOException {
        LOGGER.info("processTitleBasicsDataset: " + RatingsAppProperties.titleBasicsURL());
        IMDbDatasetDownloader.read(RatingsAppProperties.titleBasicsURL(), new TitleBasicsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String year = record.get(TitleBasicsFormat.header.startYear);
                if (RatingsAppProperties.titleIncludeYears().contains(year)) {
                    final String tconst = record.get(TitleBasicsFormat.header.tconst);
                    final String titleType = record.get(TitleBasicsFormat.header.titleType);
                    final String primaryTitle = record.get(TitleBasicsFormat.header.primaryTitle);
                    if (titleType.equals("tvEpisode")) {
                        episodeStore.addTitle(tconst, primaryTitle);
                    } else {
                        titleStore.addTitle(tconst, titleType, primaryTitle);
                    }
                }
            }
        });
    }

    private static void processTitleRatingsDataset(final TitleStore titleStore, final EpisodeStore episodeStore) throws IOException {
        LOGGER.info("processTitleRatingsDataset: " + RatingsAppProperties.titleRatingsURL());
        IMDbDatasetDownloader.read(RatingsAppProperties.titleRatingsURL(), new TitleRatingsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitleRatingsFormat.headers.tconst);
                final String averageRating = record.get(TitleRatingsFormat.headers.averageRating);
                final TitleRecord titleRecord = titleStore.tconst(tconst);
                if (titleRecord == null) {
                    final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                    if (episodeRecord == null) {
                        throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore: " + record);
                    }
                    episodeStore.updateRating(tconst, averageRating);
                } else {
                    titleStore.updateRating(tconst, averageRating);
                }
            }
        });
    }

    private static void processTitlePrincipalsDataset(final TitleStore titleStore, final EpisodeStore episodeStore, final NameStore nameStore) throws IOException {
        LOGGER.info("processTitlePrincipalsDataset: " + RatingsAppProperties.titlePrincipalsURL());
        IMDbDatasetDownloader.read(RatingsAppProperties.titlePrincipalsURL(), new TitlePrincipalsFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitlePrincipalsFormat.headers.tconst);
                final String nconst = record.get(TitlePrincipalsFormat.headers.nconst);
                final TitleRecord titleRecord = titleStore.tconst(tconst);
                if (titleRecord == null) {
                    final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                    if (episodeRecord == null) {
                        throw new IllegalStateException("Encountered record not found in both the TitleStore and EpisodeStore: " + record);
                    }
                    episodeStore.addNconst(tconst, nconst);
                    nameStore.addNconst(nconst);
                } else {
                    titleStore.addNconst(tconst, nconst);
                    nameStore.addNconst(nconst);
                }
            }
        });
    }

    private static void processTitleEpisodeDataset(final EpisodeStore episodeStore) throws IOException {
        LOGGER.info("processTitleEpisodeDataset: " + RatingsAppProperties.titleEpisodeURL());
        IMDbDatasetDownloader.read(RatingsAppProperties.titleEpisodeURL(), new TitleEpisodeFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String tconst = record.get(TitleEpisodeFormat.header.tconst);
                final EpisodeRecord episodeRecord = episodeStore.tconst(tconst);
                if (episodeRecord == null) {
                    throw new IllegalStateException("Encountered a record not present in EpisdoeStore: " + record);
                }
                final String parentTconst = record.get(TitleEpisodeFormat.header.parentTconst);
                final String episodeNumber = record.get(TitleEpisodeFormat.header.episodeNumber);
                final String seasonNumber = record.get(TitleEpisodeFormat.header.seasonNumber);
                episodeStore.updateEpisode(tconst, parentTconst, seasonNumber, episodeNumber);
            }
        });
    }

    private static void processNameBasicsDataset(final NameStore nameStore) throws IOException {
        LOGGER.info("processNameBasicsDataset: " + RatingsAppProperties.nameBasicsURL());
        IMDbDatasetDownloader.read(RatingsAppProperties.nameBasicsURL(), new IMDbTSVFormats.NameBasicFormat(), new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord record) throws IOException {
                final String nconst = record.get(IMDbTSVFormats.NameBasicFormat.headers.nconst);
                final String primaryName = record.get(IMDbTSVFormats.NameBasicFormat.headers.primaryName);
                final NameRecord nameRecord = nameStore.nconst(nconst);
                if (nameRecord != null) {
                    nameStore.updateName(nconst, primaryName);
                }
            }
        });
    }
}
