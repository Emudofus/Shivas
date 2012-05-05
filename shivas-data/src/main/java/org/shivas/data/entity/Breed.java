package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Map;

import org.shivas.common.maths.Range;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.protocol.client.enums.Gender;

public class Breed implements Serializable {
	
	public static class Level implements Serializable {
		private static final long serialVersionUID = 70309319798790462L;
		
		private int cost, bonus;

		public Level(int cost, int bonus) {
			this.cost = cost;
			this.bonus = bonus;
		}

		public int cost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int bonus() {
			return bonus;
		}

		public void setBonus(int bonus) {
			this.bonus = bonus;
		}
	}

	private static final long serialVersionUID = 7657680626426281371L;
	
	private int id;
	private short startActionPoints;
	private short startMovementPoints;
	private short startLife;
	private short startProspection;
	private Map<CharacteristicType, Map<Range, Level>> levels;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public short getStartActionPoints() {
		return startActionPoints;
	}

	public void setStartActionPoints(short startActionPoints) {
		this.startActionPoints = startActionPoints;
	}

	public short getStartMovementPoints() {
		return startMovementPoints;
	}

	public void setStartMovementPoints(short startMovementPoints) {
		this.startMovementPoints = startMovementPoints;
	}

	public short getStartLife() {
		return startLife;
	}

	public void setStartLife(short startLife) {
		this.startLife = startLife;
	}

	public short getStartProspection() {
		return startProspection;
	}

	public void setStartProspection(short startProspection) {
		this.startProspection = startProspection;
	}
	
	public Map<CharacteristicType, Map<Range, Level>> getLevels() {
		return levels;
	}

	public void setLevels(Map<CharacteristicType, Map<Range, Level>> levels) {
		this.levels = levels;
	}

	public short getDefaultSkin(Gender gender) {
		return (short) (10 * id + gender.ordinal());
	}
	
	public Level getLevel(CharacteristicType type, int stat) {
		Map<Range, Level> levels = this.levels.get(type);
		
		if (levels != null) {
			for (Map.Entry<Range, Level> level : levels.entrySet()) {
				if (level.getKey().contains(stat)) {
					return level.getValue();
				}
			}
		}
		
		return null;
	}
	
	public Level getLevel(Characteristic characteristic) {
		return getLevel(characteristic.type(), characteristic.base());
	}
	
	public String toShortString() {
		return toString().substring(0, 4);
	}

	public String toString(){
        switch (id){
    		case 1:
                return "Féca";

    		case 2:
                return "Osamodas";

    		case 3:
                return "Enutrof";

    		case 4:
                return "Sram";

    		case 5:
    			return "Xélor";

    		case 6:
    			return "Ecaflip";

    		case 7:
                return "Eniripsa";

    		case 8:
    			return "Iop";

    		case 9:
    			return "Crâ";

    		case 10:
    			return "Sadida";

    		case 11:
    			return "Sacrieur";

    		case 12:
    			return "Pandawa";

            default:
                return "UNKNOWN";
    	}
    }

}
