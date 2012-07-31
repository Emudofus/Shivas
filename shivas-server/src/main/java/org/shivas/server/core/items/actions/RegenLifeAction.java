package org.shivas.server.core.items.actions;

import java.util.Map;

import org.shivas.data.entity.ItemAction;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

public class RegenLifeAction implements ItemAction {
	
	public static final int TYPE = 3;
	
	public static RegenLifeAction make(Map<String, String> parameters) {
		String raw = parameters.get("life");
		int life = Integer.parseInt(raw);
		
		return new RegenLifeAction(life);
	}
	
	private final int life;

	public RegenLifeAction(int life) {
		this.life = life;
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
		
		target.getStats().life().plus(life);
		client.service().repositories().players().saveLater(target);
		
		client.write(target.getStats().packet());
	}

}
