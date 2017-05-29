
package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.log.L;

public class Isom {
	Isom me = this;
	final Diamond[][] array;
	final private int width;
	final private int height;
	// private Set<DiamondColor> fabrics = JUtils.newSet();
	// private Set<DiamondJoint> joints = JUtils.newSet();
	private final Set<Mask> masks = Collections.newSet();

	public Isom (final int width, final int height) {
		this.width = width;
		this.height = height;
		this.array = new Diamond[width][height];
	}

	public void setXY (final int x, final int y, final Diamond diamond) {
		this.array[x][y] = diamond;
		// fabrics.add(diamond.getTopColor());
		// fabrics.add(diamond.getBottomColor());
		// fabrics.add(diamond.getLeftColor());
		// fabrics.add(diamond.getRightColor());
		// joints.addAll(diamond.allJoints());
		this.masks.addAll(diamond.getMasks());
	}

	public void pritn (final MaskResolver maskResolver) {
		this.pritn(0, 0, this.width, this.height, maskResolver);

	}

	public ColoredλImage getDebugImage (final Coloriser coloriser) {
		return new ColoredλImage() {

			@Override
			public Color valueAt (final float x, final float y) {
				return coloriser.colorOf(x, y, Isom.this.me);
			}

		};
	}

	public int getWidth () {
		return this.width;
	}

	public int getHeight () {
		return this.height;
	}

	private boolean isOut (final int x, final int y) {
		return x < 0 || x >= this.width || y < 0 || y >= this.height;
	}

	public Diamond getDiamond (final int x, final int y) {
		if (this.isOut(x, y)) {
			return null;
		}
		return this.array[x][y];
	}

	public Set<Mask> getAllMasks () {
		return this.masks;
	}

	public void pritn (final int x0, final int y0, final int w, final int h, final MaskResolver maskResolver) {
		for (int y = y0; y < y0 + h; y++) {
			for (int l = 0; l < 4; l++) {
				for (int x = x0; x < x0 + w; x++) {
					final Diamond diamond = this.array[x][y];
					L.d_appendChars(diamond.toDebugString(l, maskResolver));
				}
				L.d();
			}
			// L.d();
		}
	}

	public boolean isOutside (final float i, final float j) {
		return i < 0 || i >= this.getWidth() || j < 0 || j >= this.getHeight();
	}

	public static boolean isFabricContainer (final int x, final int y) {
		return !((x % 2 == 0 && y % 2 == 0) || (x % 2 != 0 && y % 2 != 0));
	}
}
