package org.atomium;

import org.atomium.repository.BaseEntityRepository;
import org.atomium.util.Entity;

public class LazyReference<PK, T extends Entity<PK>> implements EntityReference<PK, T> {
	
	public static <PK, T extends Entity<PK>> LazyReference<PK, T> create(T entity) {
		return new LazyReference<PK, T>(entity);
	}
	
	private PK pk;
	private BaseEntityRepository<PK, T> repo;
	
	private T entity;
	
	public LazyReference(PK pk, BaseEntityRepository<PK, T> repo) {
		this.pk = pk;
		this.repo = repo;
	}
	
	public LazyReference(T entity) {
		this.pk = entity.getId();
		this.entity = entity;
	}

	public PK getPk() {
		return pk;
	}

	public void setPk(PK pk) {
		this.pk = pk;
	}

	@Override
	public T get() {
		if (entity == null) {
			entity = repo.find(pk);
		}
		return entity;
	}

	@Override
	public void set(T o) {
		this.entity = o;
	}

	@Override
	public boolean isNull() {
		return entity == null;
	}

}
