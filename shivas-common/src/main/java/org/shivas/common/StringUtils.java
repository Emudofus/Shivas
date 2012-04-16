package org.shivas.common;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class StringUtils {
	private StringUtils() {}
	
	private static final AtomicReference<Random> RAND = new AtomicReference<Random>(new Random(System.nanoTime()));
	
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	public static final String EXTENDED_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
	
	public static final String VOWELS = "aeiouy";
	public static final String CONSONANTS = "bcdfghjkmnpqrstvwxz";
	
	public static char random() {
		return ALPHABET.charAt(RAND.get().nextInt(ALPHABET.length()));
	}
	
	public static char randomVowels() {
		return VOWELS.charAt(RAND.get().nextInt(VOWELS.length()));
	}
	
	public static char randomConsonants() {
		return CONSONANTS.charAt(RAND.get().nextInt(CONSONANTS.length()));
	}
	
	public static String random(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; ++i) sb.append(random());
		return sb.toString();
	}
	
	public static String randomPseudo() {
		boolean vowels = RAND.get().nextBoolean();
		int length = RAND.get().nextInt(4) + 4;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			if (vowels) sb.append(randomVowels());
			else sb.append(randomConsonants());
			vowels = RAND.get().nextBoolean();
		}
		return capitalize(sb);
	}
	
	public static String capitalize(StringBuilder sb) {
		return Character.toUpperCase(sb.charAt(0)) + sb.substring(1);
	}
	
	public static String toHexOr(boolean cond, int n, String def) {
		return cond ? Integer.toString(n, 16) : def;
	}
	
	public static String toHexOrNegative(int n) {
		return toHexOr(n != -1, n, "-1");
	}
}
