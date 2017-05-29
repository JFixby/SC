package com.jfixby.sc.tools.map.test;

public class MaskEntry {

	public String mask;

	MaskEntry() {
	}

	public MaskEntry(Mask mask) {
		this.mask = mask.getMaskString();
	}

}
