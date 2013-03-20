package org.shivas.core.database;

import com.typesafe.config.Config;
import org.atomium.cfg.Configuration;
import org.atomium.impl.AbstractEntityManager;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShivasEntityManager extends AbstractEntityManager {

	private static final Logger log = LoggerFactory.getLogger(ShivasEntityManager.class);
	
	private QueryBuilderFactory builder;

	@Inject
	public ShivasEntityManager(final Config config) throws ClassNotFoundException {
		super(new Configuration() {
			public String user() {
				return config.getString("shivas.database.user");
			}
			
			public String password() {
				return config.getString("shivas.database.password");
			}
			
			public int flushDelay() {
				return config.getInt("shivas.database.options.flush_delay");
			}
			
			public String driver() {
				return "com.mysql.jdbc.Driver";
			}
			
			public String connection() {
				return config.getString("shivas.database.datasource");
			}
		});
		
		builder = new MySqlQueryBuilderFactory();
	}

	@Override
	public QueryBuilderFactory builder() {
		return builder;
	}

	@Override
	protected void unhandledException(Exception e) {
		log.error(String.format("unhandled %s %s : %s",
				e.getClass().getSimpleName(),
				e.getStackTrace()[0],
				e.getMessage()
		));
	}

}
