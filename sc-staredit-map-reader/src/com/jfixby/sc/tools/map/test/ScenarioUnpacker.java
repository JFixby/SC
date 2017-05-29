
package com.jfixby.sc.tools.map.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;

public class ScenarioUnpacker {

	private InputStream stream;
	private int stream_position;
	boolean EOS = false;
	Map<String, SectionData> sections = Collections.newMap();
	private int width;
	private int height;
	private TileSetID tileSet;
	private int scenarioType;
	private byte[][] mapMask;

	public void readFromStream (final InputStream stream) throws IOException {

		this.resetStream(stream);
		while (!this.EOS) {

			final String section_name = this.readString(4);
			if (this.EOS) {
				break;
			}

			final SectionData sec_data = new SectionData();
			sec_data.name = section_name;
			sec_data.data_length = this.readInt();
			sec_data.data_position = this.currentStreamPosition();
			sec_data.data = this.readBytes(sec_data.data_length);
			this.sections.put(section_name, sec_data);

		}
		// sections.print("sections");

		/*
		 * parse only the sections we really care about up front. the rest are done as needed. this speeds up the Play Custom screen
		 * immensely.
		 */

		/* find out what version we're dealing with */
		this.parseSection("VER ");
		if (this.sections.containsKey("IVER")) {
			this.parseSection("IVER");
		}
		if (this.sections.containsKey("IVE2")) {
			this.parseSection("IVE2");
		}

		/* strings */
		this.parseSection("STR ");

		/* load in the map info */

		/* map name/description */
		this.parseSection("SPRP");

		/* dimension */
		this.parseSection("DIM ");

		/* tileset info */
		this.parseSection("ERA ");

		/* player information */
		this.parseSection("OWNR");
		this.parseSection("SIDE");

		this.parseSection("MTXM");

		/* tile info */
		if (this.sections.containsKey("TILE")) {
			this.parseSection("TILE");
		}

		/* isometric mapping */
		if (this.sections.containsKey("ISOM")) {
			this.parseSection("ISOM");
		}
	}

	private void resetStream (final InputStream stream) {
		this.stream = stream;
		this.stream_position = 0;
		this.EOS = false;
	}

	List<String> strings = Collections.newList();
	private String mapName;
	private String mapDescription;
	private int numPlayers;
	private int numComputerSlots;
	private int numHumanSlots;
	private MapTiles mapTiles;
	private Isom isom;

	public String getMapString (final int idx) {
		if (idx == 0) {
			return "";
		}
		// return strings[idx - 1];
		return this.strings.getElementAt(idx - 1);
	}

	private void parseSection (final String section_name) {

		final SectionData sec = this.sections.get(section_name);
		if (sec == null) {
			Err.reportError("" + "map file is missing section {0}, cannot load" + section_name);
		}

		final byte[] section_data = sec.data;
		final ByteArrayInputStream buffer = new ByteArrayInputStream(section_data);
		this.resetStream(buffer);

		if (section_name == "TYPE") {
			this.scenarioType = this.readInt();
			// L.d("scenarioType", scenarioType);
		} else if (section_name == "ERA ") {
			this.tileSet = new TileSetID((short)this.readShort());
			// L.d("tileSet", tileSet);

		} else if (section_name == "DIM ") {
			this.width = this.readShort();
			this.height = this.readShort();
			// L.d("width x height", width + " x " + height);
		} else if (section_name == "MTXM") {
			this.mapTiles = new MapTiles(this.width, this.height);
			int y, x;
			for (y = 0; y < this.height; y++) {
				for (x = 0; x < this.width; x++) {
					this.mapTiles.setXY(x, y, this.readShort());
				}
			}
			// mapTiles[x][y] = readShort();
			// L.d("mapTiles", mapTiles);
			// mapTiles.print();
			// Util.ReadWord (section_data, (y*width + x)*2);
		} else if (section_name == "MASK") {
			this.mapMask = new byte[this.width][this.height];
			int y, x;
			for (y = 0; y < this.height; y++) {
				for (x = 0; x < this.width; x++) {
					this.mapMask[x][y] = (byte)this.readByte();
				}
			}
			// section_data [i++];
		} else if (section_name == "SPRP") {
			final int nameStringIndex = this.readShort();
			final int descriptionStringIndex = this.readShort();

			// L.d("mapName = {0}", nameStringIndex);
			// L.d("mapDescription = {0}", descriptionStringIndex);
			this.mapName = this.getMapString(nameStringIndex);
			this.mapDescription = this.getMapString(descriptionStringIndex);
			// L.d("mapName", mapName);
			// L.d("mapDescription", mapDescription);

		} else if (section_name == "STR ") {
			this.readStrings(section_data);
			// this.strings.print("strings");
		} else if (section_name == "OWNR") {
			this.numPlayers = 0;
			for (int i = 0; i < 12; i++) {
				/*
				 * 00 - Unused 03 - Rescuable 05 - Computer 06 - Human 07 - Neutral
				 */
				if (section_data[i] == 0x05) {
					this.numComputerSlots++;
				} else if (section_data[i] == 0x06) {
					this.numHumanSlots++;
				}
			}
		} else if (section_name == "SIDE") {
			/*
			 * 00 - Zerg 01 - Terran 02 - Protoss 03 - Independent 04 - Neutral 05 - User Select 07 - Inactive 10 - Human
			 */
			this.numPlayers = 0;
			for (int i = 0; i < 12; i++) {
				if (section_data[i] == 0x05) {
					this.numPlayers++;
				}
			}
		} else if (section_name == "UNIT") {
			// L.d("skip", section_name);
			// ReadUnits(section_data);
		} else if (section_name == "ISOM") {
			// L.d("skip", section_name);
			// ReadUnits(section_data);

			// shorts
			int y, x;
			final int H = this.height + 1;
			final int W = this.width / 2 + 1;
			this.isom = new Isom(W, H);
			final int expected_size = W * H;// number of short arrays[4]
			for (y = 0; y < H; y++) {
				for (x = 0; x < W; x++) {
					String data = "";

					final Diamond diamond = new Diamond(this.isom, x, y);
					for (int i = 0; i < 4; i++) {

						final short bi = (short)(this.readShort());

						data = data + "." + Integer.toHexString(bi);
						diamond.set(i, bi);
						// L.d("visited", visit1 + "/" + expected_size);
						// L.d("visited", visit2 + 1 + "/" + expected_size);
					}
					// L.d_addChars("|" + data);
					this.isom.setXY(x, y, diamond);
					// L.d("(" + x + "," + y + ")", data);

				}
				// L.d();
			}

			final int actual_size = section_data.length / 2 / 4;
			// L.d("expected_size", expected_size);
			// L.d(" actual_size", actual_size);
			// isom.pritn();
			// Sys.exit();
		} else if (section_name == "MBRF") {
			L.d("skip", section_name);
			// briefingData = new TriggerData();
			// briefingData.Parse(section_data, true);
		} else {
			// L.d("Unhandled Chk section type " + section_name + ", length " +
			// section_data.length);
		}
	}

	void readStrings (final byte[] data) {

		int i;
		int head_size = 0;
		final int num_strings = this.readShort();
		head_size = head_size + 2;

		// int[] offsets = new int[num_strings];
		final List<Integer> offsets = Collections.newList();

		for (i = 0; i < num_strings; i++) {
			offsets.add(this.readShort());
			head_size = head_size + 2;
		}

		for (i = 0; i < num_strings; i++) {
			final ByteArrayInputStream buffer = new ByteArrayInputStream(data);
			int rest = data.length;
			this.resetStream(buffer);
			// readBytes(head_size);
			// rest = rest - head_size;
			this.readBytes(offsets.getElementAt(i));
			rest = rest - offsets.getElementAt(i);
			int len = 0;
			if (i + 1 < num_strings) {
				len = offsets.getElementAt(i + 1) - offsets.getElementAt(i);
			} else {
				len = data.length - offsets.getElementAt(i);
			}
			final String str = this.readString(len);
			this.strings.add(str);
		}
	}

	private byte[] readBytes (final int data_length) {
		final byte[] data = new byte[data_length];
		for (int i = 0; i < data_length; i++) {
			data[i] = (byte)this.readByte();
		}
		return data;
	}

	private int currentStreamPosition () {
		return this.stream_position;
	}

	private int readInt () {
		// return (((((readByte() << 8) | readByte()) << 8) | readByte()) << 8)
		// | readByte();
		return this.readByte() | (this.readByte() << 8) | (this.readByte() << 16) | (this.readByte() << 24);

	}

	private int readShort () {
		// return (((((readByte() << 8) | readByte()) << 8) | readByte()) << 8)
		// | readByte();
		return this.readByte() | (this.readByte() << 8);

	}

	protected int readByte () {
		// read single byte from input
		int curByte = -1;
		try {
			curByte = this.stream.read();
			this.stream_position++;
		} catch (final IOException e) {
			e.printStackTrace();
			Err.reportError(e);
		}
		if (curByte == -1) {
			this.EOS = true;
		}
		return curByte;
	}

	private String readString (final int n) {
		String tmp = "";
		for (int i = 0; i < n; i++) {
			final byte b = (byte)this.readByte();
			tmp = tmp + (char)b;
		}
		return tmp;
	}

	public MapTiles getTileMap () {
		return this.mapTiles;
	}

	public Isom getIsom () {
		return this.isom;
	}

	public Tileset getTileset () {
		return this.tileSet.getEnum();
	}
}
