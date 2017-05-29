package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.ColorRandomiser;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.math.FloatMath;
import com.jfixby.scarabei.api.math.Int2;
import com.jfixby.scarabei.api.math.IntegerMath;

public class FabricColoriser extends Coloriser {

	private ColorRandomiser randomizer;

	public FabricColoriser(MaskResolver maskResolver, long seed) {
		super();
		this.maskResolver = maskResolver;

		randomizer = Colors.newColorRandomizer(seed);
	}

	final Map<String, Color> palette = Collections.newMap();
	final private MaskResolver maskResolver;

	public Color colorOf(Diamond value) {

		FabricColor fabric;
		if (value.isFabricContainer()) {
			fabric = value.getFabricColor(maskResolver);

			String key = keyFor(fabric);
			Color color = palette.get(key);
			if (color == null) {
				color = randomizer.newRandomColor(1);
				palette.put(key, color);
			}
			return color;
		} else {
			// return λimage.val(value.getX(), value.getY() + 1);
			return Colors.BLACK();
		}
	}

	@Override
	public Color colorOf(float x, float y, Isom isom) {

		int i = (int) FloatMath.floorDown(x);
		int j = (int) FloatMath.floorDown(y);

		// int i = (int) FloatMath.roundToDigit(x, 4);
		// int j = (int) FloatMath.roundToDigit(y, 4);

		if (isom.isOutside(i, j)) {
			return Colors.NO();
		}

		if (Isom.isFabricContainer(i, j)) {
			// return colorOf(isom.getDiamond(i, j)).customize().toGrayscale();
			return colorOf(isom.getDiamond(i, j));
		}
		// if (FloatMath.isInteger(x) || FloatMath.isInteger(y)) {
		// return Colors.BLACK();
		// }
		double fx = FloatMath.fractionalPartOf(x);
		double fy = FloatMath.fractionalPartOf(y);

		// fx = FloatMath.roundToDigit(fx, 1);
		// fy = FloatMath.roundToDigit(fy, 1);

		if (fx < fy) {
			if (1 - fx >= fy) {
				return colorOf(i - 1, j, isom);
			}
			return colorOf(i, j + 1, isom);
		} else {
			if (1 - fx >= fy) {
				// return colorOf(i + 1, j, isom);
				return colorOf(i, j - 1, isom);
				// return Colors.BLUE();
			}
			return colorOf(i + 1, j, isom);
			//
		}

	}

	private Int2 findClosestContainer(float x, float y, Isom isom) {
		int i = (int) FloatMath.round(x);
		int j = (int) FloatMath.round(y);

		double fx = FloatMath.fractionalPartOf(x);
		double fy = FloatMath.fractionalPartOf(y);

		return IntegerMath.newInt2(i - 1, j);

	}

	private boolean isCloserThan(Int2 candidate, Int2 current, float x, float y) {
		if (current == null) {
			return true;
		}
		return FloatMath.distance(x, y, current.getX(), current.getY()) > FloatMath.distance(x, y, candidate.getX(), candidate.getY());
	}

	private String keyFor(FabricColor value) {
		return value.getName();
	}
}
