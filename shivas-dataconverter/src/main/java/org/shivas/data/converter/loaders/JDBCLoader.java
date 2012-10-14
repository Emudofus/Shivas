package org.shivas.data.converter.loaders;

import org.atomium.util.query.QueryBuilderFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:14
 */
public abstract class JDBCLoader implements DataLoader {
    protected final Connection connection;
    protected final QueryBuilderFactory q;

    protected JDBCLoader(String url, String driver, QueryBuilderFactory q) {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.q = q;
    }

    protected JDBCSelectQueryBuilderDecorator select(String table) {
        return new JDBCSelectQueryBuilderDecorator(connection, q.select(table));
    }
}
