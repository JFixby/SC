package com.jfixby.sc.tools.map.test;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;

public class Diamond {

	Mask b0;
	Mask b1;
	Mask b2;
	Mask b3;
	private int x;
	private int y;
	private Set<DiamondJoint> allJoints = Collections.newSet();
	final private Isom master;
	Set<Mask> masks = Collections.newSet();

	public Diamond(Isom isom, int x, int y) {
		this.master = isom;
		this.x = x;
		this.y = y;
	}

	public void set(int i, short b) {

		if (i == 0) {
			b0 = new Mask(b);
		}
		if (i == 1) {
			b1 = new Mask(b);
		}
		if (i == 2) {
			b2 = new Mask(b);
		}
		if (i == 3) {
			b3 = new Mask(b);
		}
		if (b0 != null && b1 != null && b2 != null && b3 != null) {
			// List<DiamondJoint> tmp = JUtils.newList();
			// tmp.add(new DiamondJoint(b0, b1));
			// tmp.add(new DiamondJoint(b2, b1));
			// tmp.add(new DiamondJoint(b2, b3));
			// tmp.add(new DiamondJoint(b0, b3));
			// tmp.add(new DiamondJoint(b0, b2));
			// tmp.add(new DiamondJoint(b1, b3));
			//
			// for (int k = 0; k < tmp.size(); k++) {
			// DiamondJoint e = tmp.getElementAt(i);
			// if (isGoodjoint(e)) {
			// allJoints.add(e);
			// }
			// }

			masks.add(b0);
			masks.add(b1);
			masks.add(b2);
			masks.add(b3);
		}
	}

	private boolean isGoodjoint(DiamondJoint e) {
		return e.isGood();
	}

	public String Char(Mask mask, MaskResolver maskResolver) {
		if (mask.getValue() == 0x3) {
			return Printer.wrap(" ");
		}
		// FabricColor color = maskResolver.resolve(this);
		// return color.toChar();
		return mask.toChar();
	}

	public String toDebugString(int l, MaskResolver maskResolver) {
		if (l == 0) {
			return "" + prefix() + Char(b1, maskResolver) + prefix() + split_prefix();
		}
		if (l == 1) {
			return "" + Char(b0, maskResolver) + prefix() + Char(b2, maskResolver) + split_prefix();
		}
		if (l == 2) {
			return "" + prefix() + Char(b3, maskResolver) + prefix() + split_prefix();
		}
		if (l == 3) {
			return "" + prefix() + prefix() + prefix() + fabric(maskResolver);
		}
		return null;
	}

	public FabricColor getFabricColor(MaskResolver maskResolver) {
		return maskResolver.resolve(this);
	}

	private static String prefix() {
		return Printer.prefix();
	}

	private static String split_prefix() {
		return Printer.split_prefix();
	}

	public boolean isFabricContainer() {
		return Isom.isFabricContainer(x, y);
	}

	private String fabric(MaskResolver maskResolver) {
		if (!this.isFabricContainer()) {
			// return DiamondColor.wrap("...");
			// return Printer.wrap(x + "." + y);
			return Printer.split_prefix();
		} else {
			java.lang.String char_s = this.getFabricColor(maskResolver).toChar();
			if (char_s != null) {
				return Printer.wrap_split_center(char_s);
			}

			// return Printer.wrap("[" + Integer.toHexString(b3.getValue() >> 0)
			// + "]");
			return Printer.wrap_split_center("[" + b3.getMaskString() + "]");
		}
	}

	public Mask getTopColor() {
		return b1;
	}

	public Mask getBottomColor() {
		return b3;
	}

	public Mask getLeftColor() {
		return b0;
	}

	public Mask getRightColor() {
		return b2;
	}

	public Set<DiamondJoint> allJoints() {
		return allJoints;
	}

	public Set<Mask> getMasks() {
		return this.masks;
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

}
