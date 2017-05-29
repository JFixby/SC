
package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.BoxBody;
import com.jfixby.r3.api.physics.Physics2D;
import com.jfixby.r3.api.physics.PhysicsCore;
import com.jfixby.r3.api.physics.PhysicsCoreSpecs;
import com.jfixby.r3.api.ui.unit.DefaultUnit;

public class PH extends DefaultUnit {
	public static final double SIZE_FACTOR = 1;

	public PH () {

	}

	public PhysicsCore newPhysicsCore (final PhysicsCoreSpecs physics_core_config) {
		return Physics2D.newPhysicsCore(physics_core_config);
	}

	public PhysicsCoreSpecs newPhysicsCoreSpecs () {
		return Physics2D.newPhysicsCoreSpecs();
	}

	public BoxBody newBoxBody () {
		return Physics2D.newBoxBody();
	}
}
