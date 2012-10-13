package org.shivas.data.converter;

import org.atomium.util.query.Op;
import org.atomium.util.query.Order;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:20
 */
public class JDBCSelectQueryBuilderDecorator implements SelectQueryBuilder {
    private final Connection connection;
    private SelectQueryBuilder sqb;

    public JDBCSelectQueryBuilderDecorator(Connection connection, SelectQueryBuilder sqb) {
        this.connection = connection;
        this.sqb = sqb;
    }

    public Iterable<ResultSet> execute() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqb.toQuery().toString());
        statement.close();
        return ResultSetIterator.iterable(resultSet);
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
