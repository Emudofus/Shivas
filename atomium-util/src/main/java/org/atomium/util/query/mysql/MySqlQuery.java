package org.atomium.util.query.mysql;

import org.atomium.util.query.Query;

public class MySqlQuery implements Query {
	
	private String query;
	
	public MySqlQuery(String query) {
		this.query = query;
	}

	public Query setParameter(String field, Object obj) {
		query = query.replace("#" + field + "#", MySqlOp.toString(obj));
		return this;
	}
	
	@Override
	public String toString() {
		return query;
	}

}
