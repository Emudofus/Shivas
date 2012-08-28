package org.shivas.server.core.actions;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.entity.Action;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.server.config.Config;

import com.google.common.collect.Maps;

@Singleton
public class ShivasActionFactory implements ActionFactory {
	
	private final Map<Integer, ItemActionMaker> makers = Maps.newHashMap();
	
	@Inject
	public ShivasActionFactory(final Container ctner, final Config config) {
		makers.put(TeleportAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return TeleportAction.make(parameters, ctner, config);
			}
		});
		
		makers.put(GiveKamasAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return GiveKamasAction.make(parameters);
			}
		});
		
		makers.put(RegenLifeAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return RegenLifeAction.make(parameters);
			}
		});
	}

	@Override
	public Action make(int type, Map<String, String> parameters) {		
		ItemActionMaker maker = makers.get(type);
		
		return maker != null ?
					maker.make(parameters) :
					null;
	}

}
