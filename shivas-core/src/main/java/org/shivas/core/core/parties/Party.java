package org.shivas.core.core.parties;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.shivas.common.collections.Maps2;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.types.BasePartyMemberType;
import org.shivas.core.core.channels.ChannelEvent;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.events.EventListener;
import org.shivas.core.database.models.Player;
import org.shivas.core.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class Party implements Iterable<Player> {
	
	private static final int MAX_MEMBERS = 8;
	
	private Player owner;
	private final Map<Integer, Player> members = Maps.newHashMap();
	private final EventDispatcher event = EventDispatchers.create();

	public Party(Player owner) {
		this.owner = owner;
		this.members.put(this.owner.getId(), this.owner);
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}
	
	public void speak(Player member, String message) {
		if (!contains(member)) return;
		
		event.publish(new ChannelEvent(
				ChannelEnum.Party,
				member,
				message
		));
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
		this.event.publish(new PartyEvent(PartyEventType.NEW_OWNER, owner));
	}

	public Player get(Integer memberId) {
		return members.get(memberId);
	}
	
	public void add(Player member) {
		if (contains(member)) return;
		
		members.put(member.getId(), member);
		event.publish(new PartyEvent(PartyEventType.ADD_MEMBER, member));
	}
	
	public void remove(Player member) {
		if (members.remove(member.getId()) == null) return;
		
		if (count() < 2) {
			event.publish(new PartyEvent(PartyEventType.CLOSE));
		} else {
			if (member == owner) {
				Player newOwner = Maps2.randomValue(members);
				setOwner(newOwner);
			}
			
			event.publish(new PartyEvent(PartyEventType.REMOVE_MEMBER, member));
		}
	}

	public int count() {
		return members.size();
	}
	
	public boolean isFull() {
		return count() >= MAX_MEMBERS;
	}
	
	public boolean contains(Player player) {
		return members.containsKey(player.getId());
	}
	
	public void refresh(Player player) {
		if (contains(player)) {
			event.publish(new PartyEvent(PartyEventType.REFRESH_MEMBER, player));
		}
	}
	
	public Collection<BasePartyMemberType> toBasePartyMemberType() {
		return Collections2.transform(members.values(), Converters.PLAYER_TO_BASEPARTYMEMBERTYPE);
	}

	@Override
	public Iterator<Player> iterator() {
		return members.values().iterator();
	}

}
