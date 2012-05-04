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
	public final void plus(int p) { }

	@Override
	public final void minus(int m) { }

	@Override
	public final void percent(int percent) { }

	@Override
	public final void setMin() { }

	@Override
	public final void setMax() { }

}
