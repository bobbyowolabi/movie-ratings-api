package com;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class TSVReaderTest {
    private static final String TEST_RESOURCES_DIR = "src/test/resource/";
            
    @Test
    public void testBasicInput() throws IOException {
        final String input = TEST_RESOURCES_DIR + "basic";
        final InputStream inputStream = Files.newInputStream(Paths.get(input));
        final TSVReader reader = new TSVReader(inputStream);
        final reader.readNext();
    }
}
