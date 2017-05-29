package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.util.JUtils;

public class Mask implements Comparable<Mask> {

	private short value;
	private String mask_string;
	private short original;

	static final int mask_size = 8;

	public Mask(short b) {
		// this.value = (short) (b & Integer.parseInt("0000111111110000", 2));
		original = b;
		this.value = (short) (b >> 4);
		// mask_string = maskBinStringOf(value);
		mask_string = Integer.toHexString(value);

	}

	public Mask(String mask_string) {
		this.mask_string = mask_string;
		// value = valueOfBinString(mask_string);
		if (mask_string.length() > 4) {
			value = valueOfBinString(mask_string);
		} else {
			value = (short) Integer.parseInt(mask_string, 16);
		}
	}

	public String getVarName() {
		return JUtils.split(mask_string, "\\.").getLast();
	}

	private short valueOfBinString(String mask_string) {
		String tmp = mask_string.replaceAll("\\.", "");
		short tm_value = (short) Integer.parseInt(tmp, 2);
		if (!maskBinStringOf(tm_value).equals(mask_string)) {
			Err.reportError("Failed to parse: " + mask_string);
		}
		return tm_value;
	}

	private String maskBinStringOf(short b) {
		return maskStringOf(b, true);
	}

	private String maskStringOf(short b, boolean use_dots) {
		String tmp = Integer.toBinaryString(b);
		while (tmp.length() < mask_size) {
			tmp = "0" + tmp;
		}

		if (!use_dots) {
			return tmp;
		}
		String out = "";

		for (int i = 0; i < tmp.length(); i++) {
			if (i > 0 && i % 4 == 0) {
				out = out + ".";
			}
			out = out + tmp.charAt(i);
		}

		return out;
	}

	private String maskStringOf(byte b) {
		String tmp = Integer.toBinaryString(b);
		while (tmp.length() < 8) {
			tmp = "0" + tmp;
		}
		String out = "";
		for (int i = 0; i < tmp.length(); i++) {
			if (i > 0 && i % 4 == 0) {
				out = out + ".";
			}
			out = out + tmp.charAt(i);
		}

		return out;
	}

	@Override
	public int compareTo(Mask o) {
		return Integer.compare(value, o.value);
	}

	public String toChar() {
		return Printer.wrap(Integer.toHexString(value >> 0));
	}

	@Override
	public String toString() {
		return Integer.toHexString(value) + " - " + maskBinStringOf(value) + " = " + value;
	}

	public String getMaskString() {
		return mask_string;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mask other = (Mask) obj;
		if (value != other.value)
			return false;
		return true;
	}

	public String getColorName() {
		return JUtils.split(mask_string, "\\.").getElementAt(1);
	}

	public String bitAt(int i) {
		// int t = this.value >> i;
		// t = t & 0x1;
		String tmp = Integer.toBinaryString(original);
		int N = 12;
		while (tmp.length() < N) {
			tmp = "0" + tmp;
		}
		tmp = "" + tmp.charAt(N - 1 - i);
		return tmp;
	}

	public short getValue() {
		return this.value;
	}

}
