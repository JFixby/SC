
package com.jfixby.sc.tools.map.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.io.InputStream;
import com.jfixby.scarabei.api.log.L;

public class RunPrintPatterns {

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
		final MaskColoriser colorizer = new MaskColoriser();
		ColoredλImage λimage = isom.getDebugImage(colorizer);
		final int scale_factor = 4;
		final int strech_x = 2;
		λimage = CV.scale(λimage, scale_factor);
		λimage = CV.scale(λimage, strech_x, 1);
		final int w = isom.getWidth() * strech_x;
		final int h = isom.getHeight();

		saveResult(λimage, (w * scale_factor), (h * scale_factor), tager_file_name + ".png");

	}

	private static void saveResult (final ColoredλImage image, final long w, final long h, final String filename)
		throws IOException {

		final ColorMapSpecs lambda_specs = ImageProcessing.newColorMapSpecs();

		lambda_specs.setColorMapWidth((int)w);
		lambda_specs.setColorMapHeight((int)h);
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
