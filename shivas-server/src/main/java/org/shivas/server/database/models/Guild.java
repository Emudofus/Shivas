package org.shivas.server.database.models;

import org.atomium.PersistableEntity;
import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.experience.GuildExperience;
import org.shivas.server.core.guilds.GuildMemberList;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 11:52
 */
public class Guild implements PersistableEntity<Integer> {
    public static final int MIN_MEMBERS = 10;
    public static final int BASE_AVAILABLE_PLACES = 40;

    private Integer id;
    private String name;
    private Player leader;
    private GuildEmblem emblem;
    private GuildExperience experience;
    private GuildMemberList members;

    private final EventDispatcher event = EventDispatchers.create();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public GuildEmblem getEmblem() {
        return emblem;
    }

    public void setEmblem(GuildEmblem emblem) {
        this.emblem = emblem;
    }

    public GuildExperience getExperience() {
        return experience;
    }

    public void setExperience(GuildExperience experience) {
        this.experience = experience;
    }

    public GuildMemberList getMembers() {
        return members;
    }

    public void setMembers(GuildMemberList members) {
        this.members = members;
    }

    public boolean isValid() {
        return members.count() >= MIN_MEMBERS;
    }

    public int getMaxPlaces() {
        return BASE_AVAILABLE_PLACES + experience.level();
    }

    public boolean isFull() {
        return members.count() >= getMaxPlaces();
    }

    public void unsubscribe(EventListener listener) {
        event.unsubscribe(listener);
    }

    public void subscribe(EventListener listener) {
        event.subscribe(listener);
    }

    public void publish(Event event) {
        this.event.publish(event);
    }
}
