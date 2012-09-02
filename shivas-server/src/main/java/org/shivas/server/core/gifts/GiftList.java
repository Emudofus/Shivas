package org.shivas.server.core.gifts;

import java.util.Iterator;
import java.util.Map;

import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Gift;
import org.shivas.server.database.repositories.GiftRepository;
import org.shivas.server.utils.Converters;

import static org.shivas.common.collections.CollectionQuery.from;

public class GiftList implements Iterable<Gift> {
	
	private final Account owner;
	private final GiftRepository repo;
	
	private Map<Long, Gift> gifts;

	public GiftList(Account owner, GiftRepository repo) {
		this.owner = owner;
		this.repo = repo;
	}
	
	public GiftList refresh() {
		gifts = from(repo.findByOwner(owner)).computeMap(Converters.GIFT_TO_ID);
		return this;
	}

	public Gift peek() {
		Iterator<Gift> it = iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	public Gift pop() {
		Gift gift = peek();
		if (gift != null) {
			gifts.remove(gift.getId());
		}
		return gift;
	}

	@Override
	public Iterator<Gift> iterator() {
		return gifts.values().iterator();
	}

}
