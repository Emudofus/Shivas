package org.shivas.core.core.npcs;

import org.shivas.core.core.GameActorWithoutId;
import org.shivas.core.core.Location;
import org.shivas.core.core.Look;
import org.shivas.core.core.maps.GameMap;
import org.shivas.data.entity.MapTemplate;
import org.shivas.data.entity.Npc;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayNpcType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/09/12
 * Time: 20:16
 */
public class GameNpc extends Npc implements GameActorWithoutId {
    private static final long serialVersionUID = -7033230674589870070L;

    private int publicId;
    private Location location;
    private Look look;

    public GameNpc() {
        location = new Location(
                (GameMap) getMap(),
                getCell(),
                getOrientation()
        );
        look = new NpcLook(this);
    }

    @Override
    public int getPublicId() {
        return publicId;
    }

    @Override
    public void setId(Integer id) {
        this.publicId = id;
    }

    @Override
    public String getName() {
        return String.format("NPC%d", getId());
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Look getLook() {
        return look;
    }

    @Override
    public void teleport(GameMap map, short cell) {
        // TODO teleport a NPC
    }

    @Override
    public void setMap(MapTemplate map) {
        if (getMap() != null) {
            ((GameMap) getMap()).leave(this);
        }

        super.setMap(map);
        ((GameMap) getMap()).enter(this);
    }

    public RolePlayNpcType toRolePlayNpcType() {
        return new RolePlayNpcType(
                publicId,
                getTemplate().getId(),
                look.size(),
                look.size(),
                look.skin(),
                getTemplate().getGender(),
                look.colors().first(),
                look.colors().second(),
                look.colors().third(),
                getCell(),
                getOrientation(),
                look.accessories(),
                getTemplate().getCustomArtwork(),
                getTemplate().getExtraClip()
        );
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        return toRolePlayNpcType();
    }
}
