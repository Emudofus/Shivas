package org.shivas.server.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Colors implements Serializable {

	private static final long serialVersionUID = 1314592170290713909L;

	@Column(nullable=false, name="color1")
	private int first;

	@Column(nullable=false, name="color2")
	private int second;

	@Column(nullable=false, name="color3")
	private int third;
	
	public Colors() {
	}

	public Colors(int first, int second, int third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	/**
	 * @return the first
	 */
	public int first() {
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(int first) {
		this.first = first;
	}

	/**
	 * @return the second
	 */
	public int second() {
		return second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(int second) {
		this.second = second;
	}

	/**
	 * @return the third
	 */
	public int third() {
		return third;
	}

	/**
	 * @param third the third to set
	 */
	public void setThird(int third) {
		this.third = third;
	}
	
}
