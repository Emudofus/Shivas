package org.shivas.server.core.items;

import org.shivas.server.services.game.GameClient;

public class ExchangeBag extends SimpleBag {
	private final GameClient owner;
	
	private long kamas;
	private boolean ready;
	
	public ExchangeBag(GameClient owner) {
		this.owner = owner;
	}

	public GameClient getOwner() {
		return owner;
	}

	public long getKamas() {
		return kamas;
	}
	
	public void setKamas(long kamas) {
		this.kamas = kamas;
	}
	
	public ExchangeBag plusKamas(long kamas) {
		this.kamas += kamas;
		return this;
	}
	
	public ExchangeBag minusKamas(long kamas) {
		this.kamas -= kamas;
		return this;
	}
	
	public boolean hasEnoughKamas() {
		return (owner.player().getBag().getKamas() - kamas) >= 0;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
