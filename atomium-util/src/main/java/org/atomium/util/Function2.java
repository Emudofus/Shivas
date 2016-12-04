package org.atomium.util;

public interface Function2<R, A, B> {
	R invoke(A arg1, B arg2) throws Exception;
}
