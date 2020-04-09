package com.owodigi.util;

import org.apache.commons.csv.CSVFormat;

/**
 * Defines functionality needed to parse a given TSV file.
 */
public abstract class TSVFormat {
    
     /**
     * Returns the class of the enum type that defines the headers 
     * that appear on the first line in each file that describe what is in each
     * column.
     * 
     * @return 
     */   
    public abstract Class<? extends Enum<?>> headerClass();
     
    /**
     * Specifies the format of a TSV file and parses input.
     * 
     * Header line will be skipped.
     *
     * @return
     */
    public final CSVFormat format() {
        return CSVFormat
                .TDF
                .withHeader(headerClass())
                .withSkipHeaderRecord();
    }
}
