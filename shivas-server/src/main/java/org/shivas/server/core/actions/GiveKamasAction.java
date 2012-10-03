package org.shivas.server.core.actions;

import org.shivas.data.entity.Action;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

import java.util.Map;

public class GiveKamasAction implements Action {

	public static final String NAME = "GIVE_KAMAS";
	
	public static GiveKamasAction make(Map<String, String> parameters) {
		String raw = parameters.get("kamas");
		long kamas = Long.parseLong(raw);
		
		return new GiveKamasAction(kamas);
	}
	
	private final long kamas;

	public GiveKamasAction(long kamas) {
		this.kamas = kamas;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean able(Object target) {
		return target instanceof Player;
	}

	@Override
	public void apply(Object arg0) {
		Player target = (Player) arg0;
		GameClient client = target.getClient();
		
		target.getBag().plusKamas(kamas);
		
		client.write(target.getStats().packet());
	}

}
