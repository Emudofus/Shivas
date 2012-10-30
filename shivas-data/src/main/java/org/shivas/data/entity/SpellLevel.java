package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Collection;

public class SpellLevel implements Serializable {

	private static final long serialVersionUID = -1176720432349429500L;

	private byte id;
	private SpellTemplate spell;
	private byte costAP;
	private byte minRange, maxRange;
	private short criticalRate, failureRate;
	private boolean inline, los, emptyCell, adjustableRange, endsTurnOnFailure;
	private byte maxPerTurn, maxPerPlayer, turns;
	private Collection<SpellEffect> effects;
	private Collection<SpellEffect> criticalEffects;
	
	/**
	 * @return the id
	 */
	public byte getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(byte id) {
		this.id = id;
	}
	/**
	 * @return the spell
	 */
	public SpellTemplate getSpell() {
		return spell;
	}
	/**
	 * @param spell the spell to set
	 */
	public void setSpell(SpellTemplate spell) {
		this.spell = spell;
	}
	/**
	 * @return the costAP
	 */
	public byte getCostAP() {
		return costAP;
	}
	/**
	 * @param costAP the costAP to set
	 */
	public void setCostAP(byte costAP) {
		this.costAP = costAP;
	}
	/**
	 * @return the minRange
	 */
	public byte getMinRange() {
		return minRange;
	}
	/**
	 * @param minRange the minRange to set
	 */
	public void setMinRange(byte minRange) {
		this.minRange = minRange;
	}
	/**
	 * @return the maxRange
	 */
	public byte getMaxRange() {
		return maxRange;
	}
	/**
	 * @param maxRange the maxRange to set
	 */
	public void setMaxRange(byte maxRange) {
		this.maxRange = maxRange;
	}
	/**
	 * @return the criticalRate
	 */
	public short getCriticalRate() {
		return criticalRate;
	}
	/**
	 * @param criticalRate the criticalRate to set
	 */
	public void setCriticalRate(short criticalRate) {
		this.criticalRate = criticalRate;
	}
	/**
	 * @return the failureRate
	 */
	public short getFailureRate() {
		return failureRate;
	}
	/**
	 * @param failureRate the failureRate to set
	 */
	public void setFailureRate(short failureRate) {
		this.failureRate = failureRate;
	}
	/**
	 * @return the inline
	 */
	public boolean isInline() {
		return inline;
	}
	/**
	 * @param inline the inline to set
	 */
	public void setInline(boolean inline) {
		this.inline = inline;
	}
	/**
	 * @return the los
	 */
	public boolean isLos() {
		return los;
	}
	/**
	 * @param los the lov to set
	 */
	public void setLos(boolean los) {
		this.los = los;
	}
	/**
	 * @return the emptyCell
	 */
	public boolean isEmptyCell() {
		return emptyCell;
	}
	/**
	 * @param emptyCell the emptyCell to set
	 */
	public void setEmptyCell(boolean emptyCell) {
		this.emptyCell = emptyCell;
	}
	/**
	 * @return the adjustableRange
	 */
	public boolean isAdjustableRange() {
		return adjustableRange;
	}
	/**
	 * @param adjustableRange the adjustableRange to set
	 */
	public void setAdjustableRange(boolean adjustableRange) {
		this.adjustableRange = adjustableRange;
	}
	/**
	 * @return the endsTurnOnFailure
	 */
	public boolean isEndsTurnOnFailure() {
		return endsTurnOnFailure;
	}
	/**
	 * @param endsTurnOnFailure the endsTurnOnFailure to set
	 */
	public void setEndsTurnOnFailure(boolean endsTurnOnFailure) {
		this.endsTurnOnFailure = endsTurnOnFailure;
	}
	/**
	 * @return the maxPerTurn
	 */
	public byte getMaxPerTurn() {
		return maxPerTurn;
	}
	/**
	 * @param maxPerTurn the maxPerTurn to set
	 */
	public void setMaxPerTurn(byte maxPerTurn) {
		this.maxPerTurn = maxPerTurn;
	}
	/**
	 * @return the maxPerPlayer
	 */
	public byte getMaxPerPlayer() {
		return maxPerPlayer;
	}
	/**
	 * @param maxPerPlayer the maxPerPlayer to set
	 */
	public void setMaxPerPlayer(byte maxPerPlayer) {
		this.maxPerPlayer = maxPerPlayer;
	}
	/**
	 * @return the turns
	 */
	public byte getTurns() {
		return turns;
	}
	/**
	 * @param turns the turns to set
	 */
	public void setTurns(byte turns) {
		this.turns = turns;
	}
	/**
	 * @return the effects
	 */
	public Collection<SpellEffect> getEffects() {
		return effects;
	}
	/**
	 * @param effects the effects to set
	 */
	public void setEffects(Collection<SpellEffect> effects) {
		this.effects = effects;
	}
	/**
	 * @return the criticalEffects
	 */
	public Collection<SpellEffect> getCriticalEffects() {
		return criticalEffects;
	}
	/**
	 * @param criticalEffects the criticalEffects to set
	 */
	public void setCriticalEffects(Collection<SpellEffect> criticalEffects) {
		this.criticalEffects = criticalEffects;
	}
}
