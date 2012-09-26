package org.shivas.server.core.guilds;

import org.shivas.protocol.client.enums.GuildMemberRightsEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 12:25
 */
public class GuildMemberRights {
    private int rights;

    public GuildMemberRights() { }

    public GuildMemberRights(int rights) {
        this.rights = rights;
    }

    public GuildMemberRights fill(boolean all){
        rights = all ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        return this;
    }

    public boolean get(GuildMemberRightsEnum right){
        return right != null && (rights & right.value()) != 0;
    }

    public void set(GuildMemberRightsEnum right, boolean value){
        if (right != null){
            if (value){
                rights |= right.value();
            }
            else{
                rights ^= right.value();
            }
        }
    }

    public int toInt(){
        return rights;
    }
}
