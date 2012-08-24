package org.shivas.server.database.repositories;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.exception.LoadingException;
import org.atomium.repository.EntityRepository;
import org.atomium.repository.impl.AbstractRefreshableEntityRepository;
import org.atomium.util.Filter;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.UpdateQueryBuilder;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1Cipher;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.channels.ChannelList;
import org.shivas.server.core.players.PlayerList;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;

@Singleton
public class AccountRepository extends AbstractRefreshableEntityRepository<Integer, Account> {
	
	public static final String TABLE_NAME = "accounts";
	
	private final UpdateQueryBuilder saveQuery;
	private final Query loadQuery, refreshQuery, setRefreshedQuery;
	
	private final EntityRepository<Integer, Player> players;

	@Inject
	public AccountRepository(EntityManager em, Config config, EntityRepository<Integer, Player> players) {
		super(em, config.databaseRefreshRate());
		
		this.players = players;
		
		this.saveQuery = em.builder()
				.update(TABLE_NAME)
				.value("rights").value("banned").value("muted")
				.value("points").value("connected").value("channels")
				.value("last_connection").value("last_address").value("nb_connections")
				.where("id", Op.EQ);
		this.setRefreshedQuery = em.builder()
				.update(TABLE_NAME)
				.value("refreshed", false)
				.toQuery();
		this.loadQuery = em.builder()
				.select(TABLE_NAME)
				.toQuery();
		this.refreshQuery = em.builder()
				.select(TABLE_NAME)
				.where("refreshed", Op.EQ, true)
				.toQuery();
	}

	@Override
	protected Query buildLoadQuery() {
		return loadQuery;
	}

	@Override
	protected Query getRefreshQuery() {
		return refreshQuery;
	}

	@Override
	protected Query getSetRefreshedQuery() {
		return setRefreshedQuery;
	}

	@Override
	protected Query buildSaveQuery(Account entity) {
		return saveQuery.toQuery()
			.setParameter("id", entity.getId())
			.setParameter("rights", entity.hasRights())
			.setParameter("banned", entity.isBanned())
			.setParameter("muted", entity.isMuted())
			.setParameter("points", entity.getPoints())
			.setParameter("connected", entity.isConnected())
			.setParameter("channels", entity.getChannels())
			.setParameter("last_connection", entity.getLastConnection())
			.setParameter("last_address", entity.getLastAddress())
			.setParameter("nb_connections", entity.getNbConnections());
	}
	
	@Override
	public int load() throws LoadingException {
		em.execute(em.builder().update(TABLE_NAME).value("connected", false).toQuery());
		
		return super.load();
	}

	@Override
	protected Integer getPrimaryKey(ResultSet rset) throws SQLException {
		return rset.getInt("id");
	}

	@Override
	protected Account load(ResultSet result) throws SQLException {
		int id = result.getInt("id");
		
		Account account = new Account(
				id,
				0,
				result.getString("name"),
				result.getString("password"),
				result.getString("nickname"),
				result.getString("question"),
				result.getString("answer"),
				result.getBoolean("rights"),
				result.getBoolean("banned"),
				result.getBoolean("muted"),
				result.getInt("community"),
				result.getInt("points"),
				em.builder().dateTimeFormatter().parseDateTime(result.getString("subscription_end")),
				result.getBoolean("connected"),
				ChannelList.parseChannelList(result.getString("channels")),
				em.builder().dateTimeFormatter().parseDateTime(result.getString("last_connection")),
				result.getString("last_address"),
				result.getInt("nb_connections")
		);
		
		account.setPlayers(new PlayerList(account, players));
		
		if (account.hasRights() && !account.getChannels().contains(ChannelEnum.Admin)) {
			account.getChannels().add(ChannelEnum.Admin);
		}
		
		return account;
	}

	@Override
	protected void refresh(Account entity, ResultSet rset) throws SQLException {		
		entity.setPassword(rset.getString("password"));
		entity.setBanned(rset.getBoolean("banned"));
		entity.setMuted(rset.getBoolean("muted"));
		entity.setPoints(rset.getInt("points"));
		entity.setSubscriptionEnd(em.builder().dateTimeFormatter().parseDateTime(rset.getString("subscription_end")));
	}

	public Cipher passwordCipher() {
		try {
			return new Sha1Cipher();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // MUST NOT HAPPEN
			return null;
		}
	}
	
	public Account find(final String name) {
		return super.find(new Filter<Account>() {
			public Boolean invoke(Account arg1) throws Exception {
				return arg1.getName().equals(name);
			}
		});
	}
	
	public Account findByNickname(final String nickname) {
		return super.find(new Filter<Account>() {
			public Boolean invoke(Account arg1) throws Exception {
				return arg1.getName().equals(nickname);
			}
		});
	}

	@Override
	protected void unhandledException(Exception e) {
	}
	
}
