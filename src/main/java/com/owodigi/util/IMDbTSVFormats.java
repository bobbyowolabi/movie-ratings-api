package com.owodigi.util;



/**
 * Utilities to parse the datasets found at: https://www.imdb.com/interfaces/
 */
public class IMDbTSVFormats {  
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.akas.tsv.gz
     */
//    public static final TSVFormat<TITLE_AKAS_HEADERS> TITLE_AKAS_FORMAT = new TSVFormat<>();
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.basics.tsv.gz
     */
//    public static final TSVFormat<TITLE_BASICS_HEADERS> TITLE_BASICS_FORMAT = new TSVFormat<>();
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.episode.tsv.gz
     */
//    public static final TSVFormat<TITLE_EPISODE_HEADERS> TITLE_EPISODE_FORMAT = new TSVFormat<>();
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.ratings.tsv.gz
     */
//    public static final TSVFormat<TITLE_RATINGS_HEADERS> TITLE_RATINGS_FORMAT = new TSVFormat<>();

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
        
        
    
//    public static final TSVFormat NAME_BASICS_FORMAT = new TSVFormat<>(NAME_BASICS_HEADERS);
    
    /**
     * Utility to parse dataset found at https://datasets.imdbws.com/title.principals.tsv.gz
     */
//    public static final TSVFormat<TITLE_PRINCIPALS_HEADERS> TITLE_PRINCIPALS_FORMAT = new TSVFormat<>();    
    

}