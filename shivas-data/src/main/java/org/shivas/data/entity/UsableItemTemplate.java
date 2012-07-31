package org.shivas.data.entity;

import java.util.Collection;

import org.shivas.data.EntityFactory;

public class UsableItemTemplate extends ItemTemplate {

	private static final long serialVersionUID = -4474357375639487479L;
	
	private Collection<ItemAction> actions;

	public UsableItemTemplate(EntityFactory factory) {
		super(factory);
	}

	public Collection<ItemAction> getActions() {
		return actions;
	}

	public void setActions(Collection<ItemAction> actions) {
		this.actions = actions;
	}

}
