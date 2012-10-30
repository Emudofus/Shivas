package org.shivas.core.database.repositories;

import org.atomium.EntityManager;
import org.atomium.repository.EntityRepository;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.EmptyPrimaryKeyGenerator;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.StoredItem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/09/12
 * Time: 18:43
 */
@Singleton
public class StoredItemRepository extends AbstractEntityRepository<Long, StoredItem> {
    private static final String TABLE_NAME = "stored_items";

    private final EntityRepository<Long, GameItem> items;

    private final QueryBuilder deleteQuery, persistQuery, saveQuery;
    private final Query loadQuery;

    @Inject
    public StoredItemRepository(EntityManager em, EntityRepository<Long, GameItem> items) {
        super(em, new EmptyPrimaryKeyGenerator<Long>());

        this.items = items;

        this.deleteQuery = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
        this.persistQuery = em.builder().insert(TABLE_NAME).values("id", "quantity", "price");
        this.saveQuery = em.builder().update(TABLE_NAME).value("quantity").value("price").where("id", Op.EQ);
        this.loadQuery = em.builder().select(TABLE_NAME).toQuery();
    }

    @Override
    protected Query buildDeleteQuery(StoredItem entity) {
        return deleteQuery.toQuery().setParameter("id", entity.getId());
    }

    protected Query bindValues(Query query, StoredItem entity) {
        query.setParameter("id", entity.getId());
        query.setParameter("quantity", entity.getQuantity());
        query.setParameter("price", entity.getPrice());
        return query;
    }

    @Override
    protected Query buildPersistQuery(StoredItem entity) {
        Query query = bindValues(persistQuery.toQuery(), entity);
        return query;
    }

    @Override
    protected void onDeleted(StoredItem entity) {
        super.onDeleted(entity);

        if (entity.getItem().getQuantity() <= 0) {
            items.deleteLater(entity.getItem());
        }
    }

    @Override
    protected Query buildSaveQuery(StoredItem entity) {
        Query query = bindValues(saveQuery.toQuery(), entity);
        return query;
    }

    @Override
    protected Query buildLoadQuery() {
        return loadQuery;
    }

    @Override
    protected StoredItem load(ResultSet rset) throws SQLException {
        StoredItem item = new StoredItem();
        item.setItem(items.find(rset.getLong("id")));
        item.setQuantity(rset.getInt("quantity"));
        item.setPrice(rset.getLong("price"));

        item.getOwner().getStore().add(item);

        return item;
    }

    @Override
    protected void unhandledException(Exception e) {
    }
}
