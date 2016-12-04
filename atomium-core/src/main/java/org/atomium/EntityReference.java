package org.atomium;

import org.atomium.util.Entity;
import org.atomium.util.Reference;

public interface EntityReference<PK, T extends Entity<PK>> extends Reference<T> {
	PK getPk();
}
