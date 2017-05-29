package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.CircleBody;

public class SnowFlakeCircle extends SnowFlake<CircleBody> {

	public SnowFlakeCircle(double x, double y) {
		super( x, y);

	}

	@Override
	public CircleBody create_body() {
		body = newCircleBody();
		body.shape().setRadius(size);

		return body;
	}

	

}
