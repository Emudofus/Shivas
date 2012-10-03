package org.shivas.data.entity.factory;

import org.shivas.data.entity.Action;

import java.util.Map;

public interface ActionFactory {
	Action make(String name, Map<String, String> parameters);
}
