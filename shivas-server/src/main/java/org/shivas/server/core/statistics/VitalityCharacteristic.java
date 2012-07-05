package org.shivas.server.core.statistics;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;

public class VitalityCharacteristic extends BaseCharacteristic {
	
	private final Statistics stats;

	public VitalityCharacteristic(Statistics stats, CharacteristicType type, short base) {
		super(type, base);
		this.stats = stats;
	}

	public VitalityCharacteristic(Statistics stats, CharacteristicType type) {
		super(type);
		this.stats = stats;
	}
	
	protected void addCurrent(short c) {
		stats.life().plus(c);
	}

	@Override
	public void equipment(short equipment) {
		int diff = equipment - this.equipment();

		super.equipment(equipment);
		addCurrent((short) diff);
	}

	@Override
	public void gift(short gift) {
		int diff = gift - this.gift();
		
		super.gift(gift);
		addCurrent((short) diff);
	}

	@Override
	public void context(short context) {
		int diff = context - this.context();
		
		super.context(context);
		addCurrent((short) diff);
	}

	@Override
	public void plusBase(short base) {
		super.plusBase(base);
		addCurrent(base);
	}

	@Override
	public void minusBase(short minus) {
		super.minusBase(minus);
		addCurrent((short) -minus);
	}

	@Override
	public void plusEquipment(short equipment) {
		super.plusEquipment(equipment);
		addCurrent(equipment);
	}

	@Override
	public void minusEquipment(short equipment) {
		super.minusEquipment(equipment);
		addCurrent((short) -equipment);
	}

	@Override
	public void plusGift(short gift) {
		super.plusGift(gift);
		addCurrent(gift);
	}

	@Override
	public void minusGift(short gift) {
		super.minusGift(gift);
		addCurrent((short) -gift);
	}

	@Override
	public void plusContext(short context) {
		super.plusContext(context);
		addCurrent(context);
	}

	@Override
	public void minusContext(short context) {
		super.minusContext(context);
		addCurrent((short) -context);
	}

}
