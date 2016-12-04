package org.atomium.repository;

import org.atomium.util.Entity;

public interface SaveableEntityRepository<PK, T extends Entity<PK>>
	extends BaseEntityRepository<PK, T>
{
	/**
	 * save all entities
	 */
	void saveLater();
	
	/**
	 * save only one entity
	 * @param entity
	 */
	void saveLater(T entity);
	
	/**
	 * save all entities now
	 */
	void save();
	
	/**
	 * save only one entity now
	 * @param entity
	 */
	void save(T entity);
}
