package org.shivas.server.core.statistics;

import org.shivas.common.maths.LimitedValue;

public abstract class PodsValue implements LimitedValue {
	
	@Override
	public final int min() {
		return 0;
	}

	@Override
	public final void setCurrent(int current) { }

	@Override
	public final int plus(int p) { return p; }

	@Override
	public final int minus(int m) { return m; }

	@Override
	public final void percent(int percent) { }

	@Override
	public final int setMin() { return 0; }

	@Override
	public final int setMax() { return 0; }

}
