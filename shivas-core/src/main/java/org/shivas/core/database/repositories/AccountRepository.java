package org.shivas.core.database.repositories;

import org.atomium.EntityManager;
import org.atomium.exception.LoadingException;
import org.atomium.repository.impl.AbstractRefreshableEntityRepository;
import org.atomium.util.Filter;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.UpdateQueryBuilder;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1SaltCipher;
import org.shivas.core.config.ConfigProvider;
import org.shivas.core.core.channels.ChannelList;
import org.shivas.core.core.contacts.ContactList;
import org.shivas.core.core.gifts.GiftList;
import org.shivas.core.core.players.PlayerList;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.Account;
import org.shivas.protocol.client.enums.ChannelEnum;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class AccountRepository extends AbstractRefreshableEntityRepository<Integer, Account> {
	
	public static final String TABLE_NAME = "accounts";
	
	private final UpdateQueryBuilder saveQuery;
	private final Query loadQuery, refreshQuery, setRefreshedQuery;
	
	private final RepositoryContainer repositories;
    private final ConfigProvider config;

	@Inject
	public AccountRepository(EntityManager em, ConfigProvider config, RepositoryContainer repositories) {
		super(em, (int) config.getDuration("database.options.refresh_rate").getStandardSeconds());
        this.config = config;

        this.repositories = repositories;
		
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
			.setParameter("nb_connections", entity.getNbConnections())
			.setParameter("friend_notification_listener", entity.getContacts().isNotificationListener());
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
				result.getString("salt"),
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
		
		account.setPlayers(new PlayerList(account, repositories.players(), config));
		account.setContacts(new ContactList(account, repositories.contacts()));
		account.getContacts().setNotificationListener(result.getBoolean("friend_notification_listener"));
		account.setGifts(new GiftList(account, repositories.gifts()));
		
		if (account.hasRights() && !account.getChannels().contains(ChannelEnum.Admin)) {
			account.getChannels().add(ChannelEnum.Admin);
		}
		
		return account;
	}

	@Override
	protected void refresh(Account entity, ResultSet rset) throws SQLException {		
		entity.setPassword(rset.getString("password"));
		entity.setSalt(rset.getString("salt"));
		entity.setBanned(rset.getBoolean("banned"));
		entity.setMuted(rset.getBoolean("muted"));
		entity.setPoints(rset.getInt("points"));
		entity.setSubscriptionEnd(em.builder().dateTimeFormatter().parseDateTime(rset.getString("subscription_end")));
	}

	public Cipher passwordCipher(Account account) {
		return new Sha1SaltCipher(account.getSalt());
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
				return arg1.getNickname().equals(nickname);
			}
		});
	}

	@Override
	protected void unhandledException(Exception e) {
	}
	
}
