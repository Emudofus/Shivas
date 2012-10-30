package org.shivas.core.core.actions;

import org.shivas.data.entity.Action;

import java.util.Map;

public interface ActionMaker {
	Action make(Map<String, String> parameters);
}
