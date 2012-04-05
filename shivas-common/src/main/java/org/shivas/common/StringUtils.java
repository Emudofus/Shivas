package org.shivas.common;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class StringUtils {
	private StringUtils() {}
	
	private static final AtomicReference<Random> RAND = new AtomicReference<Random>(new Random(System.nanoTime()));
	
	private static final String ALPHABET = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789/*-+-_$!;:,";
	
	public static char random() {
		return ALPHABET.charAt(RAND.get().nextInt(ALPHABET.length()));
	}
	
	public static String random(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; ++i) sb.append(random());
		return sb.toString();
	}
	
	public static String toHexOr(boolean cond, int n, String def) {
		return cond ? Integer.toString(n, 16) : def;
	}
	
	public static String toHexOrNegative(int n) {
		return toHexOr(n != -1, n, "-1");
	}
}
