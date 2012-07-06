package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.common.random.Dice;
import org.shivas.protocol.client.enums.SpellEffectsEnum;

public class SpellEffect implements Serializable {

	private static final long serialVersionUID = -4195973992790568371L;
	
	private SpellLevel level;
	private SpellEffectsEnum type;
	private short first, second, third;
	private short turns, chance;
	private Dice dice;
	private String target;
	
	/**
	 * @return the level
	 */
	public SpellLevel getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(SpellLevel level) {
		this.level = level;
	}
	/**
	 * @return the type
	 */
	public SpellEffectsEnum getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SpellEffectsEnum type) {
		this.type = type;
	}
	/**
	 * @return the first
	 */
	public short getFirst() {
		return first;
	}
	/**
	 * @param first the first to set
	 */
	public void setFirst(short first) {
		this.first = first;
	}
	/**
	 * @return the second
	 */
	public short getSecond() {
		return second;
	}
	/**
	 * @param second the second to set
	 */
	public void setSecond(short second) {
		this.second = second;
	}
	/**
	 * @return the third
	 */
	public short getThird() {
		return third;
	}
	/**
	 * @param third the third to set
	 */
	public void setThird(short third) {
		this.third = third;
	}
	/**
	 * @return the turns
	 */
	public short getTurns() {
		return turns;
	}
	/**
	 * @param turns the turns to set
	 */
	public void setTurns(short turns) {
		this.turns = turns;
	}
	/**
	 * @return the chance
	 */
	public short getChance() {
		return chance;
	}
	/**
	 * @param chance the chance to set
	 */
	public void setChance(short chance) {
		this.chance = chance;
	}
	/**
	 * @return the dice
	 */
	public Dice getDice() {
		return dice;
	}
	/**
	 * @param dice the dice to set
	 */
	public void setDice(Dice dice) {
		this.dice = dice;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
