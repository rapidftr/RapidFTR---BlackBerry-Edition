package com.rapidftr.utilities;

import java.util.Random;

public class RandomStringGenerator {
	private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static String generate(int len) {
		StringBuffer sb = new StringBuffer(len);
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int ndx = (int) (random.nextInt(ALPHA_NUM.length()) % ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}
}
