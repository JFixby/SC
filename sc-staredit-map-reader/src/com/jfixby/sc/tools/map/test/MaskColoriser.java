package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.math.FloatMath;

public class MaskColoriser extends Coloriser {
	final Map<String, Color> palette = Collections.newMap();

	public Color colorOf(Diamond value) {
		String key = keyFor(value.b0);
		Color color = palette.get(key);
		if (color == null) {
			color = Colors.newRandomColor(1);
			palette.put(key, color);
		}
		return color;
	}

	private String keyFor(Mask value) {
		// return value.getVarName();
		// return value.getColorName();
		// return value.bitAt(4);
		return (value.getValue() >> 4) + "";
	}

	@Override
	public Color colorOf(float x, float y, Isom isom) {

		int i = (int) FloatMath.round(x);
		int j = (int) FloatMath.round(y);
		;
		if (i < 0 || i >= isom.getWidth() || j < 0 || j >= isom.getHeight()) {
			return Colors.RED();
		}
		Diamond value = isom.getDiamond(i, j);
		Color color = colorOf(value);
		color = color.customize().setAlpha(1);
		return color;

	}
}
