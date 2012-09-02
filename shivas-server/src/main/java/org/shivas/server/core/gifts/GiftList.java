package org.shivas.server.core.gifts;

import java.util.Iterator;
import java.util.List;

import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Gift;
import org.shivas.server.database.repositories.GiftRepository;

public class GiftList implements Iterable<Gift> {
	
	private final Account owner;
	private final GiftRepository repo;
	
	private List<Gift> gifts;

	public GiftList(Account owner, GiftRepository repo) {
		this.owner = owner;
		this.repo = repo;
	}
	
	public GiftList refresh() {
		gifts = repo.findByOwner(owner);
		return this;
	}

	public Gift pop() {
		return gifts.size() <= 0 ?
				null :
				gifts.remove(0);
	}

	@Override
	public Iterator<Gift> iterator() {
		return gifts.iterator();
	}

}
