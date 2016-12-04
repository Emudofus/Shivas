package org.atomium.util.query;

public interface UpdateQueryBuilder extends QueryBuilder {

	UpdateQueryBuilder value(String field);
	UpdateQueryBuilder value(String field, Object value);

	UpdateQueryBuilder where(String field, Op op);
	UpdateQueryBuilder where(String field, Op op, Object value);
	
	UpdateQueryBuilder and(String field, Op op);
	UpdateQueryBuilder and(String field, Op op, Object value);
	
	UpdateQueryBuilder or(String field, Op op);
	UpdateQueryBuilder or(String field, Op op, Object value);
	
}
