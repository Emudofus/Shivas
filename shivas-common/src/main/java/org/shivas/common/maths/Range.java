package org.shivas.common.maths;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Range implements Iterable<Integer> {
	
	public static Range parseRange(String s, String split) {
		String[] args = s.split(split);
		int start, end;
		
		if (args[0].length() > 0) {
			start = Integer.parseInt(args[0]);
		} else {
			start = Integer.MIN_VALUE;
		}
		
		if (args[1].length() > 0) {
			end = Integer.parseInt(args[1]);
		} else {
			end = Integer.MAX_VALUE;
		}
		
		return new Range(start, end);
	}
	
	public static Range parseRange(String s) {
		return parseRange(s, "..");
	}

	private int start, end;
	
	public Range() {
	}

	public Range(int start, int end) {
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
	
	/**
	 * return true if integer € [start;end]
	 * @param integer
	 * @return
	 */
	public boolean contains(int integer) {
		return integer >= start && integer <= end;
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
		Range that = (Range) obj;
		return this.start == that.start &&
			   this.end   == that.end;
	}

	@Override
	public int hashCode() {
		return start ^ end;
	}
	
	public String toString(String split) {
		return (start == Integer.MIN_VALUE ? "" : String.valueOf(start)) +
			   split +
			   (end == Integer.MAX_VALUE ? "" : String.valueOf(end));
	}

	@Override
	public String toString() {
		return toString("..");
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IntervalIterator(this);
	}
	
	private static class IntervalIterator implements Iterator<Integer> {
		
		private Range that;
		
		private int current;

		public IntervalIterator(Range that) {
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
