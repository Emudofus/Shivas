package org.shivas.common.statistics;

public class BasicCharacteristic implements Characteristic {
	
	private CharacteristicType type;
	
	private short base, equipment, gift, context;

	public CharacteristicType type() {
		return type;
	}

	public short base() {
		return base;
	}

	public void base(short base) {
		this.base = base;
	}

	public void plusBase(short base) {
		this.base += base;
	}

	public void minusBase(short minus) {
		this.base -= minus;
	}

	public short equipment() {
		return equipment;
	}

	public void equipment(short equipment) {
		this.equipment = equipment;
	}

	public void plusEquipment(short equipment) {
		this.equipment += equipment;
	}

	public void minusEquipment(short equipment) {
		this.equipment -= equipment;
	}

	public short gift() {
		return gift;
	}

	public void gift(short gift) {
		this.gift = gift;
	}

	public void plusGift(short gift) {
		this.gift += gift;
	}

	public void minusGift(short gift) {
		this.gift -= gift;
	}

	public short context() {
		return context;
	}

	public void context(short context) {
		this.context = context;
	}

	public void plusContext(short context) {
		this.context += context;
	}

	public void minusContext(short context) {
		this.context -= context;
	}

	public short total() {
		return (short) (base + equipment + gift + context);
	}

	public short safeTotal() {
		short total = total();
		return total >= 0 ? total : 0;
	}

}
