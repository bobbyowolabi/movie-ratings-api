package com.owodigi.util;

/**
 * The Headers of the TSV datasets found on https://www.imdb.com/interfaces/.
 */
public class IMDbTSVHeaders {
    
    public enum TITLE_AKAS_HEADERS {
        /**
         * (string) - a tconst, an alphanumeric unique identifier of the title
         */
        titleId, 
        /**
         * (integer) – a number to uniquely identify rows for a given titleId
         */
        ordering,
        /**
         * (string) – the localized title
         */
        title, 
        /**
         * (string) - the region for this version of the title
         */
        region, 
        /**
         * (string) - the language of the title
         */
        language,
        /**
         * (array) - Enumerated set of attributes for this alternative title. One or more of the following: "alternative", "dvd", "festival", "tv", "video", "working", "original", "imdbDisplay". New values may be added in the future without warning
         */
        types, 
        /**
         * (array) - Additional terms to describe this alternative title, not enumerated
         */
        attributes, 
        /**
         * (boolean) – 0: not original title; 1: original title
         */
        isOriginalTitle
    }
    
    public enum TITLE_BASICS_HEADERS {
        /**
         * (string) - alphanumeric unique identifier of the title
         */
        tconst, 
        /** 
         * (string) – the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)
         */
        titleType, 
        /**
         * (string) – the more popular title / the title used by the filmmakers on promotional materials at the point of release
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
         * (YYYY) – represents the release year of a title. In the case of TV Series, it is the series start year
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
         * (string array) – includes up to three genres associated with the title
         */
        genres
    }
    
    /**
     * Headers for dataset that contains the tv episode information
     */
    public enum TITLE_EPISODE_HEADERS {
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
    
    /**
     * Headers of the dataset that contains the principal cast/crew for titles
     */
    public enum TITLE_PRINCIPALS_HEADERS {
        /**
         * (string) - alphanumeric unique identifier of the title
         */
        tconst, 
        /**
         * (integer) – a number to uniquely identify rows for a given titleId
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
         * (string) - the name of the character played if applicable, else '\N'
         */
        characters
    }
    
    /**
     * Headers of the dataset that contains the IMDb rating and votes information for titles
     */
    public enum TITLE_RATINGS_HEADERS {
        /**
         * (string) - alphanumeric unique identifier of the title
         */
        tconst,
        /**
         *  weighted average of all the individual user ratings
         */
        averageRating,
        /**
         * number of votes the title has received        
         */
        numVotes
    }
    
    public static enum NAME_BASICS_HEADERS {
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
}
