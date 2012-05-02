package org.shivas.server.database.models;

import java.io.Serializable;
import java.util.List;

import org.atomium.Entity;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Account implements Serializable, Entity<Integer> {
	
	private static final long serialVersionUID = -4407008686315825783L;

	private int id;
	private long version;
	private String name;
	private String password;
	private String nickname;
	private String secretQuestion;
	private String secretAnswer;
	private boolean rights;
	private boolean banned;
	private int community;
	private int points;
	private DateTime subscriptionEnd;
	private boolean connected;
	private List<Player> players;

	/**
	 * @return the id
	 */
	public Integer id() {
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

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
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
