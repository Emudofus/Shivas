package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/09/12
 * Time: 14:41
 */
public class NpcAnswer implements Serializable {
    private static final long serialVersionUID = 5519453510368756686L;

    private int id;
    private Collection<Action> actions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public void setActions(Collection<Action> actions) {
        this.actions = actions;
    }
}
