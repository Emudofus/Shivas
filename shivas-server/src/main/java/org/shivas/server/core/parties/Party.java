package org.shivas.server.core.parties;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.shivas.protocol.client.types.BasePartyMemberType;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.database.models.Player;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class Party implements Iterable<Player> {
	
	private static final int MAX_MEMBERS = 8;
	
	private Player owner;
	private final Map<Integer, Player> members = Maps.newHashMap();
	private final EventDispatcher event = EventDispatchers.create();

	public Party(Player owner) {
		this.owner = owner;
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public void add(Player member) {
		if (contains(member)) return;
		
		members.put(member.getId(), member);
		event.publish(new PartyEvent(PartyEventType.ADD_MEMBER, member));
	}
	
	public void remove(Player member) {
		if (!contains(member)) return;
		
		if (count() - 1 <= 1) {
			// TODO close the party
		}
		else if (owner == member) {
			// TODO assign new leader and kick him from the party
		} else {
			// TODO kick member from the party
		}
	}

	public int count() {
		return members.size();
	}
	
	public boolean isFull() {
		return count() <= MAX_MEMBERS;
	}
	
	public boolean contains(Player player) {
		return members.containsKey(player.getId());
	}
	
	public Collection<BasePartyMemberType> toBasePartyMemberType() {
		return Collections2.transform(members.values(), Converters.PLAYER_TO_BASEPARTYMEMBERTYPE);
	}

	@Override
	public Iterator<Player> iterator() {
		return members.values().iterator();
	}

}
