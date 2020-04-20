package com.owodigi.movie.ratings.store.impl.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Provides the interface for clients to process the results of a database query.
 */
public interface ResultCallback {
    
    /**
     * Provides callers with a post operation hook to process the results of a
     * database query.
     * 
     * @param result 
     * @throws java.sql.SQLException 
     */
    public void process(final ResultSet result) throws SQLException;
    
    /**
     * Performs no operation upon the result.
     */
    public static final ResultCallback NO_OP_RESULT_CALLBACK = (final ResultSet result) -> {};
}
