
package com.jfixby.sc.tools.map.test;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Mapping;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;

public class RunPrintMasksKnowledge {

	private static final String UNKNOWN = "unknown";

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
// ImageAWT.installComponent(new RedImageAWT());
// CV.installComponent(new RedCV());
// Base64.installComponent(new GdxBase64());
// P18.installComponent(new RedP18());
// Json.installComponent(new GoogleGson());

		final MaskResolver resolver = new MaskResolver(Tileset.Jungle);
		final Mapping<FabricColor, List<Mask>> register = resolver.getMapping();
		for (int i = 0; i < register.size(); i++) {
			final FabricColor color = register.getKeyAt(i);
			final List<Mask> value = register.getValueAt(i);
			value.sort();
			value.print(color + "");
		}

	}

}
