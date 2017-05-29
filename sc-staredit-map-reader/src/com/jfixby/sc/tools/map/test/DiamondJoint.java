package com.jfixby.sc.tools.map.test;

public class DiamondJoint {

	@Override
	public String toString() {
		return "DiamondJoint [" + A + ", " + B + "]";
	}

	private FabricColor A;
	private FabricColor B;

	public DiamondJoint(FabricColor b0, FabricColor b1) {
		if (b0.compareTo(b1) >= 0) {
			A = b1;
			B = b0;
		} else {
			A = b0;
			B = b1;
		}
	}

	public boolean isGood() {
		return !A.equals(B);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((A == null) ? 0 : A.hashCode());
		result = prime * result + ((B == null) ? 0 : B.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiamondJoint other = (DiamondJoint) obj;
		if (A == null) {
			if (other.A != null)
				return false;
		} else if (!A.equals(other.A))
			return false;
		if (B == null) {
			if (other.B != null)
				return false;
		} else if (!B.equals(other.B))
			return false;
		return true;
	}

	public FabricColor getUpper() {
		return B;
	}

	public FabricColor getLower() {
		return A;
	}

}
