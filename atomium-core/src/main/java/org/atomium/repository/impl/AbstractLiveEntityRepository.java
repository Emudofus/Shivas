package org.atomium.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.atomium.EntityManager;
import org.atomium.LiveEntityReference;
import org.atomium.exception.LoadingException;
import org.atomium.repository.SaveableEntityRepository;
import org.atomium.util.Entity;
import org.atomium.util.Filter;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;

public abstract class AbstractLiveEntityRepository<PK, T extends Entity<PK>>
	implements SaveableEntityRepository<PK, T>
{
	
	protected final EntityManager em;
	
	public AbstractLiveEntityRepository(EntityManager em) {
		this.em = em;
	}
	
	public abstract long getEntityTimeToLive();
	
	protected abstract Query buildLoadQuery(PK pk);
	protected abstract Query buildSaveQuery(T entity);
	protected abstract T load(ResultSet result) throws SQLException;
	
	protected T find(Query query) {
		return em.query(query, new Function1<T, ResultSet>() {
			public T invoke(ResultSet arg1) throws Exception {
				arg1.next();
				return load(arg1);
			}
		});
	}

	public T find(PK pk) {
		return find(buildLoadQuery(pk));
	}

	public int count() {
		return -1;
	}

	public List<T> filter(Filter<T> filter) {
		return null;
	}

	public T find(Filter<T> filter) {
		return null;
	}

	public int load() throws LoadingException {
		return 0;
	}

	public boolean loaded() {
		return true;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return false;
			}

			public T next() {
				return null;
			}

			public void remove() {
			}
		};
	}
	
	public void save() {
		
	}

	public void saveLater() {
	}

	public void save(T entity) {
		Query query = buildSaveQuery(entity);
		em.execute(query);
	}
	
	public void saveLater(T entity) {
		Query query = buildSaveQuery(entity);
		em.executeLater(query);
	}
	
	public LiveEntityReference<PK, T> getReference(PK pk) {
		return new LiveEntityReference<PK, T>(pk, this);
	}
	
}
