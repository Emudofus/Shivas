package org.atomium.repository;

import java.util.List;

import org.atomium.EntityReference;
import org.atomium.util.Entity;
import org.atomium.util.Filter;

public interface BaseEntityRepository<PK, T extends Entity<PK>>
	extends Repository, Iterable<T>
{

	/**
	 * find an entity by its primary key (or id)
	 * @param pk primary key (or id)
	 * @return entity
	 */
	T find(PK pk);
	
	/**
	 * number of entities loaded
	 * @return int
	 */
	int count();
	
	/**
	 * filter entities
	 * @param filter the filter
	 * @return filtered entities
	 */
	List<T> filter(Filter<T> filter);
	
	/**
	 * find an entity by a filter
	 * @param filter the filter
	 * @return entity
	 */
	T find(Filter<T> filter);
	
	/**
	 * return a reference to an entity
	 * @param pk entity's id
	 * @return reference
	 */
	EntityReference<PK, T> getReference(PK pk);
	
}
