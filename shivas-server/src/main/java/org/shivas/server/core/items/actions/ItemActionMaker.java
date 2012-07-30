package org.shivas.server.core.items.actions;

import java.util.Map;

import org.shivas.data.entity.ItemAction;

public interface ItemActionMaker {
	ItemAction make(Map<String, String> parameters);
}
