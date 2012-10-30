package org.shivas.core.core.items;

import org.atomium.repository.PersistableEntityRepository;
import org.atomium.repository.SaveableEntityRepository;
import org.shivas.common.maths.LimitedValue;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.Player;

public class PlayerBag extends SimpleBag implements PersistentBag {
	
	private final Player owner;
	private final PersistableEntityRepository<Long, GameItem> repo;
	private final SaveableEntityRepository<Integer, Player> players;
	
	private long kamas;

	public PlayerBag(Player owner, PersistableEntityRepository<Long, GameItem> repo, SaveableEntityRepository<Integer, Player> players, long kamas) {
		this.owner = owner;
		this.repo = repo;
		this.players = players;
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
		players.saveLater(owner);
	}
	
	public PlayerBag plusKamas(long kamas) {
		this.kamas += kamas;
		players.saveLater(owner);
		return this;
	}
	
	public PlayerBag minusKamas(long kamas) {
		this.kamas -= kamas;
		players.saveLater(owner);
		return this;
	}

	@Override
	public GameItem persist(GameItem item) {
		item.setOwner(owner);
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
