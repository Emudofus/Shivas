package org.shivas.login.database.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="servers")
public class GameServer implements Serializable {
	
	private static final long serialVersionUID = 5186355514047695714L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private int id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@Column(nullable=false)
	private String address;
	
	@Column(nullable=false)
	private int port;
	
	@Column(nullable=false)
	private String systemAddress;
	
	@Column(nullable=false)
	private int systemPort;
	
	@Column(nullable=false)
	private int community;
	
	@Column(nullable=false)
	private boolean restricted;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the systemAddress
	 */
	public String getSystemAddress() {
		return systemAddress;
	}

	/**
	 * @param systemAddress the systemAddress to set
	 */
	public void setSystemAddress(String systemAddress) {
		this.systemAddress = systemAddress;
	}

	/**
	 * @return the systemPort
	 */
	public int getSystemPort() {
		return systemPort;
	}

	/**
	 * @param systemPort the systemPort to set
	 */
	public void setSystemPort(int systemPort) {
		this.systemPort = systemPort;
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
	 * @return the restricted
	 */
	public boolean isRestricted() {
		return restricted;
	}

	/**
	 * @param restricted the restricted to set
	 */
	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
	
}
