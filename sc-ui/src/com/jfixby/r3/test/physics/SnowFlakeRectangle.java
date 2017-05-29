package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.BoxBody;

public class SnowFlakeRectangle extends SnowFlake<BoxBody> {

	public SnowFlakeRectangle( double x,
			double y) {
		super( x, y);

	}

	@Override
	public BoxBody create_body() {
		body = newBoxBody();
		body.shape().setWidth(size * 4);
		body.shape().setHeight(size * 2);
		return body;
	}

}
