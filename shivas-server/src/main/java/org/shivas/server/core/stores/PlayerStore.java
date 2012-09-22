package org.shivas.server.core.stores;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlaySellerType;
import org.shivas.protocol.client.types.StoreItemType;
import org.shivas.server.core.GameActorWithoutId;
import org.shivas.server.core.Location;
import org.shivas.server.core.Look;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.StoredItem;
import org.shivas.server.utils.Converters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/09/12
 * Time: 18:37
 */
public class PlayerStore implements Iterable<StoredItem>, GameActorWithoutId {
    private final Player owner;
    private final EntityRepository<Long, StoredItem> repo;
    private final Map<Long, StoredItem> items = Maps.newHashMap();
    private final EventDispatcher event = EventDispatchers.create();
    private final Object lock = new Object();

    private int id;
    private long earnedKamas;

    public PlayerStore(Player owner, EntityRepository<Long, StoredItem> repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public Object getLock() {
        return lock;
    }

    public void subscribe(EventListener listener) {
        event.subscribe(listener);
    }

    public void unsubscribe(EventListener listener) {
        event.unsubscribe(listener);
    }

    public int count() {
        return items.size();
    }

    public boolean isEmpty() {
        return count() <= 0;
    }

    public StoredItem get(long itemId) {
        return items.get(itemId);
    }

    public void add(StoredItem item) {
        items.put(item.getId(), item);
    }

    public void persist(StoredItem item) {
        repo.persistLater(item);
        add(item);
    }

    public boolean remove(long itemId) {
        return items.remove(itemId) != null;
    }

    public boolean remove(StoredItem item) {
        return remove(item.getId());
    }

    public void delete(StoredItem item) {
        if (remove(item)) {
            repo.deleteLater(item);
        }
    }

    public void refresh() {
        Iterator<StoredItem> it = iterator();
        while (it.hasNext()) {
            StoredItem stored = it.next();
            if (stored.getQuantity() <= 0) {
                it.remove();
                repo.deleteLater(stored);
            }
        }

        if (count() <= 0) {
            close();
        } else {
            event.publish(StoreEvent.REFRESH);
        }
    }

    public void close() {
        event.publish(StoreEvent.CLOSE);
        owner.getLocation().getMap().leave(this);
    }

    public long getEarnedKamas() {
        return earnedKamas;
    }

    public void setEarnedKamas(long earnedKamas) {
        this.earnedKamas = earnedKamas;
    }

    public PlayerStore plusEarnedKamas(long earnedKamas) {
        this.earnedKamas += earnedKamas;
        return this;
    }

    public PlayerStore minusEarnedKamas(long earnedKamas) {
        this.earnedKamas -= earnedKamas;
        return this;
    }

    @Override
    public int getPublicId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return owner.getName();
    }

    @Override
    public Location getLocation() {
        return owner.getLocation();
    }

    @Override
    public Look getLook() {
        return owner.getLook();
    }

    @Override
    public void teleport(GameMap map, short cell) {
        throw new RuntimeException("you can't teleport a store");
    }

    public Collection<StoreItemType> toStoreItemType() {
        return Collections2.transform(items.values(), Converters.STOREDITEM_TO_STOREITEMTYPE);
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        return toRolePlaySellerType();
    }

    public RolePlaySellerType toRolePlaySellerType() {
        return new RolePlaySellerType(
                id,
                owner.getLocation().getCell(),
                owner.getLocation().getOrientation(),
                owner.getName(),
                owner.getLook().skin(),
                owner.getLook().size(),
                owner.getLook().colors().first(),
                owner.getLook().colors().second(),
                owner.getLook().colors().third(),
                owner.getBag().accessoriesTemplateId(),
                false, // TODO guilds
                null,
                null
        );
    }

    @Override
    public Iterator<StoredItem> iterator() {
        return items.values().iterator();
    }
}
