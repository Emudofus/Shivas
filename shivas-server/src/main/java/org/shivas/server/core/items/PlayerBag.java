package org.shivas.server.core.items;

import org.atomium.repository.PersistableEntityRepository;
import org.shivas.common.maths.LimitedValue;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;

public class PlayerBag extends SimpleBag implements PersistentBag {
	
	private final Player owner;
	private final PersistableEntityRepository<Long, GameItem> repo;

	public PlayerBag(Player owner, PersistableEntityRepository<Long, GameItem> repo) {
		this.owner = owner;
		this.repo = repo;
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
