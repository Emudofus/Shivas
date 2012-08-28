package org.shivas.server.core.actions;

import java.util.Map;

import org.shivas.data.entity.Action;

public interface ItemActionMaker {
	Action make(Map<String, String> parameters);
}
