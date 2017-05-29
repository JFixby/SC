
package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.PhysicsCoreStator;
import com.jfixby.r3.api.physics.PhysicsCoreStatorSpecs;
import com.jfixby.r3.api.ui.unit.physics.PhysicsCoreDebugRenderer;
import com.jfixby.r3.api.ui.unit.physics.PhysicsCoreDebugRendererSpecs;

public interface PhysicsFactory {

	PhysicsCoreStatorSpecs newPhysicsCoreStatorSpecs ();

	PhysicsCoreStator newPhysicsCoreStator (PhysicsCoreStatorSpecs stator_properties);

	PhysicsCoreDebugRenderer newDebugRenderer (PhysicsCoreDebugRendererSpecs debug_renderer_properties);

	PhysicsCoreDebugRendererSpecs newDebugRendererSpecs ();

}
