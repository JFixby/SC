
package com.jfixby.r3.test.physics;

import java.util.ArrayList;

import com.jfixby.r3.api.physics.BODY_TYPE;
import com.jfixby.r3.api.physics.BoxBody;
import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.api.ui.unit.RootLayer;
import com.jfixby.r3.api.ui.unit.input.MouseMovedEvent;
import com.jfixby.r3.api.ui.unit.input.TouchDownEvent;
import com.jfixby.r3.api.ui.unit.input.TouchDraggedEvent;
import com.jfixby.r3.api.ui.unit.input.TouchUpEvent;
import com.jfixby.r3.api.ui.unit.user.MouseInputEventListener;
import com.jfixby.scarabei.api.angles.Angles;
import com.jfixby.scarabei.api.collisions.COLLISION_RELATION;
import com.jfixby.scarabei.api.collisions.CollisionCategory;
import com.jfixby.scarabei.api.collisions.CollisionRelations;
import com.jfixby.scarabei.api.collisions.Collisions;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;
import com.jfixby.scarabei.api.log.L;

public class CollisionTest extends PhysicsTest {
	public static final double m = PH.SIZE_FACTOR;

	public CollisionTest () {
		super();
	}

	private CollisionCategory cat_A;
	private CollisionCategory cat_B;
	private CollisionCategory cat_C;

	@Override
	public void deploy (final RootLayer root_layer, final ComponentsFactory factory) {
		this.define_collisions(root_layer, factory);
		root_layer.attachComponent(this.touch);

		this.attachWall(-0.5f, this.cat_A, m);
		this.attachWall(0f, this.cat_B, m);
		this.attachWall(0.5f, this.cat_C, m);

		this.getSurroundingBox().setSize(10.2f * m, 7.7f * m);

	}

	private void define_collisions (final RootLayer root_layer, final ComponentsFactory factory) {

		this.cat_A = Collisions.AtomicCategories().A();

		this.cat_B = Collisions.AtomicCategories().B();

		this.cat_C = Collisions.AtomicCategories().C();

	}

	public void attachWall (final double val, final CollisionCategory group, final double m) {
		final BoxBody wall = this.newBoxBody();
		wall.physics().setType(BODY_TYPE.STATIC);
		this.getPhysicsCore().attachBody(wall);

		wall.physics().setCollisionCategory(group);

		wall.physics().setType(BODY_TYPE.STATIC);
		wall.shape().setSize(this.getSurroundingBox().getWidth() * 1f * m, this.getSurroundingBox().getHeight() * 0.02f * m);
		wall.location().setRotation(Angles.newAngle());
		wall.location().setPosition(0, val * this.getSurroundingBox().getHeight() * 0.485f * m);

	}

	private final MouseInputEventListener touch = new MouseInputEventListener() {

		@Override
		public boolean onMouseMoved (final MouseMovedEvent event) {
			L.d(event);
			return false;
		}

		@Override
		public boolean onTouchDown (final TouchDownEvent input_event) {
			CollisionTest.this.addSnow(input_event.getCanvasPosition());
			return true;
		}

		@Override
		public boolean onTouchUp (final TouchUpEvent event) {
			return false;
		}

		@Override
		public boolean onTouchDragged (final TouchDraggedEvent input_event) {
			CollisionTest.this.addSnow(input_event.getCanvasPosition());
			return true;

		}

	};

	private void addSnow (final ReadOnlyFloat2 xy) {
		// Log.d("add", x + " " + y);
		final int K = 2;
		if (SnowFlake.counter % K == 2) {
			final SnowFlakePoly flake = new SnowFlakePoly(xy.getX(), xy.getY());
			this.getPhysicsCore().attachBody(flake.body);

			final CollisionRelations collision_ralations = flake.body.physics().getCollisionRelations();

			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_A);

			// flake.body.physics().getCollisionRelations()
			// .setPolicy(COLLISION_RELATION.COLLIDES_WITH, cat_A);

			this.snow.add(flake);
		} else if (SnowFlake.counter % K == 0) {
			final SnowFlakeRectangle flake = new SnowFlakeRectangle(xy.getX(), xy.getY());
			this.getPhysicsCore().attachBody(flake.body);
			final CollisionRelations collision_ralations = flake.body.physics().getCollisionRelations();

			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_B);
			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_A);
			// flake.body.physics().setRestitution(1f);
			this.snow.add(flake);
		} else if (SnowFlake.counter % K == 1) {

			final SnowFlakeCircle flake = new SnowFlakeCircle(xy.getX(), xy.getY());
			this.getPhysicsCore().attachBody(flake.body);

			final CollisionRelations collision_ralations = flake.body.physics().getCollisionRelations();

			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_A);
			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_B);
			collision_ralations.setPolicy(COLLISION_RELATION.DOES_NOT_COLLIDE_WITH, this.cat_C);
			// flake.body.physics().setRestitution(0.5f);
			this.snow.add(flake);
		}

	}

	@SuppressWarnings("rawtypes") final ArrayList<SnowFlake> snow = new ArrayList<SnowFlake>();

	@Override
	public double getGravityX () {
		return 0;
	}

	@Override
	public double getGravityY () {
		return 5;
	}
}
