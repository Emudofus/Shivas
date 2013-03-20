package org.shivas.core.core.actions;

import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import org.shivas.data.Container;
import org.shivas.data.entity.Action;
import org.shivas.data.entity.factory.ActionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ShivasActionFactory implements ActionFactory {
	
	private final Map<String, ActionMaker> makers = Maps.newHashMap();

	@Inject
	public ShivasActionFactory(final Container ctner, final Config config) {
		add(TeleportAction.NAME, new ActionMaker() {
			public Action make(Map<String, String> parameters) {
				return TeleportAction.make(parameters, ctner, config);
			}
		});

		add(GiveKamasAction.NAME, new ActionMaker() {
			public Action make(Map<String, String> parameters) {
				return GiveKamasAction.make(parameters);
			}
		});

		add(RegenLifeAction.NAME, new ActionMaker() {
			public Action make(Map<String, String> parameters) {
				return RegenLifeAction.make(parameters);
			}
		});

        add(CreateGuildAction.NAME, new ActionMaker() {
            public Action make(Map<String, String> parameters) {
                return CreateGuildAction.make(parameters);
            }
        });

        add(NewQuestionAction.NAME, new ActionMaker() {
            public Action make(Map<String, String> parameters) {
                return NewQuestionAction.make(parameters, ctner);
            }
        });

        add(RemoveItemAction.NAME, new ActionMaker() {
            public Action make(Map<String, String> parameters) {
                return RemoveItemAction.make(parameters, ctner);
            }
        });
	}
	
	public void add(String name, ActionMaker maker) {
		makers.put(name, maker);
	}

	@Override
	public Action make(String name, Map<String, String> parameters) {
		ActionMaker maker = makers.get(name);
		
		return maker != null ?
					maker.make(parameters) :
					null;
	}

}
