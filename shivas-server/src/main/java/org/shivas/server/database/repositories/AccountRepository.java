package org.shivas.server.database.repositories;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.repository.EntityRepository;
import org.atomium.repository.impl.AbstractLiveEntityRepository;
import org.atomium.util.Filter;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;
import org.atomium.util.query.UpdateQueryBuilder;
import org.joda.time.DateTime;
import org.shivas.common.collections.Maps2;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1Cipher;
import org.shivas.server.core.ChannelList;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;
import org.shivas.server.utils.Converters;

@Singleton
public class AccountRepository extends AbstractLiveEntityRepository<Integer, Account> {
	
	public static final String TABLE_NAME = "accounts";
	
	private SelectQueryBuilder loadByIdQuery, loadByNameQuery;
	private UpdateQueryBuilder saveQuery;
	
	private EntityRepository<Integer, Player> players;

	@Inject
	public AccountRepository(EntityManager em, EntityRepository<Integer, Player> players) {
		super(em);
		
		this.players = players;
		
		this.loadByIdQuery = em.builder().select(TABLE_NAME).where("id", Op.EQ);
		this.loadByNameQuery = em.builder().select(TABLE_NAME).where("name", Op.EQ);
		this.saveQuery = em.builder()
				.update(TABLE_NAME)
				.value("rights").value("banned")
				.value("points").value("connected").value("channels")
				.where("id", Op.EQ);
	}
	
	public Cipher passwordCipher() {
		try {
			return new Sha1Cipher();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // MUST NOT HAPPEN
			return null;
		}
	}
	
	public Account find(String name) {
		Query query = loadByNameQuery.toQuery();
		query.setParameter("name", name);
		
		return find(query);
	}

	@Override
	protected Query buildLoadQuery(Integer pk) {
		Query query = loadByIdQuery.toQuery();
		query.setParameter("id", pk);
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Account entity) {
		Query query = saveQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("rights", entity.hasRights());
		query.setParameter("banned", entity.isBanned());
		query.setParameter("points", entity.getPoints());
		query.setParameter("connected", entity.isConnected());
		query.setParameter("channels", entity.getChannels().toString());
		
		return query;
	}

	@Override
	protected Account load(ResultSet result) throws SQLException {
		final int id = result.getInt("id");

		Map<Integer, Player> players = Maps2.toMap(this.players.filter(new Filter<Player>() {
			public Boolean invoke(Player arg1) throws Exception {
				return arg1.getOwnerReference().getPk() == id;
			}
		}), Converters.PLAYER_TO_ID);
		
		return new Account(
				id, 
				0, 
				result.getString("name"), 
				result.getString("password"), 
				result.getString("nickname"), 
				result.getString("question"), 
				result.getString("answer"), 
				result.getBoolean("rights"), 
				result.getBoolean("banned"), 
				result.getInt("community"), 
				result.getInt("points"), 
				new DateTime(result.getDate("subscriptionEnd")), 
				result.getBoolean("connected"),
				ChannelList.parseChannelList(result.getString("channels")),
				players
		);
	}
	
}
