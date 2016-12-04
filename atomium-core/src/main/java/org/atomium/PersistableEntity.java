package org.atomium;

import org.atomium.util.Entity;

public interface PersistableEntity<PK> extends Entity<PK> {

	void setId(PK pk);
	
}
