package org.shivas.server.core.items.actions;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.entity.ItemAction;
import org.shivas.data.entity.factory.ItemActionFactory;
import org.shivas.server.config.Config;

import com.google.common.collect.Maps;

@Singleton
public class ShivasItemActionFactory implements ItemActionFactory {
	
	private final Map<Integer, ItemActionMaker> makers = Maps.newHashMap();
	
	@Inject
	public ShivasItemActionFactory(final Container ctner, final Config config) {
		makers.put(TeleportAction.TYPE, new ItemActionMaker() {
			public ItemAction make(Map<String, String> parameters) {
				return TeleportAction.make(parameters, ctner, config);
			}
		});
		
		makers.put(GiveKamasAction.TYPE, new ItemActionMaker() {
			public ItemAction make(Map<String, String> parameters) {
				return GiveKamasAction.make(parameters);
			}
		});
	}

	@Override
	public ItemAction make(int type, Map<String, String> parameters) {		
		ItemActionMaker maker = makers.get(type);
		
		return maker != null ?
					maker.make(parameters) :
					null;
	}

}
