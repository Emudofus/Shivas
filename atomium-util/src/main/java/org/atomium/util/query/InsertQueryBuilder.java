package org.atomium.util.query;

public interface InsertQueryBuilder extends QueryBuilder {

	InsertQueryBuilder value(String field);
	InsertQueryBuilder value(String field, Object value);
	InsertQueryBuilder values(String... fields);
	
}
