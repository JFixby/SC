package com.jfixby.sc.tools.map.test;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.collections.Mapping;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;

public class MaskResolver {
	FabricColor BAD = new FabricColor(null);
	List<FabricColor> colors = Collections.newList();
	Map<Mask, FabricColor> reg = Collections.newMap();
	Map<FabricColor, List<Mask>> by_fabrics = Collections.newMap();
	Set<Mask> failed_masks = Collections.newSet();
	Set<FabricColor> detected = Collections.newSet();
	long total_requests = 0;
	long failed_requests = 0;

	public MaskResolver(Tileset tileset) throws IOException {
		load(tileset);
	}

	public void reset() {
		reg.clear();
	}

	public void load(Def def) {
		FabricColor color = new FabricColor(def.name);
		colors.add(color);
		for (int i = 0; i < def.entries.size(); i++) {
			MaskEntry elem = def.entries.get(i);

			Mask key = new Mask(elem.mask);
			if (reg.containsKey(key)) {
				L.d("key", key);
				L.d(" value 1", reg.get(key));
				L.d(" value 2", color);
				Err.reportError("Keys collision <" + key + ">");
			}
			reg.put(key, color);

			List<Mask> list = by_fabrics.get(color);
			if (list == null) {
				list = Collections.newList();
				by_fabrics.put(color, list);
			}
			list.add(key);
		}
	}

	private void load(Tileset tileset) throws IOException {
		reset();
		File input_folder = LocalFileSystem.ApplicationHome();
		String tileset_folder_name = tileset.getFolderName();
		File def_folder = input_folder.child("def").child(tileset_folder_name);

		FilesList defs = def_folder.listDirectChildren();
		for (int i = 0; i < defs.size(); i++) {
			File def_i_folder = defs.getElementAt(i);
			if (def_i_folder.isFolder()) {
				String name = def_i_folder.nameWithoutExtension();
				File def_file_i = def_i_folder.child(name + "." + Def.DEF_FILE_EXTENTION);
				L.d("loading", def_file_i);
				String data = def_file_i.readToString();
				Def def = Json.deserializeFromString(Def.class, data);
				load(def);
			}
		}

		File manual_folder = input_folder.child("holistic").child(tileset_folder_name);
		manual_folder.makeFolder();
		FilesList manual_def_files = manual_folder.listDirectChildren().filterByExtension(Def.DEF_FILE_EXTENTION);
		for (int i = 0; i < manual_def_files.size(); i++) {
			File fi = manual_def_files.getElementAt(i);
			L.d("loading", fi);
			String data = fi.readToString();
			ManualDef def = Json.deserializeFromString(ManualDef.class, data);
			load(def);
		}
	}

	private void load(ManualDef manual_def) {
		for (int i = 0; i < manual_def.sections.size(); i++) {
			Def def = manual_def.sections.get(i);
			load(def);
		}
	}

	public FabricColor resolve(Diamond diamond) {
		if (!diamond.isFabricContainer()) {
			Err.reportError("?");
		}
		Mask bottom = diamond.getBottomColor();
		// return diamond.getBottomColor();

		FabricColor color = this.findColor(bottom);

		return color;

	}

	private FabricColor findColor(Mask mask) {
		FabricColor color = this.reg.get(mask);
		this.total_requests++;

		if (color == null) {
			failed_masks.add(mask);
			this.failed_requests++;
			return BAD;
		}
		detected.add(color);
		return color;
	}

	public Set<Mask> listFailed() {
		return failed_masks;
	}

	public void resetFailedCounter() {
		failed_masks.clear();
		detected.clear();
		this.total_requests = 0;
		this.failed_requests = 0;
	}

	public float getFailedRatio() {
		return failed_requests * 1f / total_requests;
	}

	public Set<FabricColor> listDetected() {
		return detected;
	}

	public Mapping<FabricColor, List<Mask>> getMapping() {
		return by_fabrics;
	}
}
