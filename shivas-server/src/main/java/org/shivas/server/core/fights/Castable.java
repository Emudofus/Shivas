package org.shivas.server.core.fights;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public interface Castable {
    void cast(Fighter caster) throws FightException;
}
