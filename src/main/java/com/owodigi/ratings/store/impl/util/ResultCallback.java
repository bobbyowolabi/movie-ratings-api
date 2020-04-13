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
    
    public static final ResultCallback NO_OP_RESULT_CALLBACK = (final ResultSet result) -> {};
}
