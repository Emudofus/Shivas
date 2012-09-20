package org.shivas.server.core.stores;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.types.StoreItemType;
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
public class PlayerStore implements Iterable<StoredItem> {
    private final Player owner;
    private final EntityRepository<Long, StoredItem> repo;
    private final Map<Long, StoredItem> items = Maps.newHashMap();

    public PlayerStore(Player owner, EntityRepository<Long, StoredItem> repo) {
        this.owner = owner;
        this.repo = repo;
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

    public Collection<StoreItemType> toStoreItemType() {
        return Collections2.transform(items.values(), Converters.STOREDITEM_TO_STOREITEMTYPE);
    }

    @Override
    public Iterator<StoredItem> iterator() {
        return items.values().iterator();
    }
}
