package org.atomium;

import org.atomium.repository.impl.AbstractLiveEntityRepository;
import org.atomium.util.Entity;

public class LiveEntityReference<PK, T extends Entity<PK>> implements EntityReference<PK, T> {

	private final PK pk;
	private final AbstractLiveEntityRepository<PK, T> repo;
	
	private long lastUpdate;
	private T data;

	public LiveEntityReference(PK pk, AbstractLiveEntityRepository<PK, T> repo) {
		this.pk = pk;
		this.repo = repo;
		this.lastUpdate = 0;
	}
	
	public boolean hasExpired() {
		return (System.currentTimeMillis() - lastUpdate) >= repo.getEntityTimeToLive();
	}

	@Override
	public T get() {
		if (data == null || hasExpired()) {
			data = repo.find(pk);
			lastUpdate = System.currentTimeMillis();
		}
		return data;
	}

	@Override
	public void set(T o) {
		data = o;
		lastUpdate = System.currentTimeMillis();
	}

	@Override
	public boolean isNull() {
		return data == null;
	}

	@Override
	public PK getPk() {
		return pk;
	}

}
