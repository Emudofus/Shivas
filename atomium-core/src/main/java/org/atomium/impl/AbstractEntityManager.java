package org.atomium.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.atomium.EntityManager;
import org.atomium.cfg.Configuration;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEntityManager implements EntityManager {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractEntityManager.class);
	
	private Configuration cfg;
	private ExecutorService executor;
	private Connection connection;
	private boolean run;
	private List<Query> queries = Collections.synchronizedList(new ArrayList<Query>());

	public AbstractEntityManager(Configuration cfg) throws ClassNotFoundException {
		Class.forName(cfg.driver());
		
		this.cfg = cfg;
		this.executor = Executors.newSingleThreadExecutor();
	}
	
	protected abstract void unhandledException(Exception e);

	public void start() {
		try {
			connection = DriverManager.getConnection(cfg.connection(), cfg.user(), cfg.password());
			
			run = true;
			
			executor.submit(new Runnable() {
				public void run() {
					loop();
				}
			});
			
			log.info("connection successfully opened");
		} catch (SQLException e) {
			log.error("can't open connection because : {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		run = false;
		executor.shutdown();
		try {
			flush();
			connection.close();
			
			log.info("connection successfully stopped");
		} catch (SQLException e) {
			log.error("can't close connection because : {}", e.getMessage());
		}
	}
	
	private void loop() {
		while (run) {
			try {
				Thread.sleep(cfg.flushDelay());
				flush();
			} catch (InterruptedException e) {
				log.error("FATAL ERROR");
			}
		}
	}
	
	private static <T> List<T> copyAndClear(List<T> list) {
		List<T> result = new ArrayList<T>(list);
		list.clear();
		return result;
	}
	
	private void flush() {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			List<Query> copy = copyAndClear(queries);
			for (Query query : copy) {
				statement.execute(query.toString());
			}
			log.debug("{} queries were been executed", copy.size());
		} catch (SQLException e) {
			log.error(e.toString());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e.toString());
				}
			}
		}
	}

	public <T> T query(Query query, Function1<T, ResultSet> function) {
		T result = null;		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			
			ResultSet set = statement.executeQuery(query.toString());
			result = function.invoke(set);
		} catch (Exception e) {
			unhandledException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					unhandledException(e);
				}
			}
		}
		return result;
	}

	public void executeLater(Query query) {
		queries.add(query);
	}
	
	@Override
	public void execute(Query query) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			
			statement.execute(query.toString());
		} catch (Exception e) {
			unhandledException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					unhandledException(e);
				}
			}
		}
	}

}
