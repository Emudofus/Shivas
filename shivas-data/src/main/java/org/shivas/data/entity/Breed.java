package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.protocol.client.enums.Gender;

public class Breed implements Serializable {

	private static final long serialVersionUID = 7657680626426281371L;
	
	private int id;
	private short startActionPoints;
	private short startMovementPoints;
	private short startLife;
	private short startProspection;

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
	
	public short getDefaultSkin(Gender gender) {
		return (short) (10 * id + gender.ordinal());
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
