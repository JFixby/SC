package com.jfixby.sc.tools.map.test;

public class TileSetID {

	@Override
	public String toString() {
		// return "TileSetID[" + mask + "] " + readInt + " : " + tileset;
		return tileset + "";
	}

	private String mask;
	private short readInt;
	private Tileset tileset;

	public TileSetID(short readInt) {
		mask = Integer.toBinaryString(readInt);
		this.readInt = readInt;
		tileset = Tileset.valueOf(readInt);

	}

	public Tileset getEnum() {
		return tileset;
	}

}
