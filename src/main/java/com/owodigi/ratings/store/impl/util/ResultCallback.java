package com.owodigi.ratings.store.impl.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public interface ResultCallback {
    /**
     * 
     * @param result 
     */
    public void process(final ResultSet result) throws SQLException;
}
