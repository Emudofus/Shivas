package org.shivas.server.database.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.EntityReference;
import org.atomium.exception.LoadingException;
import org.atomium.repository.BaseEntityRepository;
import org.atomium.util.Action1;
import org.atomium.util.Filter;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.data.Container;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Gift;

import com.google.common.collect.Lists;

@Singleton
public class GiftRepository implements BaseEntityRepository<Long, Gift> {
	
	private static final String TABLE_NAME = "gifts";
	
	private final EntityManager em;
	private final BaseEntityRepository<Integer, Account> accounts;
	private final Container ctner;
	
	private final QueryBuilder loadQuery, deleteQuery;

	@Inject
	public GiftRepository(EntityManager em, BaseEntityRepository<Integer, Account> accounts, Container ctner) {
		this.em = em;
		this.accounts = accounts;
		this.ctner = ctner;
		
		this.loadQuery = em.builder()
				.select(TABLE_NAME)
				.where("owner", Op.EQ);
		this.deleteQuery = em.builder()
				.delete(TABLE_NAME)
				.where("owner", Op.EQ);
	}

	private Gift load(ResultSet rset) throws SQLException {
		ItemTemplate tpl = ctner.get(ItemTemplate.class).byId(rset.getInt("item"));
		
		Gift gift = new Gift();
		gift.setId(rset.getLong("id"));
		gift.setOwner(accounts.find(rset.getInt("owner")));
		gift.setItem((GameItem) tpl.generate()); // pas encore de solution miracle contre ce cast :(
		gift.setQuantity(rset.getInt("quantity"));
		gift.setTitle(rset.getString("title"));
		gift.setMessage(rset.getString("message"));
		
		return gift;
	}
	
	public List<Gift> findByOwner(int ownerId) {
		final Query load = loadQuery.toQuery().setParameter("owner", ownerId),
					delete = deleteQuery.toQuery().setParameter("owner", ownerId);
		
		final List<Gift> gifts = Lists.newArrayList();

		em.query(load, new Action1<ResultSet>() {
			public Void invoke(ResultSet rset) throws Exception {
				while (rset.next()) {
					Gift gift = load(rset);
					gifts.add(gift);
				}
				
				em.execute(delete);
				
				return null;
			}
		});
		
		return gifts;
	}
	
	public List<Gift> findByOwner(Account owner) {
		return findByOwner(owner.getId());
	}

	@Override
	public int load() throws LoadingException {
		return 0;
	}

	@Override
	public boolean loaded() {
		return true;
	}

	@Override
	public Iterator<Gift> iterator() {
		return null;
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public List<Gift> filter(Filter<Gift> arg0) {
		return null;
	}

	@Override
	public Gift find(Long arg0) {
		return null;
	}

	@Override
	public Gift find(Filter<Gift> arg0) {
		return null;
	}

	@Override
	public EntityReference<Long, Gift> getReference(Long arg0) {
		return null;
	}

}
