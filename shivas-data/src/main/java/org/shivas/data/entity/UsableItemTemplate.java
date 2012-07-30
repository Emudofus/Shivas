package org.shivas.data.entity;

import java.util.Map;

import org.shivas.data.EntityFactory;

public class UsableItemTemplate extends ItemTemplate {

	private static final long serialVersionUID = -4474357375639487479L;
	
	private Map<Integer, ItemAction> actions;

	public UsableItemTemplate(EntityFactory factory) {
		super(factory);
	}

	public Map<Integer, ItemAction> getActions() {
		return actions;
	}

	public void setActions(Map<Integer, ItemAction> actions) {
		this.actions = actions;
	}

}
