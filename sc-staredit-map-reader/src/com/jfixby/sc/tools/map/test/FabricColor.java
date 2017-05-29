package com.jfixby.sc.tools.map.test;

public class FabricColor implements Comparable<FabricColor> {

	private String name;

	@Override
	public String toString() {
		return "#" + name + "";
	}

	public FabricColor(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FabricColor other = (FabricColor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toChar() {
		if (name != null) {
			return ("" + this.name.substring(0, 3) + "").toUpperCase();
		} else {
			// return "<?>";
			return null;
		}
	}

	@Override
	public int compareTo(FabricColor o) {
		return this.name.compareTo(o.name);
	}

	public float getGrayValue() {
		return ((byte) this.hashCode()) / 255f;
	}

	public String getName() {
		return name;
	}

}
