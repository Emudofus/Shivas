package org.shivas.server.core.gifts;

import java.util.Iterator;
import java.util.Map;

import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Gift;
import org.shivas.server.database.repositories.GiftRepository;

import com.google.common.collect.Maps;

public class GiftList implements Iterable<Gift> {
	
	private final Account owner;
	private final GiftRepository repo;
	
	private final Map<Long, Gift> gifts = Maps.newHashMap();

	public GiftList(Account owner, GiftRepository repo) {
		this.owner = owner;
		this.repo = repo;
	}
	
	public GiftList refresh() {
		for (Gift gift : repo.findByOwner(owner)) {
			if (gifts.containsKey(gift.getId())) continue;
			
			gifts.put(gift.getId(), gift);
		}
		
		return this;
	}

	public Gift peek() {
		Iterator<Gift> it = iterator();
		return it.hasNext() ? it.next() : null;
	}

	public Gift get(long giftId) {
		return gifts.get(giftId);
	}
	
	public boolean remove(long giftId) {
		return gifts.remove(giftId) != null;
	}
	
	public boolean remove(Gift gift) {
		return remove(gift.getId());
	}
	
	public void delete(Gift gift) {
		if (remove(gift)) {
			repo.delete(gift);
		}
	}

	@Override
	public Iterator<Gift> iterator() {
		return gifts.values().iterator();
	}

}
