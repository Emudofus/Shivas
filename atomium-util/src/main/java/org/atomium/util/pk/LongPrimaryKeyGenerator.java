package org.atomium.util.pk;

public class LongPrimaryKeyGenerator implements PrimaryKeyGenerator<Long> {

	private Long next = 0L;
	
	public void setMax(Long pk) {
		if (next < pk)
			next = pk;
	}

	public Long next() {
		return ++next;
	}

}
