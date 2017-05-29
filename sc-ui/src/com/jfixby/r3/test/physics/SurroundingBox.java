package com.jfixby.r3.test.physics;

import com.jfixby.r3.api.physics.BODY_TYPE;
import com.jfixby.r3.api.physics.Body;
import com.jfixby.r3.api.physics.BoxBody;
import com.jfixby.r3.api.physics.Physics2D;
import com.jfixby.r3.api.physics.PhysicsCore;
import com.jfixby.scarabei.api.angles.Angles;

public class SurroundingBox {
	private double box_w = 10f;
	private double box_h = 7.6f;
	BoxBody wall_down;
	BoxBody wall_right;
	BoxBody wall_left;
	BoxBody wall_up;
	private PhysicsCore physicsCore;

	public double getWidth() {
		return box_w;
	}

	public double getHeight() {
		return box_h;
	}

	public void attach_ground(final PhysicsCore physicsCore) {
		this.physicsCore = physicsCore;
		wall_down = newBoxBody();
		wall_down.physics().setType(BODY_TYPE.STATIC);
		physicsCore.attachBody(wall_down);
		wall_down.debugInfo().setName("wall_down");

		// wall_down.setContactListener(down_wall_listener);

		wall_right = newBoxBody();
		wall_right.physics().setType(BODY_TYPE.STATIC);
		physicsCore.attachBody(wall_right);
		wall_right.debugInfo().setName("wall_right");

		wall_left = newBoxBody();
		wall_left.physics().setType(BODY_TYPE.STATIC);
		physicsCore.attachBody(wall_left);
		wall_left.debugInfo().setName("wall_left");

		wall_up = newBoxBody();
		wall_up.physics().setType(BODY_TYPE.STATIC);
		physicsCore.attachBody(wall_up);
		wall_up.debugInfo().setName("wall_up");

	}

	public BoxBody newBoxBody() {
		return Physics2D.newBoxBody();
	}

	public BoxBody getDownWall() {
		return this.wall_down;
	}

	public void setSize(double box_w, double box_h) {
		this.box_w = box_w;
		this.box_h = box_h;
		wall_down.physics().setType(BODY_TYPE.STATIC);
		wall_down.shape().setSize(box_w * 1f, box_h * 0.02f);
		wall_down.location().setRotation(Angles.newAngle());
		wall_down.location().setPosition(0, box_h * 0.485f);

		wall_up.physics().setType(BODY_TYPE.STATIC);
		wall_up.shape().setSize(box_w * 1f, box_h * 0.02f);
		wall_up.location().setRotation(Angles.newAngle());
		wall_up.location().setPosition(0, -box_h * 0.485f);

		wall_left.physics().setType(BODY_TYPE.STATIC);
		wall_left.shape().setSize(box_w * 0.02f, box_h * 1f);
		wall_left.location().setRotation(Angles.newAngle());
		wall_left.location().setPosition(box_w * 0.485f, 0f);

		wall_right.physics().setType(BODY_TYPE.STATIC);
		wall_right.shape().setSize(box_w * 0.02f, box_h * 1f);
		wall_right.location().setRotation(Angles.newAngle());
		wall_right.location().setPosition(-box_w * 0.485f, 0f);
	}

	public void detatch() {

		physicsCore.detatchBody(wall_down);
		physicsCore.detatchBody(wall_up);
		physicsCore.detatchBody(wall_right);
		physicsCore.detatchBody(wall_left);
	}

	public Body getUpWall() {
		return wall_up;
	}

	public Body getRightWall() {
		return wall_right;
	}

	public Body getLeftWall() {
		return wall_left;
	}
}
