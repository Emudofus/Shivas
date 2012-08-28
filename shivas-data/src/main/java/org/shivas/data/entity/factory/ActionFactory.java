package org.shivas.data.entity.factory;

import java.util.Map;

import org.shivas.data.entity.Action;

public interface ActionFactory {
	Action make(int type, Map<String, String> parameters);
}
