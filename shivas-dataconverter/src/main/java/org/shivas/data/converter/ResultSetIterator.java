package org.shivas.data.converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:30
 */
public class ResultSetIterator implements Iterator<ResultSet> {
    public static Iterable<ResultSet> iterable(final ResultSet rset) {
        return new Iterable<ResultSet>() {
            public Iterator<ResultSet> iterator() {
                return new ResultSetIterator(rset);
            }
        };
    }

    private final ResultSet resultSet;

    private SQLException lastException;

    public ResultSetIterator(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public SQLException getLastException() {
        return lastException;
    }

    @Override
    public boolean hasNext() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            lastException = e;
            return false;
        }
    }

    @Override
    public ResultSet next() {
        if (lastException != null) return null;
        return resultSet;
    }

    @Override
    public void remove() {
        throw new RuntimeException("you can't do this");
    }
}
