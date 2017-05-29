
package com.jfixby.sc.tools.map.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.geometry.Geometry;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.io.InputStream;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;

public class RunPrintFabricPatterns {

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

		final MapTiles tile_map = unpacker.getTileMap();
		final Isom isom = unpacker.getIsom();
		// Coloriser colorizer = new DiamondColoriser();
		final Tileset tileset = unpacker.getTileset();

		final MaskResolver resolver = new MaskResolver(tileset);
		final long color_seed = System.currentTimeMillis();
		L.d("color_seed", color_seed);
		final Coloriser colorizer = new FabricColoriser(resolver, color_seed);

		ColoredλImage λimage = isom.getDebugImage(colorizer);
		final Rectangle input_area = Geometry.newRectangle(isom.getWidth(), isom.getHeight());

		final Rectangle rotated_area = Geometry.newRectangle(isom.getWidth(), isom.getHeight());
		rotated_area.setOriginRelative(0.5, 0.5);
		rotated_area.setRotation(FloatMath.toRadians(0 * -45));
		λimage = CV.map(λimage, input_area, rotated_area);

		final Rectangle wrap_area = Geometry.setupWrapingFrame(rotated_area.listVertices(), Geometry.newRectangle());
		wrap_area.setOriginRelative(0, 0);

		final Rectangle output_area = Geometry.newRectangle(1024, 1024);
		λimage = CV.map(λimage, wrap_area, output_area);
		saveResult(λimage, output_area, tager_file_name + "-" + color_seed + ".png");

	}

	private static void saveResult (final ColoredλImage image, final Rectangle rectangle, final String filename)
		throws IOException {

		final ColorMapSpecs lambda_specs = ImageProcessing.newColorMapSpecs();

		lambda_specs.setColorMapWidth((int)rectangle.getWidth());
		lambda_specs.setColorMapHeight((int)rectangle.getHeight());
		lambda_specs.setLambdaColoredImage(image);
		final ColorMap bw = ImageProcessing.newColorMap(lambda_specs);

		writeImage(bw, filename);
	}

	private static void writeImage (final ColorMap image, final String file_name) throws IOException {
		final BufferedImage gwt_bw = ImageAWT.toAWTImage(image);
		final File output_image_file = LocalFileSystem.ApplicationHome().child("output").child(file_name);
		L.d("writing", output_image_file);
		ImageAWT.writeToFile(gwt_bw, output_image_file, "png");
	}
}
