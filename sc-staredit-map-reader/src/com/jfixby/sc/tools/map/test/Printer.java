package com.jfixby.sc.tools.map.test;

public class Printer {
	public static int prefix_size = 2;
	public static int split_prefix_size = 9;
	public static final String prefix = " ";
	public static final String split_prefix = " ";

	public static String prefix() {
		return prefix(prefix_size);
	}

	public static String split_prefix() {
		return prefix(split_prefix_size);
	}

	public static String prefix(int size) {
		String tmp = "";
		while (tmp.length() < size) {
			tmp = tmp + prefix;
		}
		return tmp;
	}

	public static String wrap(String tmp) {
		return wrap(tmp, prefix_size, prefix);
	}

	public static String wrap(String tmp, int size, String prefix) {
		while (tmp.length() < size) {
			tmp = prefix + tmp;
		}
		return tmp;
	}

	public static String wrap_split(String tmp) {
		return wrap(tmp, split_prefix_size, split_prefix);
	}

	public static String wrap_split_center(String string) {
		return Printer.wrap_split(string + Printer.wrap("", Printer.split_prefix_size / 3, split_prefix));
	}

}
