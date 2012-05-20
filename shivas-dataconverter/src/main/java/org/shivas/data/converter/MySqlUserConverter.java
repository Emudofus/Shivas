package org.shivas.data.converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.atomium.util.Action1;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;

public abstract class MySqlUserConverter implements Converter {

	private Connection connection;
	
	protected QueryBuilderFactory q;
	
	protected void initConnection(String host, String db, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?zeroDateTimeBehavior=convertToNull", user, pass);
			q = new MySqlQueryBuilderFactory();
		} catch (ClassNotFoundException e) {
			App.log("can't load driver because : %s", e.getMessage());
			System.exit(1);
		} catch (SQLException e) {
			App.log("can't open connection because : %s", e.getMessage());
			System.exit(1);
		}
	}
	
	protected <T> T useStatement(Function1<T, Statement> action) {
		Statement statement = null;
		T result = null;
		try {
			statement = connection.createStatement();
			
			result = action.invoke(statement);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	protected void query(final Query query, final Action1<ResultSet> action) {
		useStatement(new Action1<Statement>() {
			public Void invoke(Statement arg1) throws Exception {
				ResultSet result = arg1.executeQuery(query.toString());
				action.invoke(result);
				result.close();
				return null;
			}
		});
	}

}
