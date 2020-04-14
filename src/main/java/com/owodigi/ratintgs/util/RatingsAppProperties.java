package com.owodigi.ratintgs.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 */
public final class RatingsAppProperties {
    public static final String SYSTEM_PROPERTIES_FILE = "ratings.properties.file";
    private static final String SYSTEM_PROPERTIES_PATH;
    private static final String DATABASE_PATH = "ratings.db.path";
    private static final String DATABASE_USERNAME = "ratings.db.username";
    private static final String DATABASE_PASSWORD = "ratings.db.password";
    private static final String IMDB_TITLE_BASICS_URL = "imdb.title.basics.url";
    private static final String IMBD_TITLE_RATINGS_URL = "imdb.title.ratings.url";
    private static final String TITLE_INCLUDE_YEARS = "title.include.years";
    private static final Properties PROPERTIES = new Properties();
    
    static {
        SYSTEM_PROPERTIES_PATH = loadProperties();
    }

    /**
     * 
     * @return 
     */
    public static Path databasePath() {
       return Paths.get(requiredProperty(DATABASE_PATH));
    }
    
    /**
     * 
     * @return 
     */
    public static String databaseUserName() {
        return requiredProperty(DATABASE_USERNAME);
    }
    
    /**
     * 
     * @return 
     */
    public static String databaseUserPassword() {
        return requiredProperty(DATABASE_PASSWORD);
    }
    
    /**
     * 
     * @return 
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
     * 
     * @param property
     * @return 
     */
    private static String requiredProperty(final String property) {
        final String value = PROPERTIES.getProperty(property);
        if (value == null) {
            throw new IllegalArgumentException(property + " is a required property; File: " + SYSTEM_PROPERTIES_PATH);
        }
        return value;
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public static URL titleBasicsURL() throws IOException {
        return toURL(requiredProperty(IMDB_TITLE_BASICS_URL));
    }
    
    public static URL titleRatingsURL() throws IOException {
        return toURL(requiredProperty(IMBD_TITLE_RATINGS_URL));
    }    
    
    /**
     * 
     * @return 
     */
    public static Set<String> titleIncludeYears() {
        return new HashSet<>(Arrays.asList(requiredProperty(TITLE_INCLUDE_YEARS).split("\\s*[,]\\s*")));
    }
    
    /**
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
