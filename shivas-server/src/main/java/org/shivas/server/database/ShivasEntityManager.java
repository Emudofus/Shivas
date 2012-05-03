package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.cfg.Configuration;
import org.atomium.impl.AbstractEntityManager;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.mysql.MySqlQueryBuilder;
import org.shivas.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ShivasEntityManager extends AbstractEntityManager {

	private static final Logger log = LoggerFactory.getLogger(ShivasEntityManager.class);
	
	private QueryBuilderFactory builder;

	@Inject
	public ShivasEntityManager(final Config config) throws ClassNotFoundException {
		super(new Configuration() {
			public String user() {
				return config.databaseUser();
			}
			
			public String password() {
				return config.databasePassword();
			}
			
			public int flushDelay() {
				return config.databaseFlushDelay();
			}
			
			public String driver() {
				return "com.mysql.jdbc.Driver";
			}
			
			public String connection() {
				return config.databaseConnection();
			}
		});
		
		builder = new MySqlQueryBuilder();
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
