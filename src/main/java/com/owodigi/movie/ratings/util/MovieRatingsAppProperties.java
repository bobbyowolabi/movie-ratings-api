package com.owodigi.movie.ratings.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class MovieRatingsAppProperties {
    public static final String SYSTEM_PROPERTIES_FILE = "ratings.properties.file";
    private static final String SYSTEM_PROPERTIES_PATH;
    public static final String DATABASE_PATH = "ratings.db.path";
    private static final String DATABASE_USERNAME = "ratings.db.username";
    private static final String DATABASE_PASSWORD = "ratings.db.password";
    public static final String IMDB_TITLE_BASICS_URL = "imdb.title.basics.url";
    public static final String IMBD_TITLE_RATINGS_URL = "imdb.title.ratings.url";
    public static final String IMBD_TITLE_PRINCIPALS_URL = "imdb.title.principals.url";
    public static final String IMBD_TITLE_EPISODE_URL = "imdb.title.episode.url";
    public static final String IMBD_NAME_BASICS_URL = "imdb.name.basics.url";
    private static final String TITLE_INCLUDE_YEARS = "title.include.years";
    private static final Properties PROPERTIES = new Properties();
    
    static {
        SYSTEM_PROPERTIES_PATH = loadProperties();
    }

    /**
     * The path of the database file.  An appropriate extension will be given
     * to the file.
     * 
     * @return path of the database file
     */
    public static Path databasePath() {
       return Paths.get(requiredProperty(DATABASE_PATH));
    }
    
    /**
     * Database username used to log into the database
     * 
     * @return Database username
     */
    public static String databaseUserName() {
        return requiredProperty(DATABASE_USERNAME);
    }
    
    /**
     * Database password used to log into the database
     * 
     * @return Database password
     */
    public static String databaseUserPassword() {
        return requiredProperty(DATABASE_PASSWORD);
    }
    
    /**
     * Loads the properties from the Properties file specified in {@link MovieRatingsAppProperties#SYSTEM_PROPERTIES_FILE}
     * 
     * @return path to properties file
     */
    private static String loadProperties() {
        final String systemPropertiesPath = System.getProperty(SYSTEM_PROPERTIES_FILE);
        if (systemPropertiesPath == null) {
            throw new IllegalArgumentException("System proptery " + SYSTEM_PROPERTIES_FILE + " is not set");
        }
        try {
            PROPERTIES.load(Files.newInputStream(Paths.get(systemPropertiesPath)));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load system properties file " + systemPropertiesPath + " due to " + ex.getMessage(), ex);
        }
        return systemPropertiesPath;
    }
    
    /**
     * Searches for the property with the specified key in this property list. 
     * If the key is not found in this property list, an IllegalArgumentException
     * is thrown
     * 
     * @param property the property key
     * @return value of the property
     */
    private static String requiredProperty(final String property) {
        final String value = PROPERTIES.getProperty(property);
        if (value == null) {
            throw new IllegalArgumentException(property + " is a required property; File: " + SYSTEM_PROPERTIES_PATH);
        }
        return value;
    }
    
    /**
     * The URL of the Title Basics dataset files can be accessed and downloaded 
     * from https://datasets.imdbws.com/.
     * 
     * @return
     * @throws IOException 
     */
    public static URL titleBasicsURL() throws IOException {
        return toURL(requiredProperty(IMDB_TITLE_BASICS_URL));
    }
    
    /**
     * The URL of the Title Principals dataset files can be accessed and downloaded 
     * from https://datasets.imdbws.com/.
     * 
     * @return
     * @throws IOException 
     */
    public static URL titlePrincipalsURL () throws IOException {
        return toURL(requiredProperty(IMBD_TITLE_PRINCIPALS_URL));
    }
    
    /**
     * The URL of the Title Ratings dataset files can be accessed and downloaded 
     * from https://datasets.imdbws.com/.
     * 
     * @return
     * @throws IOException 
     */
    public static URL titleRatingsURL() throws IOException {
        return toURL(requiredProperty(IMBD_TITLE_RATINGS_URL));
    }
    
    /**
     * The URL of the Title Episode dataset files can be accessed and downloaded 
     * from https://datasets.imdbws.com/.
     * 
     * @return
     * @throws IOException 
     */
    public static URL titleEpisodeURL() throws IOException {
        return toURL(requiredProperty(IMBD_TITLE_EPISODE_URL));
    }

    /**
     * The URL of the Name Basic dataset files can be accessed and downloaded 
     * from https://datasets.imdbws.com/.
     * 
     * @return
     * @throws IOException 
     */    
    public static URL nameBasicsURL() throws IOException {
        return toURL(requiredProperty(IMBD_NAME_BASICS_URL));
    }
    
    /**
     * Returns the years of titles that should be persisted.
     * 
     * @return 
     */
    public static Set<String> titleIncludeYears() {
        return new HashSet<>(Arrays.asList(requiredProperty(TITLE_INCLUDE_YEARS).split("\\s*[,]\\s*")));
    }
    
    /**
     * Converts the given string to URL
     * 
     * @param urlString
     * @return
     * @throws IOException 
     */
    private static URL toURL(final String urlString) throws IOException {
        try {
            return new URL(urlString);
        } catch (final IOException ex) {
            throw new IOException("Invalid URL " + urlString, ex);
        }        
    }    
}
