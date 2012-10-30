package org.shivas.core.core.castables.effects;

import org.shivas.data.entity.SpellLevel;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.core.core.castables.zones.Zone;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:57
 */
public abstract class Effect implements EffectInterface {

    protected final SpellTemplate spell;
    protected final SpellLevel spellLevel;
    protected final SpellEffectTypeEnum spellEffect;

    protected int nbTurns;
    protected Zone zone;

    protected Effect(SpellLevel spellLevel, SpellEffectTypeEnum spellEffect) {
        this.spell = spellLevel != null ? spellLevel.getSpell() : null;
        this.spellLevel = spellLevel;
        this.spellEffect = spellEffect;
    }

    @Override
    public SpellEffectTypeEnum getSpellEffect() {
        return spellEffect;
    }

    @Override
    public SpellLevel getSpellLevel() {
        return spellLevel;
    }

    @Override
    public void setNbTurns(int nbTurns) {
        this.nbTurns = nbTurns;
    }

    @Override
    public Zone getZone() {
        return zone;
    }

    @Override
    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
