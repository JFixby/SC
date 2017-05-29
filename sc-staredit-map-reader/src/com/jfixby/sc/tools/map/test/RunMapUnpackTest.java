//package com.jfixby.sc.tools.map.test;
//
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//import com.jfixby.cmns.adopted.gdx.base64.GdxBase64;
//import com.jfixby.cmns.api.base64.Base64;
//import com.jfixby.cmns.api.collections.Map;
//import com.jfixby.cmns.api.collections.Set;
//import com.jfixby.cmns.api.color.Color;
//import com.jfixby.cmns.api.filesystem.File;
//import com.jfixby.cmns.api.filesystem.LocalFileSystem;
//import com.jfixby.cmns.api.floatn.Float2;
//import com.jfixby.cmns.api.geometry.Geometry;
//import com.jfixby.cmns.api.geometry.Rectangle;
//import com.jfixby.cmns.api.image.ColorMap;
//import com.jfixby.cmns.api.image.ImageProcessing;
//import com.jfixby.cmns.api.image.LambdaColorMap;
//import com.jfixby.cmns.api.image.LambdaColorMapSpecs;
//import com.jfixby.cmns.api.io.InputStream;
//import com.jfixby.cmns.api.lambda.λFunction;
//import com.jfixby.cmns.api.log.L;
//import com.jfixby.cmns.api.math.FixedInt2;
//import com.jfixby.cmns.api.math.FloatMath;
//import com.jfixby.cmns.api.sys.Sys;
//import com.jfixby.cmns.api.util.JUtils;
//import com.jfixby.cmns.desktop.DesktopAssembler;
//import com.jfixby.cv.api.cv.CV;
//import com.jfixby.cv.api.gwt.ImageGWT;
//import com.jfixby.cv.red.gwt.RedCV;
//import com.jfixby.cv.red.gwt.RedImageGWT;
//import com.jfixby.r3.ext.api.patch18.Grid;
//import com.jfixby.r3.ext.api.patch18.GridFactory;
//import com.jfixby.r3.ext.api.patch18.GridSpecs;
//import com.jfixby.r3.ext.api.patch18.P18;
//import com.jfixby.r3.ext.api.patch18.P18Palette;
//import com.jfixby.r3.ext.api.patch18.P18PaletteFactory;
//import com.jfixby.r3.ext.api.patch18.PaletteSpecs;
//import com.jfixby.r3.ext.api.patch18.grid.GridBrush;
//import com.jfixby.r3.ext.api.patch18.grid.GridBrushApplicationResult;
//import com.jfixby.r3.ext.api.patch18.palette.Fabric;
//import com.jfixby.r3.ext.api.patch18.palette.FabricSpecs;
//import com.jfixby.util.patch18.red.RedP18;
//
//public class RunMapUnpackTest {
//
//	public static void main(String[] args) throws IOException {
//		DesktopAssembler.setup();
//		ImageGWT.installComponent(new RedImageGWT());
//		CV.installComponent(new RedCV());
//		Base64.installComponent(new GdxBase64());
//		P18.installComponent(new RedP18());
//
//		File input_folder = LocalFileSystem.ApplicationHome().child("input");
//
//		// File file = input_folder.child("2.chk");
//		// File file = input_folder.child("3.chk");
//		File file = input_folder.child("def").child("mud.chk");
//
//		// File file = input_folder.child("empty.chk");
//		// File file = input_folder.child("asfalt.chk");
//
//		ScenarioUnpacker unpacker = new ScenarioUnpacker();
//		InputStream stream = file.newInputStream();
//		java.io.InputStream java_stream = stream.toJavaInputStream();
//		unpacker.readFromStream(java_stream);
//
//		MapTiles tile_map = unpacker.getTileMap();
//		Isom isom = unpacker.getIsom();
//
//		float scale_factor = 4;
//		{
//			λImage λimage = tile_map.getDebugImage();
//			λimage = CV.scale(λimage, scale_factor);
//			int w = tile_map.getWidth();
//			int h = tile_map.getHeight();
//
//			saveResult(λimage, FloatMath.round(w * scale_factor), FloatMath.round(h * scale_factor), "tilemap.png");
//		}
//
//		{
//			λImage λimage = isom.getDebugImage();
//			λimage = CV.scale(λimage, scale_factor);
//			λimage = CV.scale(λimage, 2, 1);
//			int w = isom.getWidth();
//			int h = isom.getHeight();
//
//			Set<Mask> masks = isom.getAllMasks();
//			// masks.print("masks");
//			// Sys.exit();
//			isom.pritn();
//			masks.sort();
//			masks.print("masks");
//			// Sys.exit();
//			// Sys.exit();
//			// saveResult(λimage, FloatMath.round(w * scale_factor * 2),
//			// FloatMath.round(h * scale_factor), "isom.png");
//		}
//
//		printP18(isom);
//
//	}
//
//	private static void printP18(Isom isom) {
//		P18PaletteFactory palette_factory = P18.invoke().getPaletteFactory();
//		PaletteSpecs palette_specs = palette_factory.newPaletteSpecs();
//
//		Set<DiamondColor> fabrics_int = isom.listFabrics();
//		Set<DiamondJoint> joints_list = isom.listJoints();
//		Map<FabricColor, Fabric> coloring = JUtils.newMap();
//
//		for (int i = 0; i < fabrics_int.size(); i++) {
//			DiamondColor c = fabrics_int.getElementAt(i);
//			FabricSpecs fabric_specs = palette_factory.newFabricSpecs();
//			fabric_specs.setFabricName("f-" + c);
//			Fabric fabric = palette_factory.newFabric(fabric_specs);
//			palette_specs.addFabric(fabric);
//			coloring.put(c, fabric);
//		}
//		joints_list.print("joints_list");
//
//		for (int i = 0; i < joints_list.size(); i++) {
//			DiamondJoint joint_int = joints_list.getElementAt(i);
//			DiamondColor color_a = joint_int.getUpper();
//			DiamondColor color_b = joint_int.getLower();
//			Fabric upper_fabric = coloring.get(color_a);
//			Fabric lower_fabric = coloring.get(color_b);
//			palette_specs.defineRelation(upper_fabric, lower_fabric);
//		}
//
//		P18Palette palette = palette_factory.newPalette(palette_specs);
//
//		GridFactory grid_factory = P18.getGridFactory();
//		GridSpecs grid_specs = grid_factory.newGridSpecs();
//		grid_specs.setPalette(palette);
//		grid_specs.setActiveGridArea(null);
//
//		Grid grid = grid_factory.newGrid(grid_specs);
//		// produceGrid(grid, coloring, isom);
//		produceGrid2(grid, coloring, isom);
//	}
//
//	private static void produceGrid2(Grid grid, Map<FabricColor, Fabric> coloring, Isom isom) {
//
//		double rootOf2 = FloatMath.sqrt(2);
//		GridBrush brush = grid.getBrush();
//		int gridW = (isom.getWidth() + isom.getHeight());
//		int gridH = (isom.getHeight() + isom.getWidth());
//
//		Rectangle grid_float_area = Geometry.newRectangle(gridW / rootOf2, gridH / rootOf2);
//		Rectangle grid_index_area = Geometry.newRectangle(gridW / 2, gridH / 2);
//		Rectangle isom_float_area = Geometry.newRectangle(isom.getWidth() - 1, isom.getHeight() - 1);
//		Rectangle isom_index_area = Geometry.newRectangle((isom.getWidth() - 1), (isom.getHeight() - 1));
//
//		isom_float_area.setRotation(FloatMath.toRadians(-45));
//		isom_float_area.setPosition(0, isom.getWidth() / rootOf2);
//
//		L.d("grid_index_area   ", grid_index_area);
//		L.d("grid_float_area   ", grid_float_area);
//		L.d("isom_float_area   ", isom_float_area);
//		L.d("isom_index_area   ", isom_index_area);
//
//		brush.begin();
//
//		for (int grid_y = 0; grid_y < grid_index_area.getHeight(); grid_y++) {
//			for (int grid_x = 0; grid_x < grid_index_area.getWidth(); grid_x++) {
//
//				Float2 tmp = Geometry.newFloat2(grid_x, grid_y);
//
//				grid_index_area.toRelative(tmp);
//				//
//				grid_float_area.toAbsolute(tmp);
//				// L.d(" grid f", tmp);
//				// L.d("   isom", isom_float_area);
//
//				isom_float_area.toRelative(tmp);
//				// L.d(" isom r", tmp);
//				isom_index_area.toAbsolute(tmp);
//
//				int isom_x = (int) (tmp.getX());
//				int isom_y = (int) (tmp.getY());
//
//				Diamond diamond = isom.getDiamond(isom_x, isom_y);
//
//				if (diamond == null) {
//
//					continue;
//				}
//
//				if (diamond.isFabricContainer()) {
//					// L.d("grid", grid_x + "," + grid_y);
//					// L.d(" isom f", tmp);
//					// L.d(" isom i", isom_x + "," + isom_y);
//					// L.d(" isom F", diamond.isFabricContainer());
//				} else {
//					// L.d("grid", grid_x + "," + grid_y);
//					// L.d(" isom f", tmp);
//					// L.d(" isom i", isom_x + "," + isom_y);
//					// L.d(" isom F", diamond.isFabricContainer());
//					continue;
//				}
//				DiamondColor diamond_color = diamond.getFabricColor(null);
//				// DiamondColor diamond_color = isom.getFabricAt(isom_x,
//				// isom_y);
//
//				Fabric fabric = coloring.get(diamond_color);
//				brush.setFabric(fabric);
//				brush.setPaintAtNode(grid_x, grid_y);
//			}
//		}
//
//		GridBrushApplicationResult result = brush.end();
//		grid.print(0, 0, gridW / 2, gridH / 2);
//	}
//
//	private static void produceGrid(Grid grid, Map<DiamondColor, Fabric> coloring, Isom isom) {
//
//		double rootOf2 = FloatMath.sqrt(2);
//		GridBrush brush = grid.getBrush();
//		int gridW = (isom.getWidth() + isom.getHeight());
//		int gridH = (isom.getHeight() + isom.getWidth());
//
//		Rectangle grid_float_area = Geometry.newRectangle(gridW / rootOf2, gridH / rootOf2);
//		Rectangle grid_index_area = Geometry.newRectangle(gridW, gridH);
//		Rectangle isom_float_area = Geometry.newRectangle(isom.getWidth() - 1, isom.getHeight() - 1);
//		Rectangle isom_index_area = Geometry.newRectangle(isom.getWidth() - 1, isom.getHeight() - 1);
//
//		grid_float_area.setRotation(FloatMath.toRadians(-45));
//		grid_float_area.setPosition(-isom.getWidth(), +isom.getWidth());
//
//		L.d("grid_index_area   ", grid_index_area);
//		L.d("grid_float_area   ", grid_float_area);
//		L.d("isom_float_area   ", isom_float_area);
//		L.d("isom_index_area   ", isom_index_area);
//
//		brush.begin();
//
//		for (int isom_y = 0; isom_y < isom_index_area.getHeight(); isom_y = isom_y + 1) {
//			for (int isom_x = 0; isom_x < isom_index_area.getWidth(); isom_x = isom_x + 1) {
//
//				Diamond diamond = isom.getDiamond(isom_x, isom_y);
//				if (!diamond.isFabricContainer()) {
//					continue;
//				}
//				DiamondColor diamond_color = diamond.getFabricColor(null);
//				Float2 tmp = Geometry.newFloat2(isom_x, isom_y);
//
//				L.d("isom", tmp);
//
//				isom_index_area.toRelative(tmp);
//				isom_float_area.toAbsolute(tmp);
//				grid_float_area.toRelative(tmp);
//				grid_index_area.toAbsolute(tmp);
//
//				Fabric fabric = coloring.get(diamond_color);
//
//				if (fabric == null) {
//					tmp = Geometry.newFloat2(isom_x, isom_y);
//					L.d("skip", tmp);
//					continue;
//				}
//
//				brush.setFabric(fabric);
//
//				int grid_x = (int) FloatMath.round(tmp.getX());
//				int grid_y = (int) FloatMath.round(tmp.getY());
//
//				L.d("  grid f", tmp);
//				L.d("  grid i", grid_x + "," + grid_y);
//
//				brush.setPaintAtNode(grid_x, grid_y);
//			}
//		}
//
//		GridBrushApplicationResult result = brush.end();
//		grid.print(0, 0, gridW, gridH);
//	}
//
//	
//
// }
