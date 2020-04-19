package com.owodigi.movie.ratings.store.impl;

import static com.owodigi.movie.ratings.store.impl.H2StoreTest.DATABASE_FILE_SUFFIX;
import com.owodigi.movie.ratings.store.impl.util.ColumnConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class GeneralH2StoreTest extends H2StoreTest {
    
    @Test
    public void dbDirectoryDoesNotExist() throws IOException {
        final Path databasePath = Paths.get("./target/test-data3/foo");
        final Path databaseFile = Paths.get(databasePath.toString() + DATABASE_FILE_SUFFIX);
        Files.deleteIfExists(databaseFile);
        try {
            final DatabaseStore store = new DatabaseStore(connection(databasePath.toString())) {
                
                @Override
                protected List<ColumnConfig> columnConfigs() {
                    return Arrays.asList(
                        new ColumnConfig(EpisodeTable.columns.tconst.name(), "VARCHAR(255)")
                    );
                }
                
                @Override
                protected String tableName() {
                    return "TEST_TABLE";
                }
            };
            store.execute("INSERT INTO TEST_TABLE (tconst) VALUES (1);");
            Assert.assertEquals(databaseFile + " exists", true, Files.exists(databaseFile));
        } finally {
            final Path parent = databaseFile.getParent();
            Files.list(parent).sequential().forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException ex) {
                    Assert.fail(ex.toString());
                }
            });
            Files.deleteIfExists(parent);
        }
    }  
}
