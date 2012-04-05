package org.shivas.protocol.client.types;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/02/12
 * Time: 18:43
 */
public class BaseItemTemplateType {
    private int id;
    private Collection<BaseItemTemplateEffectType> effects;

    public BaseItemTemplateType() {
    }

    public BaseItemTemplateType(int id, Collection<BaseItemTemplateEffectType> effects) {
        this.id = id;
        this.effects = effects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<BaseItemTemplateEffectType> getEffects() {
        return effects;
    }

    public void setEffects(Collection<BaseItemTemplateEffectType> effects) {
        this.effects = effects;
    }
}
