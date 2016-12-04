package org.atomium.repository;

import org.atomium.util.Entity;

public interface EntityRepository<PK, T extends Entity<PK>> extends
		PersistableEntityRepository<PK, T>,
		SaveableEntityRepository<PK, T>
{

}
