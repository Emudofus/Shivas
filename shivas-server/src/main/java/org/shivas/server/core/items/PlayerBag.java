package org.shivas.server.core.items;

import org.atomium.repository.PersistableEntityRepository;
import org.shivas.common.maths.LimitedValue;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;

public class PlayerBag extends SimpleBag implements PersistentBag {
	
	private final Player owner;
	private final PersistableEntityRepository<Long, GameItem> repo;
	
	private long kamas;

	public PlayerBag(Player owner, PersistableEntityRepository<Long, GameItem> repo, long kamas) {
		this.owner = owner;
		this.repo = repo;
		this.kamas = kamas;
	}

	/**
	 * @return the kamas
	 */
	public long getKamas() {
		return kamas;
	}

	/**
	 * @param kamas the kamas to set
	 */
	public void setKamas(long kamas) {
		this.kamas = kamas;
	}
	
	public PlayerBag plusKamas(long kamas) {
		this.kamas += kamas;
		return this;
	}
	
	public PlayerBag minusKamas(long kamas) {
		this.kamas -= kamas;
		return this;
	}

	@Override
	public GameItem persist(GameItem item) {
		repo.persistLater(item);
		add(item);
		return item;
	}

	@Override
	public boolean delete(GameItem item) {
		repo.deleteLater(item);
		return remove(item);
	}
	
	public boolean full() {
		LimitedValue pods = owner.getStats().pods();
		return pods.current() >= pods.max();
	}
	
}
