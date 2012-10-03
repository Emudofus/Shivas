package org.shivas.server.core.actions;

import com.google.common.collect.Maps;
import org.shivas.data.Container;
import org.shivas.data.entity.Action;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.server.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ShivasActionFactory implements ActionFactory {
	
	private final Map<Integer, ItemActionMaker> makers = Maps.newHashMap();

	@Inject
	public ShivasActionFactory(final Container ctner, final Config config) {
		add(TeleportAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return TeleportAction.make(parameters, ctner, config);
			}
		});

		add(GiveKamasAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return GiveKamasAction.make(parameters);
			}
		});

		add(RegenLifeAction.TYPE, new ItemActionMaker() {
			public Action make(Map<String, String> parameters) {
				return RegenLifeAction.make(parameters);
			}
		});

        add(CreateGuildAction.TYPE, new ItemActionMaker() {
            public Action make(Map<String, String> parameters) {
                return CreateGuildAction.make(parameters);
            }
        });

        add(NewQuestionAction.TYPE, new ItemActionMaker() {
            public Action make(Map<String, String> parameters) {
                return NewQuestionAction.make(parameters, ctner);
            }
        });

        add(RemoveItemAction.TYPE, new ItemActionMaker() {
            public Action make(Map<String, String> parameters) {
                return RemoveItemAction.make(parameters, ctner);
            }
        });
	}
	
	public void add(int type, ItemActionMaker maker) {
		makers.put(type, maker);
	}

	@Override
	public Action make(int type, Map<String, String> parameters) {		
		ItemActionMaker maker = makers.get(type);
		
		return maker != null ?
					maker.make(parameters) :
					null;
	}

}
