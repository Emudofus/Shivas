package org.atomium.repository.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Timer;

import org.atomium.EntityManager;
import org.atomium.util.Action1;
import org.atomium.util.Entity;
import org.atomium.util.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRefreshableEntityRepository<PK, T extends Entity<PK>> extends AbstractSaveableEntityRepository<PK, T> implements ActionListener {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractRefreshableEntityRepository.class);
	
	private final Timer timer;
	private final int refreshRate;

	protected AbstractRefreshableEntityRepository(EntityManager em, int refreshRate) {
		super(em);
		
		this.refreshRate = refreshRate;
		this.timer = new Timer(this.refreshRate * 1000, this);
	}
	
	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	protected abstract Query getRefreshQuery();
	protected abstract Query getSetRefreshedQuery();
	
	protected abstract PK getPrimaryKey(ResultSet rset) throws SQLException;
	protected abstract void refresh(T entity, ResultSet rset) throws SQLException;
	
	private void refreshAll(ResultSet rset) throws Exception {
		while (rset.next()) {
			PK pk = getPrimaryKey(rset);
			if (pk == null) throw new Exception("can't get the entity's pk");
			
			T entity = entities.get(pk);
			if (entity == null) {
				entity = load(rset);
				entities.put(entity.getId(), entity);
			}
			
			refresh(entity, rset);
		}
		
		em.execute(getSetRefreshedQuery());
	}
	
	@Override
	public final void actionPerformed(ActionEvent arg0) {
		em.query(getRefreshQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				refreshAll(arg1);
				return null;
			}
		});
		
		log.debug("repository refreshed");
	}

}
