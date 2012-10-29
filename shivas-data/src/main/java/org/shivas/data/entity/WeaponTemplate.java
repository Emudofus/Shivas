package org.shivas.data.entity;

import org.shivas.data.EntityFactory;

public class WeaponTemplate extends ItemTemplate {

	private static final long serialVersionUID = -520474825772002553L;

    private short cost;
    private short minRange, maxRange;
    private short criticalRate, failureRate;
    private short criticalBonus;
    private boolean twoHands;
    private boolean ethereal;

	public WeaponTemplate(EntityFactory factory) {
		super(factory);
	}

    public short getCost() {
        return cost;
    }

    public void setCost(short cost) {
        this.cost = cost;
    }

    public short getMinRange() {
        return minRange;
    }

    public void setMinRange(short minRange) {
        this.minRange = minRange;
    }

    public short getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(short maxRange) {
        this.maxRange = maxRange;
    }

    public short getCriticalRate() {
        return criticalRate;
    }

    public void setCriticalRate(short criticalRate) {
        this.criticalRate = criticalRate;
    }

    public short getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(short failureRate) {
        this.failureRate = failureRate;
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

    public short getCriticalBonus() {
        return criticalBonus;
    }

    public void setCriticalBonus(short criticalBonus) {
        this.criticalBonus = criticalBonus;
    }
}
