package com.owodigi.movie.ratings.store.impl.util;

/**
 * Defines the configuration for a database column
 */
public class ColumnConfig {
    private final String column;
    private final String type;

    /**
     * Creates a new ColumnConfig instance.
     * @param column
     * @param type 
     */
    public ColumnConfig(final String column, final String type) {
        this.column = column;
        this.type = type;
    }

    /**
     * Name of column.
     * 
     * @return 
     */
    public String column() {
        return column;
    }

    /**
     * Data type of the column.
     * 
     * @return 
     */
    public String type() {
        return type;
    }
}
