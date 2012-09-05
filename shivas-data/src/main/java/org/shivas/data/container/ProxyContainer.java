package org.shivas.data.container;

import org.shivas.data.Container;
import org.shivas.data.Repository;

public class ProxyContainer implements Container {
	
	private Container parent;

	@Override
	public <T> Repository<T> get(Class<T> entity) {
		if (parent != null) {
			return parent.get(entity);
		}
		return null;
	}

	public Container getParent() {
		return parent;
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

}
