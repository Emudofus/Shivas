package org.atomium.util.query.mysql;

import org.atomium.util.query.Op;
import org.atomium.util.query.Order;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;

public class MySqlSelectQueryBuilder implements SelectQueryBuilder {
	
	private String table;
	private String[] fields;
	
	private StringBuilder sb = new StringBuilder();

	public MySqlSelectQueryBuilder(String table, String... fields) {
		this.table = table;
		this.fields = fields;
	}
	
	private void print(Op op) {
		sb.append(MySqlOp.print(op));
	}
	
	private void print(Order order) {
		sb.append(MySqlOp.print(order));
	}

	public SelectQueryBuilder where(String field, Op op) {
		sb.append("WHERE `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public SelectQueryBuilder where(String field, Op op, Object value) {
		sb.append("WHERE `").append(field).append("`");
		print(op);
		sb.append("'").append(MySqlOp.toString(value)).append("'");
		
		return this;
	}

	public SelectQueryBuilder and(String field, Op op) {
		sb.append(" AND `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public SelectQueryBuilder and(String field, Op op, Object value) {
		sb.append(" AND `").append(field).append("`");
		print(op);
		sb.append("'").append(MySqlOp.toString(value)).append("'");
		
		return this;
	}

	public SelectQueryBuilder or(String field, Op op) {
		sb.append(" OR `").append(field).append("`");
		print(op);
		sb.append("'#").append(field).append("#'");
		
		return this;
	}

	public SelectQueryBuilder or(String field, Op op, Object value) {
		sb.append(" OR `").append(field).append("`");
		print(op);
		sb.append("'").append(MySqlOp.toString(value)).append("'");
		
		return this;
	}

	public SelectQueryBuilder orderBy(String field, Order order) {
		sb.append(" ORDER BY `").append(field).append("` ");
		print(order);
		
		return this;
		
	}

	public SelectQueryBuilder and(String field, Order order) {
		sb.append(" AND `").append(field).append("` ");
		print(order);
		
		return this;
	}
	
	public Query toQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("SELECT ");
		if (fields.length > 0) {
			boolean first = true;
			for (String field : fields) {
				if (first) first = false;
				else s.append(", ");
				s.append('`').append(field).append('`');
			}
		} else {
			s.append("*");
		}
		
		s.append(" FROM `").append(table).append("` ");

		return new MySqlQuery(s.toString() + sb.toString() + ";");
	}

}
