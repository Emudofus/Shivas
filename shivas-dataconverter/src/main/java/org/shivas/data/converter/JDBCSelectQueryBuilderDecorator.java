package org.shivas.data.converter;

import org.atomium.util.query.Op;
import org.atomium.util.query.Order;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:20
 */
public class JDBCSelectQueryBuilderDecorator implements SelectQueryBuilder {
    private static class ResultSetIterator implements Iterator<ResultSet> {
        private final Statement statement;
        private final ResultSet resultSet;

        private SQLException lastException;

        private ResultSetIterator(Statement statement, ResultSet resultSet) {
            this.statement = statement;
            this.resultSet = resultSet;
        }

        public boolean hasNext() {
            try {
                if (!resultSet.next()) {
                    statement.close();
                    return false;
                }
                return true;
            } catch (SQLException e) {
                lastException = e;
                return false;
            }
        }

        public ResultSet next() {
            return lastException != null ? null : resultSet;
        }

        public void remove() {
            throw new RuntimeException("you can't do this");
        }
    }

    private static class ResultSetIterable implements Iterable<ResultSet> {

        private final Statement statement;
        private final ResultSet resultSet;

        private ResultSetIterable(Statement statement, ResultSet resultSet) {
            this.statement = statement;
            this.resultSet = resultSet;
        }

        public Iterator<ResultSet> iterator() {
            return new ResultSetIterator(statement, resultSet);
        }
    }

    private final Connection connection;
    private SelectQueryBuilder sqb;

    public JDBCSelectQueryBuilderDecorator(Connection connection, SelectQueryBuilder sqb) {
        this.connection = connection;
        this.sqb = sqb;
    }

    public Iterable<ResultSet> execute() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqb.toQuery().toString());
        return new ResultSetIterable(statement, resultSet);
    }

    @Override
    public JDBCSelectQueryBuilderDecorator where(String s, Op op) {
        sqb = sqb.where(s, op);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator where(String s, Op op, Object o) {
        sqb = sqb.where(s, op, o);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator and(String s, Op op) {
        sqb = sqb.and(s, op);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator and(String s, Op op, Object o) {
        sqb = sqb.and(s, op, o);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator or(String s, Op op) {
        sqb = sqb.or(s, op);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator or(String s, Op op, Object o) {
        sqb = sqb.or(s, op, o);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator orderBy(String s, Order order) {
        sqb = sqb.orderBy(s, order);
        return this;
    }

    @Override
    public JDBCSelectQueryBuilderDecorator and(String s, Order order) {
        sqb.and(s, order);
        return this;
    }

    @Override
    public Query toQuery() {
        return sqb.toQuery();
    }
}
