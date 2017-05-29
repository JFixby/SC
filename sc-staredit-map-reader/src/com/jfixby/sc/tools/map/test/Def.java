
package com.jfixby.sc.tools.map.test;

import java.util.ArrayList;

import com.jfixby.scarabei.api.collections.Set;

public class Def {
	public static final String DEF_FILE_EXTENTION = "def";
	public String name;
	public ArrayList<MaskEntry> entries = new ArrayList<MaskEntry>();

	Def () {

	}

	public Def (final String name) {
		this.name = name;
	}

	public void addMasks (final Set<Mask> masks) {
		for (int i = 0; i < masks.size(); i++) {
			final Mask mask = masks.getElementAt(i);
			this.addMask(mask);
		}
	}

	public void addMask (final Mask mask) {
		final MaskEntry entry = new MaskEntry(mask);
		this.entries.add(entry);
	}

}
