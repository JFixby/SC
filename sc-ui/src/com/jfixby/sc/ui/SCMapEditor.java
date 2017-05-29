
package com.jfixby.sc.ui;

import java.util.Comparator;

import com.jfixby.examples.wdgs.WDGS_Pizza_Palette;
import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.api.ui.unit.DefaultUnit;
import com.jfixby.r3.api.ui.unit.RootLayer;
import com.jfixby.r3.api.ui.unit.UnitManager;
import com.jfixby.r3.api.ui.unit.camera.Camera;
import com.jfixby.r3.api.ui.unit.camera.CameraSpecs;
import com.jfixby.r3.api.ui.unit.camera.SIMPLE_CAMERA_POLICY;
import com.jfixby.r3.api.ui.unit.input.CharTypedEvent;
import com.jfixby.r3.api.ui.unit.input.InputManager;
import com.jfixby.r3.api.ui.unit.input.KeyDownEvent;
import com.jfixby.r3.api.ui.unit.input.MouseMovedEvent;
import com.jfixby.r3.api.ui.unit.input.MouseScrolledEvent;
import com.jfixby.r3.api.ui.unit.input.TouchDownEvent;
import com.jfixby.r3.api.ui.unit.input.TouchDraggedEvent;
import com.jfixby.r3.api.ui.unit.input.TouchUpEvent;
import com.jfixby.r3.api.ui.unit.layer.LayerUtils;
import com.jfixby.r3.api.ui.unit.layer.TreeLayer;
import com.jfixby.r3.api.ui.unit.user.KeyboardInputEventListener;
import com.jfixby.r3.api.ui.unit.user.MouseInputEventListener;
import com.jfixby.r3.ext.api.patch18.palette.Fabric;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;
import com.jfixby.scarabei.api.geometry.Geometry;
import com.jfixby.scarabei.api.geometry.ORIGIN_RELATIVE_HORIZONTAL;
import com.jfixby.scarabei.api.geometry.ORIGIN_RELATIVE_VERTICAL;
import com.jfixby.scarabei.api.geometry.projections.OffsetProjection;
import com.jfixby.scarabei.api.input.Key;
import com.jfixby.scarabei.api.input.UserInput;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.utl.pizza.api.Pizza;
import com.jfixby.utl.pizza.api.PizzaLandscape;
import com.jfixby.utl.pizza.api.PizzaLandscapeFactory;
import com.jfixby.utl.pizza.api.PizzaLandscapeListener;
import com.jfixby.utl.pizza.api.PizzaLandscapeSpecs;
import com.jfixby.utl.pizza.api.PizzaPalette;
import com.jfixby.utl.pizza.api.PizzaTile;
import com.jfixby.utl.pizza.api.PizzaTileType;

public class SCMapEditor extends DefaultUnit implements InputManager {
	PizzaPalette wdgs_pizza_palette = WDGS_Pizza_Palette.PALETTE;

	final Map<ReadOnlyFloat2, Scene2DComponent> scenes = Collections.newMap();

	private RootLayer root;
	private ComponentsFactory components_factory;
	private PizzaLandscape pizza_scape;

	// LayersTree tiles_container;

	private Camera cam;

	private TreeLayer<ReadOnlyFloat2> tree;

	@Override
	public void onCreate (final UnitManager unitManager) {
		super.onCreate(unitManager);

		this.root = unitManager.getRootLayer();
		this.components_factory = unitManager.getComponentsFactory();

		{
			final CameraSpecs cam_spec = this.components_factory.getCameraDepartment().newCameraSpecs();
			cam_spec.setSimpleCameraPolicy(SIMPLE_CAMERA_POLICY.EXPAND_CAMERA_VIEWPORT_ON_SCREEN_RESIZE);

			this.cam = this.components_factory.getCameraDepartment().newCamera(cam_spec);
			this.root.setCamera(this.cam);
			this.cam.setOriginRelative(ORIGIN_RELATIVE_HORIZONTAL.CENTER, ORIGIN_RELATIVE_VERTICAL.CENTER);
			this.cam.setPosition(0, 0);
// this.cam.setZoom(0.3);
		}

		{

			final PizzaLandscapeFactory landscape_fac = Pizza.invoke().getPizzaLandscapeFactory();

			final PizzaLandscapeSpecs landscape_specs = landscape_fac.newLandscapeSpecs();
			landscape_specs.setActiveArea(0, 0, 1024 * 4, 1024 * 3);
			landscape_specs.setPalette(this.wdgs_pizza_palette);

			this.pizza_scape = landscape_fac.newPizzaLandscape(landscape_specs);
			this.pizza_scape.setLandscapeListener(this.listener);

		}

		this.root.attachComponent(this.mouse_input);
		this.root.attachComponent(this.keyboard_input);

		final Comparator<ReadOnlyFloat2> comparator = new Comparator<ReadOnlyFloat2>() {
			@Override
			public int compare (final ReadOnlyFloat2 o1, final ReadOnlyFloat2 o2) {
				return Double.compare(o1.getY(), o2.getY());
			}
		};
		this.tree = LayerUtils.newTree(this.components_factory, comparator);

		// tiles_container = new BinaryTree(components_factory);
		// Layer layer = tiles_container.getRoot();
		// root.attachComponent(layer);

		this.root.attachComponent(this.tree);
	}

	private final MouseInputEventListener mouse_input = new MouseInputEventListener() {

		@Override
		public boolean onMouseScrolled (final MouseScrolledEvent event) {

			if (event.getScrollValue() < 0) {
				SCMapEditor.this.cam.setZoom(SCMapEditor.this.cam.getZoom() * 1.5);
			}

			if (event.getScrollValue() > 0) {
				SCMapEditor.this.cam.setZoom(SCMapEditor.this.cam.getZoom() / 1.5);
			}

			return true;
		}

		@Override
		public boolean onMouseMoved (final MouseMovedEvent input_event) {

			return true;
		}

		@Override
		public boolean onTouchDown (final TouchDownEvent input_event) {
			final ReadOnlyFloat2 canvas_xy = input_event.getCanvasPosition();
			// L.d();
			// L.d("canvas_xy", canvas_xy);
			SCMapEditor.this.pizza_scape.getBrush()//
				.pointAtCanvas(canvas_xy);
			SCMapEditor.this.pizza_scape.getBrush()//
				.applyPaint();
// SCMapEditor.this.root.print();
			return true;
		}

		@Override
		public boolean onTouchUp (final TouchUpEvent input_event) {
			return false;
		}

		@Override
		public boolean onTouchDragged (final TouchDraggedEvent input_event) {
			final ReadOnlyFloat2 canvas_xy = input_event.getCanvasPosition();
			// L.d();
			// L.d("canvas_xy", canvas_xy);
			SCMapEditor.this.pizza_scape.getBrush()//
				.pointAtCanvas(canvas_xy);
			SCMapEditor.this.pizza_scape.getBrush()//
				.applyPaint();
			return true;
		}
	};

	KeyboardInputEventListener keyboard_input = new KeyboardInputEventListener() {

		@Override
		public boolean onKeyDown (final KeyDownEvent e) {
			final Key key = e.getKey();
			L.d("key", key);
			final double delta = SCMapEditor.this.cam.getWidth() / 10d;

			if (UserInput.Keyboard().NUM_0() == key) {
				if (e.isKeyPressed(UserInput.Keyboard().CONTROL_LEFT())) {
					SCMapEditor.this.cam.setZoom(1);
				}
			}

			if (UserInput.Keyboard().DPAD_LEFT() == key) {
				SCMapEditor.this.cam.setPositionX(SCMapEditor.this.cam.getPositionX() - delta);
			}
			if (UserInput.Keyboard().DPAD_RIGHT() == key) {
				SCMapEditor.this.cam.setPositionX(SCMapEditor.this.cam.getPositionX() + delta);
			}
			if (UserInput.Keyboard().DPAD_UP() == key) {
				SCMapEditor.this.cam.setPositionY(SCMapEditor.this.cam.getPositionY() - delta);
			}
			if (UserInput.Keyboard().DPAD_DOWN() == key) {
				SCMapEditor.this.cam.setPositionY(SCMapEditor.this.cam.getPositionY() + delta);
			}

			return true;
		}

		@Override
		public boolean onCharTyped (final CharTypedEvent e) {
			final char char_typed = e.getChar();
			L.d("char_typed", char_typed);
			if (char_typed == '+') {
				SCMapEditor.this.cam.setZoom(SCMapEditor.this.cam.getZoom() * 1.5);

			}

			if (char_typed == '-') {
				SCMapEditor.this.cam.setZoom(SCMapEditor.this.cam.getZoom() / 1.5);
			}
			if (char_typed == '1') {
				final int fabric_index = 1;
				final Fabric fabric = SCMapEditor.this.wdgs_pizza_palette.listFabrics().getFabric(fabric_index);
				SCMapEditor.this.pizza_scape.getBrush().setFabric(fabric);
			}
			if (char_typed == '2') {
				final int fabric_index = 2;
				final Fabric fabric = SCMapEditor.this.wdgs_pizza_palette.listFabrics().getFabric(fabric_index);
				SCMapEditor.this.pizza_scape.getBrush().setFabric(fabric);
			}
			if (char_typed == '3') {
				final int fabric_index = 3;
				final Fabric fabric = SCMapEditor.this.wdgs_pizza_palette.listFabrics().getFabric(fabric_index);
				SCMapEditor.this.pizza_scape.getBrush().setFabric(fabric);
			}
			if (char_typed == '0') {
				final int fabric_index = 0;
				final Fabric fabric = SCMapEditor.this.wdgs_pizza_palette.listFabrics().getFabric(fabric_index);
				SCMapEditor.this.pizza_scape.getBrush().setFabric(fabric);
			}

			return true;
		}

	};

	ScenesPool pool = new ScenesPool();

	private final PizzaLandscapeListener listener = new PizzaLandscapeListener() {

		@Override
		public void onBlockRemove (final PizzaTile block) {
			final PizzaTileType type = block.getType();
			final ReadOnlyFloat2 canvas_position = block.getCanvasPosition();
			final Scene2DComponent scene = SCMapEditor.this.scenes.remove(canvas_position);
			if (scene == null) {
				SCMapEditor.this.scenes.print("scenes");
				Err.reportError("Corruption");
			}
			SCMapEditor.this.tree.detatchComponent(scene);
			SCMapEditor.this.pool.dispose(scene);

		}

		@Override
		public void onBlockAdd (final PizzaTile block) {
			final PizzaTileType type = block.getType();
			final ReadOnlyFloat2 canvas_position = block.getCanvasPosition();
			L.d("Add block at", canvas_position);
			final ID name = block.getType().getID();
			final Scene2DComponent scene = SCMapEditor.this.pool.spawn(SCMapEditor.this.components_factory, name);
			final OffsetProjection offset = Geometry.getProjectionFactory().newOffset();
			offset.setOffsetXY(canvas_position.getX(), canvas_position.getY());
			scene.getRoot().setProjection(offset);

// scene.setDebugRenderFlag(false);
			SCMapEditor.this.scenes.put(canvas_position, scene);
			SCMapEditor.this.tree.attachComponent(scene, canvas_position);
// scene.setPosition(canvas_position);
		}

		@Override
		public void onBlockFocus (final PizzaTile block) {
			// FixedFloat2 canvas_position = block.getCanvasPosition();
			// L.d("canvas_position", canvas_position);

		}

	};

	@Override
	public void enableInput () {
	}
}
