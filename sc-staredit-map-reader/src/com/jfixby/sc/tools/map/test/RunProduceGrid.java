
package com.jfixby.sc.tools.map.test;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.InputStream;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;

public class RunProduceGrid {

	private static final String UNKNOWN = "unknown";

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
// ImageAWT.installComponent(new RedImageAWT());
// CV.installComponent(new RedCV());
// Base64.installComponent(new GdxBase64());
// P18.installComponent(new RedP18());
// Json.installComponent(new GoogleGson());

		final File input_folder = LocalFileSystem.ApplicationHome().child("input");
		final FilesList list = input_folder.listDirectChildren().filterByExtension(".chk");

		for (int i = 0; i < list.size(); i++) {
			final File file = list.getElementAt(i);

			collectUnknown(file);
		}

	}

	private static void collectUnknown (final File file) throws IOException {

		final String tager_file_name = file.nameWithoutExtension();
		L.d("---[" + tager_file_name + "]-----------------");

		// File file = input_folder.child("empty.chk");
		// File file = input_folder.child("asfalt.chk");

		final ScenarioUnpacker unpacker = new ScenarioUnpacker();
		final InputStream stream = file.newInputStream();
		final java.io.InputStream java_stream = stream.toJavaInputStream();
		unpacker.readFromStream(java_stream);

		final Tileset tileset = unpacker.getTileset();

		final MaskResolver resolver = new MaskResolver(tileset);

		final MapTiles tile_map = unpacker.getTileMap();
		final Isom isom = unpacker.getIsom();
		isom.pritn(resolver);
		final Set<Mask> failed_masks = resolver.listFailed();
		final Set<FabricColor> detected = resolver.listDetected();
		// printP18(isom);

		float failed_value = resolver.getFailedRatio();
		failed_value = (float)FloatMath.roundToDigit(failed_value * 100, 2);
		final String ratio_string = failed_value + "%";
		failed_masks.sort();
		failed_masks.print("failed " + tager_file_name);
		L.d("failed to resolve", ratio_string);
		detected.sort();
		detected.print("detected " + tager_file_name);

		final Def failed = new Def(UNKNOWN);
		for (int i = 0; i < failed_masks.size(); i++) {
			final Mask failed_mask = failed_masks.getElementAt(i);
			failed.addMask(failed_mask);
		}
		final File holistic_folder = LocalFileSystem.ApplicationHome().child("holistic");
		final File unk = holistic_folder.child(UNKNOWN);
		unk.makeFolder();
		// unk.clearFolder();
		final File unk_def_file = unk.child(tager_file_name + "." + Def.DEF_FILE_EXTENTION);
		unk_def_file.writeString(Json.serializeToString(failed).toString());

	}
}
