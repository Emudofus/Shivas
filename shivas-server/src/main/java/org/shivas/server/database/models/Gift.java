package org.shivas.server.database.models;

import java.io.Serializable;

import org.atomium.PersistableEntity;
import org.shivas.data.entity.ItemTemplate;

public class Gift implements Serializable, PersistableEntity<Long> {

	private static final long serialVersionUID = 2434371955304215872L;
	
	private long id;
	private Account owner;
	private ItemTemplate item;
	private int quantity;
	private String title, message;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public ItemTemplate getItem() {
		return item;
	}

	public void setItem(ItemTemplate item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(long id) {
		this.id = id;
	}

}
