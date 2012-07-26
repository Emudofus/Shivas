package org.shivas.data.entity;

import org.shivas.protocol.client.enums.ItemEffectEnum;

public interface ItemEffect {
	ItemEffectEnum getType();
	void setType(ItemEffectEnum type);
	
	ItemEffect copy();
}
