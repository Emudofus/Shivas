package org.shivas.server.core.actions;

import java.util.Map;

import org.shivas.data.entity.Action;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

public class GiveKamasAction implements Action {

	public static final int TYPE = 2;
	
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
	public int getType() {
		return TYPE;
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
