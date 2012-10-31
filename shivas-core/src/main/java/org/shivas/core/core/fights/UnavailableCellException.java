package org.shivas.core.core.fights;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:04
 */
public class UnavailableCellException extends FightException {
    private final FightCell cell;

    public UnavailableCellException(FightCell cell) {
        super(String.format("cell %d is unavailable", cell.getId()));
        this.cell = cell;
    }

    public FightCell getCell() {
        return cell;
    }
}
