package org.shivas.common.statistics;

public interface Characteristic {
	CharacteristicType type();
	
	short base();
	void base(short base);
	void plusBase(short base);
	void minusBase(short minus);
	
	short equipment();
	void equipment(short equipment);
	void plusEquipment(short equipment);
	void minusEquipment(short equipment);
	
	short gift();
	void gift(short gift);
	void plusGift(short gift);
	void minusGift(short gift);
	
	short context();
	void context(short context);
	void plusContext(short context);
	void minusContext(short context);
	
	short total();
	short safeTotal();
	
	Characteristic copy(Statistics copiedStats);
}
