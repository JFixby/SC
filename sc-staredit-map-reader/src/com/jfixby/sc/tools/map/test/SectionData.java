package com.jfixby.sc.tools.map.test;

public class SectionData {

	public int data_length;
	public int data_position;
	public String name;
	public byte[] data;

	@Override
	public String toString() {
		return "SectionData[" + name + "] length=" + data_length + " data_position=" + data_position;
	}

}
