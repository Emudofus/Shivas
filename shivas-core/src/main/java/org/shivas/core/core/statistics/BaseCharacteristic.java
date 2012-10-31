package org.shivas.core.core.statistics;

import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;

public class BaseCharacteristic implements Characteristic {
	
	private CharacteristicType type;
	
	private short base, equipment, gift, context;

	public BaseCharacteristic(CharacteristicType type) {
		this.type = type;
	}

	public BaseCharacteristic(CharacteristicType type, short base) {
		this.type = type;
		this.base = base;
	}

	public BaseCharacteristic(CharacteristicType type, short base, short equipment, short gift, short context) {
		this.type = type;
		this.base = base;
		this.equipment = equipment;
		this.gift = gift;
		this.context = context;
	}

	@Override
	public CharacteristicType type() {
		return type;
	}

	@Override
	public short base() {
		return base;
	}

	@Override
	public void base(short base) {
		this.base = base;
	}

	@Override
	public void plusBase(short base) {
		this.base += base;
	}

	@Override
	public void minusBase(short minus) {
		this.base -= minus;
	}

	@Override
	public short equipment() {
		return equipment;
	}

	@Override
	public void equipment(short equipment) {
		this.equipment = equipment;
	}

	@Override
	public void plusEquipment(short equipment) {
		this.equipment += equipment;
	}

	@Override
	public void minusEquipment(short equipment) {
		this.equipment -= equipment;
	}

	@Override
	public short gift() {
		return gift;
	}

	@Override
	public void gift(short gift) {
		this.gift = gift;
	}

	@Override
	public void plusGift(short gift) {
		this.gift += gift;
	}

	@Override
	public void minusGift(short gift) {
		this.gift -= gift;
	}

	@Override
	public short context() {
		return context;
	}

	@Override
	public void context(short context) {
		this.context = context;
	}

	@Override
	public void plusContext(short context) {
		this.context += context;
	}

	@Override
	public void minusContext(short context) {
		this.context -= context;
	}

	@Override
	public short total() {
		return (short) (base + equipment + gift + context);
	}

	@Override
	public short safeTotal() {
		short total = total();
		return total > 0 ? total : 0;
	}

	@Override
	public Characteristic copy(Statistics copiedStats) {
		return new BaseCharacteristic(type, base, equipment, gift, context);
	}

}
