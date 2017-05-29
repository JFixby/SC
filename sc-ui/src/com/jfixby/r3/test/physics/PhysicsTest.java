
package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.PhysicsCore;
import com.jfixby.r3.api.physics.PhysicsCoreRotor;
import com.jfixby.r3.api.physics.PhysicsCoreSpecs;
import com.jfixby.r3.api.physics.PhysicsCoreStator;
import com.jfixby.r3.api.physics.PhysicsCoreStatorSpecs;
import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.api.ui.unit.RootLayer;
import com.jfixby.r3.api.ui.unit.UnitManager;
import com.jfixby.r3.api.ui.unit.camera.Camera;
import com.jfixby.r3.api.ui.unit.physics.PhysicsCoreDebugRenderer;
import com.jfixby.r3.api.ui.unit.physics.PhysicsCoreDebugRendererSpecs;
import com.jfixby.scarabei.api.sys.Sys;

public abstract class PhysicsTest extends PH {

	SurroundingBox surroundingBox = new SurroundingBox();

	private Camera debug_renderer_camera;
	private PhysicsCore physicsCore;
	PhysicsFactory physics_department;

	public Camera getCamera () {
		return this.debug_renderer_camera;
	}

	public SurroundingBox getSurroundingBox () {
		return this.surroundingBox;
	}

	@Override
	public void onCreate (final UnitManager unitManager) {
		this.onCreate(unitManager.getRootLayer(), unitManager.getComponentsFactory());
	}

	public PhysicsCore getPhysicsCore () {
		return this.physicsCore;
	}

	public void onCreate (final RootLayer root_layer, final ComponentsFactory factory) {

// physics_department = Physics2D.getPhysicsDepartment();
		this.physics_department = null;

		final PhysicsCoreSpecs physics_core_config = this.newPhysicsCoreSpecs();
		final long how_many_milliseconds_will_pass_per_step_inside_the_core = 1000L / 25L;
		physics_core_config.setTimeDeltaPerStepInTheCore(how_many_milliseconds_will_pass_per_step_inside_the_core);

		final double gravity_x = this.getGravityX();
		final double gravity_y = this.getGravityY();

		physics_core_config.setGravity(gravity_x, gravity_y);
		this.physicsCore = this.newPhysicsCore(physics_core_config);

		final PhysicsCoreRotor rotor = this.physicsCore.getRotor();
		{
			final PhysicsCoreStatorSpecs stator_properties = this.physics_department.newPhysicsCoreStatorSpecs();
			stator_properties.setRotor(rotor);
			stator_properties.setStatorClock(Sys.SystemTime());
			// stator_properties.setCriticalOverheatDelay(1000L);

			final long how_many_milliseconds_will_pass_per_step_in_our_universe = 5L;
			stator_properties.setTimeDeltaPerStepInOurUniverse(how_many_milliseconds_will_pass_per_step_in_our_universe);

			final PhysicsCoreStator stator = this.physics_department.newPhysicsCoreStator(stator_properties);

// root_layer.attachComponent(stator);
		}

		{

			final PhysicsCoreDebugRendererSpecs debug_renderer_properties = this.physics_department.newDebugRendererSpecs();
// debug_renderer_properties.setPhysicsCore(this.physicsCore);
			final PhysicsCoreDebugRenderer debug_renderer = this.physics_department.newDebugRenderer(debug_renderer_properties);

			this.debug_renderer_camera = debug_renderer.getCamera();

			this.debug_renderer_camera.setOriginRelative(0.5, 0.5);
			this.debug_renderer_camera.setPosition(0, 0);
			this.debug_renderer_camera.setZoom(50 / SIZE_FACTOR);

			// debug_renderer.setIUserInputListener(touch);
			// root_layer.setCamera(debug_renderer_camera);
			root_layer.attachComponent(debug_renderer);
		}

		// root_layer.attachComponent(viewport_listener);
		// root_layer.attachComponent(updater);
		this.surroundingBox.attach_ground(this.physicsCore);

		this.deploy(root_layer, factory);

	}

	public abstract double getGravityX ();

	public abstract double getGravityY ();

	public abstract void deploy (RootLayer root_layer, ComponentsFactory factory);

	// final U_ViewportListener viewport_listener = new U_ViewportListener() {
	//
	// @Override
	// public void onViewportUpdated(final ViewportUpdate viewportUpdater) {
	//
	// final double canvas_width = viewportUpdater.getViewportWidth();
	// final double canvas_height = viewportUpdater.getViewportHeight();
	//
	// final double canvas_ratio = viewportUpdater.getWidthToHeightRatio();
	//
	// debug_renderer_camera.setSize(surroundingBox.getHeight()
	// * canvas_ratio, surroundingBox.getHeight());
	// debug_renderer_camera.setPosition(0, 0);
	//
	// }
	//
	// };

	@Override
	public void onResume () {
	}

	@Override
	public void onPause () {
	}

	@Override
	public void onDestroy () {
	}
}
