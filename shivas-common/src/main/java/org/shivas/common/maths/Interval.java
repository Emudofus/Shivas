package org.shivas.common.maths;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Interval implements Iterable<Integer> {
	
	public static Interval parseInterval(String s) {
		int index = s.indexOf("..");
		String left = s.substring(0, index),
			   right = s.substring(index + 2);
		return new Interval(Integer.parseInt(left), Integer.parseInt(right));
	}

	private int start, end;
	
	public Interval() {
	}

	public Interval(int start, int end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public int start() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int end() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
	public List<Integer> asList() {
		List<Integer> list = Lists.newArrayList();
		for (int i = start; i < end; ++i) {
			list.add(i);
		}
		return list;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		Interval that = (Interval) obj;
		return this.start == that.start &&
			   this.end   == that.end;
	}

	@Override
	public int hashCode() {
		return start ^ end;
	}

	@Override
	public String toString() {
		return start + ".." + end;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IntervalIterator(this);
	}
	
	private static class IntervalIterator implements Iterator<Integer> {
		
		private Interval that;
		
		private int current;

		public IntervalIterator(Interval that) {
			this.that = that;
		}

		@Override
		public boolean hasNext() {
			return current < that.end;
		}

		@Override
		public Integer next() {
			return current + 1;
		}

		@Override
		public void remove() {
		}
		
	}
	
}