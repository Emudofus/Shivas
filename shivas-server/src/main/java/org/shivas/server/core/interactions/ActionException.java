package org.shivas.server.core.interactions;

public class ActionException extends Exception {

	private static final long serialVersionUID = -2019222601772278284L;

	public ActionException() {
		super();
	}

	public ActionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ActionException(String arg0) {
		super(arg0);
	}

	public ActionException(Throwable arg0) {
		super(arg0);
	}

}
