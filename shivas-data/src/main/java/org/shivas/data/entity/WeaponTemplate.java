package org.shivas.data.entity;

import org.shivas.data.EntityFactory;

public class WeaponTemplate extends ItemTemplate {

	private static final long serialVersionUID = -520474825772002553L;
	
	private boolean twoHands;
	private boolean ethereal;

	public WeaponTemplate(EntityFactory factory) {
		super(factory);
	}

	/**
	 * @return the twoHands
	 */
	public boolean isTwoHands() {
		return twoHands;
	}

	/**
	 * @param twoHands the twoHands to set
	 */
	public void setTwoHands(boolean twoHands) {
		this.twoHands = twoHands;
	}

	/**
	 * @return the ethereal
	 */
	public boolean isEthereal() {
		return ethereal;
	}

	/**
	 * @param ethereal the ethereal to set
	 */
	public void setEthereal(boolean ethereal) {
		this.ethereal = ethereal;
	}

}
