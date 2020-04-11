package com.owodigi.ratings.store.impl.util;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public interface StatementCallback {

    public void execute(final String sql, final Statement statement) throws SQLException;
}
