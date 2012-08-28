package org.shivas.data.entity;

import java.util.Collection;

import org.shivas.data.EntityFactory;

public class UsableItemTemplate extends ItemTemplate {

	private static final long serialVersionUID = -4474357375639487479L;
	
	private Collection<Action> actions;

	public UsableItemTemplate(EntityFactory factory) {
		super(factory);
	}

	public Collection<Action> getActions() {
		return actions;
	}

	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}

}
