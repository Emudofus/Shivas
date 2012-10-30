package org.shivas.core.database.models;

import org.atomium.LazyReference;
import org.atomium.util.Entity;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.shivas.core.core.channels.ChannelList;
import org.shivas.core.core.contacts.ContactList;
import org.shivas.core.core.gifts.GiftList;
import org.shivas.core.core.players.PlayerList;
import org.shivas.core.core.stores.PlayerStore;

import java.io.Serializable;

public class Account implements Serializable, Entity<Integer> {
	
	private static final long serialVersionUID = -4407008686315825783L;

	private int id;
	private long version;
	private String name;
	private String password, salt;
	private String nickname;
	private String secretQuestion;
	private String secretAnswer;
	private boolean rights;
	private boolean banned;
	private boolean muted;
	private int community;
	private int points;
	private DateTime subscriptionEnd;
	private boolean connected;
	private ChannelList channels;
	private DateTime lastConnection;
	private String lastAddress;
	private int nbConnections;
	private PlayerList players;
	private ContactList contacts;
	private GiftList gifts;
	
	private Player currentPlayer;
    private PlayerStore currentStore;

	public Account(int id, long version, String name, String password, String salt,
			String nickname, String secretQuestion, String secretAnswer,
			boolean rights, boolean banned, boolean muted, int community, int points,
			DateTime subscriptionEnd, boolean connected, ChannelList channels,
			DateTime lastConnection, String lastAddress, int nbConnections) {
		this.id = id;
		this.version = version;
		this.name = name;
		this.password = password;
		this.salt = salt;
		this.nickname = nickname;
		this.secretQuestion = secretQuestion;
		this.secretAnswer = secretAnswer;
		this.rights = rights;
		this.banned = banned;
		this.muted = muted;
		this.community = community;
		this.points = points;
		this.subscriptionEnd = subscriptionEnd;
		this.connected = connected;
		this.channels = channels;
		this.lastConnection = lastConnection;
		this.lastAddress = lastAddress;
		this.nbConnections = nbConnections;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the secretQuestion
	 */
	public String getSecretQuestion() {
		return secretQuestion;
	}

	/**
	 * @param secretQuestion the secretQuestion to set
	 */
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	/**
	 * @return the secretAnswer
	 */
	public String getSecretAnswer() {
		return secretAnswer;
	}

	/**
	 * @param secretAnswer the secretAnswer to set
	 */
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	/**
	 * @return the rights
	 */
	public boolean hasRights() {
		return rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(boolean rights) {
		this.rights = rights;
	}

	/**
	 * @return the banned
	 */
	public boolean isBanned() {
		return banned;
	}

	/**
	 * @param banned the banned to set
	 */
	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	/**
	 * @return the muted
	 */
	public boolean isMuted() {
		return muted;
	}

	/**
	 * @param muted the muted to set
	 */
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	/**
	 * @return the community
	 */
	public int getCommunity() {
		return community;
	}

	/**
	 * @param community the community to set
	 */
	public void setCommunity(int community) {
		this.community = community;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @return the subscriptionEnd
	 */
	public DateTime getSubscriptionEnd() {
		return subscriptionEnd;
	}

	/**
	 * @param subscriptionEnd the subscriptionEnd to set
	 */
	public void setSubscriptionEnd(DateTime subscriptionEnd) {
		this.subscriptionEnd = subscriptionEnd;
	}
	
	public boolean isSubscriber() {
		return subscriptionEnd != null && subscriptionEnd.isAfterNow();
	}
	
	public Duration getRemainingSubscription() {
		return new Duration(DateTime.now(), subscriptionEnd);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connected the connected to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public ChannelList getChannels() {
		return channels;
	}

	public void setChannels(ChannelList channels) {
		this.channels = channels;
	}

	public DateTime getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(DateTime lastConnection) {
		this.lastConnection = lastConnection;
	}

	public String getLastAddress() {
		return lastAddress;
	}

	public void setLastAddress(String lastAddress) {
		this.lastAddress = lastAddress;
	}

	public int getNbConnections() {
		return nbConnections;
	}

	public void setNbConnections(int nbConnections) {
		this.nbConnections = nbConnections;
	}
	
	public void incrementNbConnections() {
		++this.nbConnections;
	}
	
	public boolean firstConnection() {
		return nbConnections <= 0;
	}

	/**
	 * @return the players
	 */
	public PlayerList getPlayers() {
		return players;
	}
	
	public void setPlayers(PlayerList players) {
		this.players = players;
	}

	public ContactList getContacts() {
		return contacts;
	}

	public void setContacts(ContactList contacts) {
		this.contacts = contacts;
	}

	public GiftList getGifts() {
		return gifts;
	}

	public void setGifts(GiftList gifts) {
		this.gifts = gifts;
	}

	/**
	 * @return the currentPlayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param currentPlayer the currentPlayer to set
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

    public PlayerStore getCurrentStore() {
        return currentStore;
    }

    public void setCurrentStore(PlayerStore currentStore) {
        this.currentStore = currentStore;
    }

    public LazyReference<Integer, Account> toReference() {
		return new LazyReference<Integer, Account>(this);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Account))
			return false;
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
