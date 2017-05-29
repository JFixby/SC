package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.BODY_TYPE;
import com.jfixby.r3.api.physics.Body;
import com.jfixby.r3.api.physics.BoxBody;
import com.jfixby.r3.api.physics.CircleBody;
import com.jfixby.r3.api.physics.Physics2D;
import com.jfixby.r3.api.physics.PolyBody;
import com.jfixby.r3.api.physics.PolyBodySpecs;
import com.jfixby.scarabei.api.log.L;

public abstract class SnowFlake<T extends Body> {
	public final static double m = PH.SIZE_FACTOR;
	public final static double size = 0.1 * m * 1 + 0 * 0.06;

	public static int counter = 0;
	public T body;

	SnowFlake() {
		super();
	}

	public SnowFlake(double x, double y) {
		body = create_body();
		body.physics().setType(BODY_TYPE.DYNAMIC);
		body.location().setPosition(x, y);
		body.mass().setMass(1d);
		body.mass().setInertia(1d);
		body.debugInfo().setName("SnowFlake #" + counter);
		// body.listeners().setOnTimeUpdateListener(time_update_listener);
		// body.physics().setFriction(1);
		// body.physics().setRestitution(1);
		// body.physics().setDensity(1);
		counter++;
		L.d("SnowFlake", counter + " size=" + size);
	}

	public abstract T create_body();

	public CircleBody newCircleBody() {
		return Physics2D.newCircleBody();
	}

	public PolyBody newPolyBody(PolyBodySpecs shapeSpecs) {
		return Physics2D.newPolyBody(shapeSpecs);
	}

	public BoxBody newBoxBody() {
		return Physics2D.newBoxBody();
	}

}
