package com.jfixby.sc.tools.map.test;

public enum Tileset {
	Badlands(0), Platform(1), Installation(2), Ashworld(3), Jungle(4), Desert(5), Ice(6), Twilight(7);

	Tileset(int id) {
		this.id = id;
	}

	final int id;

	public static Tileset valueOf(short readInt) {
		return values()[readInt];
		// for (int i = 0; i < values().length; i++) {
		// values()[i]==
		// }
		// return null;
	}

	public String getFolderName() {
		return this.toString();
	}
}
