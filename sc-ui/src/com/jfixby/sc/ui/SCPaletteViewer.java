
package com.jfixby.sc.ui;

import com.jfixby.examples.wdgs.WDGS_Pizza_Palette;
import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.api.ui.unit.DefaultUnit;
import com.jfixby.r3.api.ui.unit.RootLayer;
import com.jfixby.r3.api.ui.unit.UnitManager;
import com.jfixby.r3.api.ui.unit.camera.Camera;
import com.jfixby.r3.api.ui.unit.camera.CameraSpecs;
import com.jfixby.r3.api.ui.unit.camera.SIMPLE_CAMERA_POLICY;
import com.jfixby.r3.api.ui.unit.geometry.RectangleComponent;
import com.jfixby.r3.api.ui.unit.input.CharTypedEvent;
import com.jfixby.r3.api.ui.unit.user.KeyboardInputEventListener;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.r3.ext.scene2d.api.Scene2DSpawner;
import com.jfixby.r3.ext.scene2d.api.Scene2DSpawningConfig;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.geometry.ORIGIN_RELATIVE_HORIZONTAL;
import com.jfixby.scarabei.api.geometry.ORIGIN_RELATIVE_VERTICAL;
import com.jfixby.utl.pizza.api.PizzaPalette;
import com.jfixby.utl.pizza.api.PizzaTilesList;

public class SCPaletteViewer extends DefaultUnit {
	PizzaPalette pizza_palette = WDGS_Pizza_Palette.PALETTE;

	private RootLayer root;
	private ComponentsFactory components_factory;

	private PizzaTilesList tiles;

	@Override
	public void onCreate (final UnitManager unitManager) {
		super.onCreate(unitManager);
		this.root = unitManager.getRootLayer();
		this.components_factory = unitManager.getComponentsFactory();

		final CameraSpecs cam_spec = this.components_factory.getCameraDepartment().newCameraSpecs();
		cam_spec.setSimpleCameraPolicy(SIMPLE_CAMERA_POLICY.EXPAND_CAMERA_VIEWPORT_ON_SCREEN_RESIZE);

		final Camera cam = this.components_factory.getCameraDepartment().newCamera(cam_spec);
		this.root.setCamera(cam);
		cam.setOriginRelative(ORIGIN_RELATIVE_HORIZONTAL.CENTER, ORIGIN_RELATIVE_VERTICAL.CENTER);
		cam.setPosition(0, 0);
		{
			final RectangleComponent rectangleA = this.components_factory.getGeometryDepartment().newRectangle();
			this.root.attachComponent(rectangleA);
			rectangleA.setSize(1024, 768);
			rectangleA.setPosition(0, 0);
		}
		{
			final RectangleComponent rectangleB = this.components_factory.getGeometryDepartment().newRectangle();
			this.root.attachComponent(rectangleB);
			rectangleB.setOriginRelative(1, 1);
			rectangleB.setSize(1024, 768);
			rectangleB.setPosition(0, 0);
		}

		this.tiles = this.pizza_palette.listTiles();
		// variations.print("all variations");

// root.print();
// Sys.exit();

		this.root.attachComponent(this.input);
		this.resetScenes();

	}

	KeyboardInputEventListener input = new KeyboardInputEventListener() {

		@Override
		public boolean onCharTyped (final CharTypedEvent e) {
			final char char_typed = e.getChar();
			if (char_typed == '.') {
				SCPaletteViewer.this.index++;
			}
			if (char_typed == ',') {
				SCPaletteViewer.this.index--;
			}
			if (SCPaletteViewer.this.index < 0) {
				SCPaletteViewer.this.index = SCPaletteViewer.this.tiles.size() - 1;
			}
			if (SCPaletteViewer.this.index >= SCPaletteViewer.this.tiles.size()) {
				SCPaletteViewer.this.index = 0;
			}
			SCPaletteViewer.this.resetScenes();
			return true;
		}

	};

	int index = 0;
	Scene2DComponent current_scene = null;

	private void resetScenes () {
		if (this.current_scene != null) {
			this.root.detatchComponent(this.current_scene);
		}
		final ID name = this.tiles.getElementAt(this.index).getID();

		final Scene2DSpawningConfig config = new Scene2DSpawningConfig();
		config.structureID = (name);

		this.current_scene = Scene2DSpawner.spawnScene(this.components_factory, config);
		this.current_scene.startAllAnimations();
		this.root.attachComponent(this.current_scene);
	}
}
