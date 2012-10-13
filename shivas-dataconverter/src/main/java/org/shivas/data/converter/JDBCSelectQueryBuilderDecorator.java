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
    private final SelectQueryBuilder sqb;

    public JDBCSelectQueryBuilderDecorator(Connection connection, SelectQueryBuilder sqb) {
        this.connection = connection;
        this.sqb = sqb;
    }

    public ResultSet execute() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqb.toQuery().toString());
        statement.close();
        return resultSet;
    }

    @Override
    public SelectQueryBuilder where(String s, Op op) {
        return sqb.where(s, op);
    }

    @Override
    public SelectQueryBuilder where(String s, Op op, Object o) {
        return sqb.where(s, op, o);
    }

    @Override
    public SelectQueryBuilder and(String s, Op op) {
        return sqb.and(s, op);
    }

    @Override
    public SelectQueryBuilder and(String s, Op op, Object o) {
        return sqb.and(s, op, o);
    }

    @Override
    public SelectQueryBuilder or(String s, Op op) {
        return sqb.or(s, op);
    }

    @Override
    public SelectQueryBuilder or(String s, Op op, Object o) {
        return sqb.or(s, op, o);
    }

    @Override
    public SelectQueryBuilder orderBy(String s, Order order) {
        return sqb.orderBy(s, order);
    }

    @Override
    public SelectQueryBuilder and(String s, Order order) {
        return sqb.and(s, order);
    }

    @Override
    public Query toQuery() {
        return sqb.toQuery();
    }
}
