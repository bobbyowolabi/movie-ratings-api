package com.owodigi.ratings.store.impl.util;

/**
 *
 */
public class ColumnConfig {
    private final String column;
    private final String type;

    /**
     * 
     * @param column
     * @param type 
     */
    public ColumnConfig(final String column, final String type) {
        this.column = column;
        this.type = type;
    }

    /**
     * 
     * @return 
     */
    public String column() {
        return column;
    }

    /**
     * 
     * @return 
     */
    public String type() {
        return type;
    }
}
