package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.log.L;

public class MapTiles {

	private int width;
	private int height;
	private int[][] array;
	Set<Integer> all_values_palette = Collections.newSet();
	final private ColoredλImage debug_color = new ColoredλImage() {

		@Override
		public Color valueAt(float x, float y) {
			int i = (int) x;
			int j = (int) y;
			if (i < 0 || i >= width || j < 0 || j >= height) {
				return Colors.GREEN();
			}
			int value = array[i][j];
			Color color = Colors.newColor(value << 16);
			color = color.customize().setAlpha(1);
			// color = color.invert();
			return color;
		}
	};

	public MapTiles(int width, int height) {
		this.width = width;
		this.height = height;
		array = new int[width][height];
	}

	public void setXY(int x, int y, int readShort) {
		array[x][y] = readShort;
		all_values_palette.add(readShort);
	}

	public void print() {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				L.d_appendChars(charAt(i, j));
			}
			L.d();
		}
		all_values_palette.print("palette");
	}

	private String charAt(int x, int y) {
		int obj = array[x][y];
		return "." + (obj);
	}

	@Override
	public String toString() {
		return "MapTiles [width=" + width + ", height=" + height + "]";
	}

	public ColoredλImage getDebugImage() {
		return debug_color;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
