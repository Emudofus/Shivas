package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.WorldStateEnum;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 10:27
 * IDE : IntelliJ IDEA
 */
public class GameServerType {
    private int id;
    private String address;
    private int connexionPort;
    private WorldStateEnum state;
    private int completion;

    public GameServerType() {

    }

    public GameServerType(int id, String address, int connexionPort, WorldStateEnum state, int completion) {
        this.id = id;
        this.address = address;
        this.connexionPort = connexionPort;
        this.state = state;
        this.completion = completion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnexionPort() {
        return connexionPort;
    }

    public void setConnexionPort(int connexionPort) {
        this.connexionPort = connexionPort;
    }

    public WorldStateEnum getState() {
        return state;
    }

    public void setState(WorldStateEnum state) {
        this.state = state;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }
}
