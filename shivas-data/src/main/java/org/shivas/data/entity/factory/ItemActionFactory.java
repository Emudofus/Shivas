package org.shivas.data.entity.factory;

import java.util.Map;

import org.shivas.data.entity.ItemAction;

public interface ItemActionFactory {
	ItemAction make(int type, Map<String, String> parameters);
}
