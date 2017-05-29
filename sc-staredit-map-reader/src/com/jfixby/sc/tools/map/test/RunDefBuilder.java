
package com.jfixby.sc.tools.map.test;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.InputStream;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;

public class RunDefBuilder {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
// ImageAWT.installComponent(new RedImageAWT());
// CV.installComponent(new RedCV());
// Base64.installComponent(new GdxBase64());
// P18.installComponent(new RedP18());
// Json.installComponent(new GoogleGson());

		final File input_folder = LocalFileSystem.ApplicationHome().child("input");
		final File def_folder = input_folder.child("def");

		final FilesList defs = def_folder.listDirectChildren();
		for (int i = 0; i < defs.size(); i++) {
			final File file_i = defs.getElementAt(i);
			if (file_i.isFile()) {
				process(file_i);
			}
		}

	}

	private static void process (final File file_i) throws IOException {

		// File file = input_folder.child("empty.chk");
		// File file = input_folder.child("asfalt.chk");

		final ScenarioUnpacker unpacker = new ScenarioUnpacker();
		final InputStream stream = file_i.newInputStream();
		final java.io.InputStream java_stream = stream.toJavaInputStream();
		unpacker.readFromStream(java_stream);

		final MapTiles tile_map = unpacker.getTileMap();
		final Isom isom = unpacker.getIsom();

		final Tileset tileset = unpacker.getTileset();

		final String name = file_i.nameWithoutExtension();
		final String tileset_folder_name = tileset.getFolderName();
		final File def_i_folder = file_i.parent().parent().parent().child("def").child(tileset_folder_name).child(name);
		def_i_folder.makeFolder();
		def_i_folder.clearFolder();

		final Set<Mask> masks = Collections.newSet();
		final int margin = 4;
		final int l = 6;
		final int r = 6 + 4;
		L.d("---" + name + "------------");
		// isom.pritn();
		L.d(">>>>>");
		// isom.pritn(l, l, r - l, r - l);

		for (int x = l; x < r; x++) {
			for (int y = l; y < r; y++) {
				final Diamond diamond = isom.getDiamond(x, y);
				final Set<Mask> diamond_masks = diamond.getMasks();
				masks.addAll(diamond_masks);
			}
		}
		masks.sort();
		masks.print(name);
		final Def def = new Def(name);
		def.addMasks(masks);
		final File def_file = def_i_folder.child(name + "." + Def.DEF_FILE_EXTENTION);
		final String data = Json.serializeToString(def).toString();
		def_file.writeString(data);
		L.d();
	}
}
