package org.atomium.repository;

import org.atomium.util.Entity;

public interface PersistableEntityRepository<PK, T extends Entity<PK>>
	extends BaseEntityRepository<PK, T>
{
	/**
	 * create entity in the database now
	 * @param entity
	 */
	void persist(T entity);
	
	/**
	 * create entity in the database
	 * @param entity
	 */
	void persistLater(T entity);

	/**
	 * delete entity in the database now
	 * @param entity
	 */
	void delete(T entity);
	
	/**
	 * delete entity in the database
	 * @param entity
	 */
	void deleteLater(T entity);
}
