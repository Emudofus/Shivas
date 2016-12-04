package org.atomium.util.query;

public interface SelectQueryBuilder extends QueryBuilder {
	
	SelectQueryBuilder where(String field, Op op);
	SelectQueryBuilder where(String field, Op op, Object value);
	
	SelectQueryBuilder and(String field, Op op);
	SelectQueryBuilder and(String field, Op op, Object value);
	
	SelectQueryBuilder or(String field, Op op);
	SelectQueryBuilder or(String field, Op op, Object value);
	
	SelectQueryBuilder orderBy(String field, Order order);
	SelectQueryBuilder and(String field, Order order);
	
}
