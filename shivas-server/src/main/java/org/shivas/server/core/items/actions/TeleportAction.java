package org.shivas.server.core.items.actions;

import java.util.Map;

import org.shivas.data.Container;
import org.shivas.data.entity.ItemAction;
import org.shivas.server.config.Config;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.maps.GameMap;

public class TeleportAction implements ItemAction {

	public static final int TYPE = 1;
	
	public static TeleportAction make(Map<String, String> parameters, Container ctner, Config config) {		
		String mapRaw  = parameters.get("map"),
			   cellRaw = parameters.get("cell");
		
		GameMap map = mapRaw.equalsIgnoreCase("start") ? 
				config.startMap() : 
				ctner.get(GameMap.class).byId(Integer.parseInt(mapRaw));
		
		short cell = mapRaw.equalsIgnoreCase("start") ? 
				config.startCell() : 
				Short.parseShort(cellRaw);
		
		return new TeleportAction(map, cell);
	}
	
	private final GameMap map;
	private final short cell;

	public TeleportAction(GameMap map, short cell) {
		this.map = map;
		this.cell = cell;
	}

	@Override
	public int getType() {
		return TYPE;
	}

	@Override
	public boolean able(Object target) {
		return target instanceof GameActor;
	}

	@Override
	public void apply(Object arg1) {
		GameActor target = (GameActor) arg1;
		
		target.teleport(map, cell);
	}

}
