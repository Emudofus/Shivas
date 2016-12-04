package org.atomium.repository.impl;

import org.atomium.EntityManager;
import org.atomium.PersistableEntity;
import org.atomium.exception.LoadingException;
import org.atomium.repository.PersistableEntityRepository;
import org.atomium.util.pk.PrimaryKeyGenerator;
import org.atomium.util.query.Query;

public abstract class AbstractPersistRefreshableEntityRepository<PK, T extends PersistableEntity<PK>>
	extends AbstractRefreshableEntityRepository<PK, T> 
	implements PersistableEntityRepository<PK, T>
{
	protected final PrimaryKeyGenerator<PK> pkgen;

	protected AbstractPersistRefreshableEntityRepository(EntityManager em, int refreshRate, PrimaryKeyGenerator<PK> pkgen) {
		super(em, refreshRate);
		this.pkgen = pkgen;
	}
	
	@Override
	protected void afterLoading() throws LoadingException {
		super.afterLoading();
		
		for (PK id : entities.keySet()) {
			pkgen.setMax(id);
		}
	}

	protected abstract Query buildPersistQuery(T entity);
	protected abstract Query buildDeleteQuery(T entity);
	
	protected void beforePersist(T entity) {}
	protected void onPersisted(T entity) {}
	protected void beforeDelete(T entity) {}
	protected void onDeleted(T entity) {}

	@Override
	public void persist(T entity) {
		entity.setId(pkgen.next());
		beforePersist(entity);
		
		Query query = buildPersistQuery(entity);
		em.execute(query);

		entities.put(entity.getId(), entity);
		onPersisted(entity);
	}

	@Override
	public void persistLater(T entity) {
		entity.setId(pkgen.next());
		beforePersist(entity);
		
		Query query = buildPersistQuery(entity);
		em.executeLater(query);
		
		entities.put(entity.getId(), entity);
		onPersisted(entity);
	}

	@Override
	public void delete(T entity) {
		beforeDelete(entity);
		
		Query query = buildDeleteQuery(entity);
		em.execute(query);
		
		entities.remove(entity.getId());
		onDeleted(entity);
	}

	@Override
	public void deleteLater(T entity) {
		beforeDelete(entity);
		
		Query query = buildDeleteQuery(entity);
		em.execute(query);
		
		entities.remove(entity.getId());
		onDeleted(entity);
	}

}
