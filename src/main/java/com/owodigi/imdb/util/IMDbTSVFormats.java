package com.owodigi.imdb.util;

/**
 * Utilities to parse the datasets found at: https://www.imdb.com/interfaces/
 */
public class IMDbTSVFormats {  
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.episode.tsv.gz
     */
    public static class TitleEpisodeFormat extends TSVFormat {
        /**
         * Headers for dataset that contains the tv episode information
         */
        public enum header {
            /**
             * (string) - alphanumeric identifier of episode
             */
            tconst,
            /**
             * (string) - alphanumeric identifier of the parent TV Series
             */
            parentTconst,
            /**
             * (integer) – season number the episode belongs to
             */
            seasonNumber,
            /**
             * (integer) – episode number of the tconst in the TV series
             */
            episodeNumber
        }

        @Override
        public Class<? extends Enum<?>> headerClass() {
            return header.class;
        }
    }
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/name.basics.tsv.gz
     */
    public static class NameBasicFormat extends TSVFormat {

        public enum headers {
            /**
             * (string) - alphanumeric unique identifier of the name/person
             */
            nconst,
            /**
             * (string)– name by which the person is most often credited
             */
            primaryName,
            /**
             * in YYYY format
             */
            birthYear,
            /**
             * in YYYY format if applicable, else '\N'
             */
            deathYear,
            /**
             * (array of strings)– the top-3 professions of the person
             */
            primaryProfession,
            /**
             * (array of tconsts) – titles the person is known for
             */
            knownForTitles
        }
        
        @Override
        public Class<? extends Enum<?>> headerClass() {
            return headers.class;
        }        
    }
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.principals.tsv.gz
     */    
    public static class TitlePrincipalsFormat extends TSVFormat {

        /**
         * Headers of the dataset that contains the principal cast/crew for
         * titles
         */
        public enum headers {
            /**
             * (string) - alphanumeric unique identifier of the title
             */
            tconst,
            /**
             * (integer) – a number to uniquely identify rows for a given
             * titleId
             */
            ordering,
            /**
             * alphanumeric unique identifier of the name/person
             */
            nconst,
            /**
             * (string) - the category of job that person was in
             */
            category,
            /**
             * (string) - the specific job title if applicable, else '\N'
             */
            job,
            /**
             * (string) - the name of the character played if applicable, else
             * '\N'
             */
            characters
        }
        
        @Override
        public Class<? extends Enum<?>> headerClass() {
            return headers.class;
        }        
    }
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.ratings.tsv.gz
     */    
    public static class TitleRatingsFormat extends TSVFormat {
        /**
         * Headers of the dataset that contains the IMDb rating and votes
         * information for titles
         */
        public enum headers {
            /**
             * (string) - alphanumeric unique identifier of the title
             */
            tconst,
            /**
             * weighted average of all the individual user ratings
             */
            averageRating,
            /**
             * number of votes the title has received
             */
            numVotes
        }
        
        @Override
        public Class<? extends Enum<?>> headerClass() {
            return headers.class;
        }
    }

    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.basics.tsv.gz
     */    
    public static class TitleBasicsFormat extends TSVFormat {
        public enum header {
            /**
             * (string) - alphanumeric unique identifier of the title
             */
            tconst,
            /**
             * (string) – the type/format of the title (e.g. movie, short,
             * tvseries, tvepisode, video, etc)
             */
            titleType,
            /**
             * (string) – the more popular title / the title used by the
             * filmmakers on promotional materials at the point of release
             */
            primaryTitle,
            /**
             * (string) - original title, in the original language
             */
            originalTitle,
            /**
             * (boolean) - 0: non-adult title; 1: adult title
             */
            isAdult,
            /**
             * (YYYY) – represents the release year of a title. In the case of
             * TV Series, it is the series start year
             */
            startYear,
            /**
             * (YYYY) – TV Series end year. ‘\N’ for all other title types
             */
            endYear,
            /**
             * primary runtime of the title, in minutes
             */
            runtimeMinutes,
            /**
             * (string array) – includes up to three genres associated with the
             * title
             */
            genres
        }

        @Override
        public Class<? extends Enum<?>> headerClass() {
            return header.class;
        }
    }
}