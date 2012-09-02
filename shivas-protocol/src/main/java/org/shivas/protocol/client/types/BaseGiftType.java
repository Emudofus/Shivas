package org.shivas.protocol.client.types;

import java.util.Collection;

public class BaseGiftType {
	private long id;
	private int type;
	private String title, message, gfxUrl;
	private Collection<BaseItemType> items;
	
	public BaseGiftType() {
		// TODO Auto-generated constructor stub
	}

	public BaseGiftType(long id, int type, String title, String message,
			String gfxUrl, Collection<BaseItemType> items) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.message = message;
		this.gfxUrl = gfxUrl;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getGfxUrl() {
		return gfxUrl;
	}

	public void setGfxUrl(String gfxUrl) {
		this.gfxUrl = gfxUrl;
	}

	public Collection<BaseItemType> getItems() {
		return items;
	}

	public void setItems(Collection<BaseItemType> items) {
		this.items = items;
	}
}
