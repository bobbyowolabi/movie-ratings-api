package com.owodigi.util;

import com.owodigi.util.IMDbTSVFormats.NameBasicFormat;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.util.IMDbTSVFormats.TitlePrincipalsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import com.owodigi.util.TSVFormat;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.junit.Assert;
import org.junit.Test;

public class IMDbTSVFormatsTest {
    private static final String TEST_RESOURCES_DIR = "src/test/resources/";
            
    
    public void test(final List<List<String>> expected, final String input, final TSVFormat expectedFormat) {
        try (final Reader reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8);
             final CSVParser actual = expectedFormat.format().parse(reader)) {
            AssertUtils.assertEquals(expected, actual);
        } catch (final IOException ex) {
            Assert.fail("Unable to parse " + input + " due to " + ex);
        }
    }      
    
    @Test
    public void nameBaiscsTSV() {
        final String input = TEST_RESOURCES_DIR + "name.basics-sample.tsv";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("nm0000001", "Fred Astaire", "1899", "1987", "soundtrack,actor,miscellaneous", "tt0053137,tt0050419,tt0072308,tt0043044"),
            Arrays.asList("nm0000002", "Lauren Bacall", "1924", "2014", "actress,soundtrack", "tt0071877,tt0038355,tt0037382,tt0117057"),
            Arrays.asList("nm0000003", "Brigitte Bardot", "1934", "\\N", "actress,soundtrack,producer", "tt0049189,tt0057345,tt0054452,tt0059956")
        );
        test(expected, input, new NameBasicFormat());
    }
 
    @Test
    public void titleBasicsTSV() {
        final String input = TEST_RESOURCES_DIR + "title.basics-sample.tsv";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "short", 	"Carmencita", "Carmencita", "0", "1894", "\\N", "1", "Documentary,Short"),
            Arrays.asList("tt0000002", "short", 	"Le clown et ses chiens", "Le clown et ses chiens", "0", "1892", "\\N", "5", "Animation,Short"),
            Arrays.asList("tt0000003", "short", "Pauvre Pierrot", "Pauvre Pierrot", "0", "1892", "\\N", "4", "Animation,Comedy,Romance")
        );
        test(expected, input, new TitleBasicsFormat());
    }    
    
    @Test
    public void titleEpisodeTSV() {
        final String input = TEST_RESOURCES_DIR + "title.episode-sample.tsv";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0041951", "tt0041038", "1", "9"),
            Arrays.asList("tt0042816", "tt0989125", "1", "17"),
            Arrays.asList("tt0042889", "tt0989125", "\\N", "\\N")
        );
        test(expected, input, new TitleEpisodeFormat());        
    }
    
    @Test
    public void titlePrincipalsTSV() {
        final String input = TEST_RESOURCES_DIR + "title.principals-sample.tsv";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "1", "nm1588970", "self", "\\N", "[\"Self\"]"),
            Arrays.asList("tt0000001", "2", "nm0005690", "director", "\\N", "\\N"),
            Arrays.asList("tt0000001", "3", "nm0374658", "cinematographer", "director of photography", "\\N"),
            Arrays.asList("tt0000005", "1", "nm0443482",	 "actor", "\\N", "[\"Blacksmith\"]")
        );
        test(expected, input, new TitlePrincipalsFormat());
    }   
    
    @Test
    public void titleRatingsTSV() {
        final String input = TEST_RESOURCES_DIR + "title.ratings-sample.tsv";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "5.6", "1593"),
            Arrays.asList("tt0000002", "6.0", "195"),
            Arrays.asList("tt0000003", "6.5", "1266")
        );
        test(expected, input, new TitleRatingsFormat());
    }
}
