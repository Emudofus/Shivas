package org.shivas.data.loader;

import org.shivas.common.StringUtils;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.GameCell;

public class CellLoader {
	private CellLoader() {}
	
	public static GameCell[] parse(String string, EntityFactory factory) {
		GameCell[] cells = new GameCell[string.length() / 10];
		for (int i = 0; i < string.length(); i += 10) {
			GameCell cell = factory.newGameCell();
			cell.setId((short) (i / 10));
			initCell(cell, string.substring(i, i + 10));
			cells[i / 10] = cell;
		}
		return cells;
	}
	
	public static void initCell(GameCell cell, String string) {
        int[] hashCodes = new int[10];
        for (int i = 0; i < 10; ++i){
            hashCodes[i] = StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(i));
        }

        boolean los = (hashCodes[0] & 1) == 1;
        int groundLevel = hashCodes[1] & 15;
        int movementType = (hashCodes[2] & 56) >> 3;
        int groundSlope = (hashCodes[4] & 60) >> 2;
		
        cell.setLineOfSight(los);
        cell.setGroundLevel(groundLevel);
        cell.setMovementType(GameCell.MovementType.valueOf(movementType));
        cell.setGroundSlope(groundSlope);
	}
}
