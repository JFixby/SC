
package com.jfixby.r3.test.physics;

import java.io.IOException;

import com.jfixby.r3.api.physics.BODY_TYPE;
import com.jfixby.r3.api.physics.PolyBody;
import com.jfixby.rana.api.asset.AssetHandler;
import com.jfixby.rana.api.asset.AssetsConsumer;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.asset.LoadedAssets;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;

public class SnowFlakePoly extends SnowFlake<PolyBody> implements AssetsConsumer {

	public SnowFlakePoly (final double x, final double y) {

		this.body = this.create_body();
		this.body.physics().setType(BODY_TYPE.DYNAMIC);
		this.body.location().setPosition(x, y);

		this.body.debugInfo().setName("SnowFlake #" + counter);
		// body.listeners().setOnTimeUpdateListener(time_update_listener);

		counter++;
		L.d("SnowFlake", counter);

	}

	// com.jfixby.red.g7.testing.physics.phtest.shapes.

	@Override
	public PolyBody create_body () {

		final ID star1 = Names.newID("com.jfixby.red.test.physics.star1");
		final ID star2 = Names.newID("com.jfixby.red.test.physics.star2");
		final ID ball = Names.newID("com.jfixby.red.test.physics.ball");
		final ID shifted = Names.newID("com.jfixby.red.test.physics.shifted");
		final ID testA = Names.newID("com.jfixby.red.test.physics.testA");
		final ID strange = Names.newID("com.jfixby.red.test.physics.strange");
		final ID strange2 = Names.newID("com.jfixby.red.test.physics.strange2");
		final ID testB = Names.newID("com.jfixby.red.test.spineboy.skelet.front.fist.shape");
		final ID R = Names.newID("com.jfixby.red.test.physics.R");

		final ID chosen_asset_id = R;
		// combined = Geometry.newCombinedGeometry();
		try {
			AssetsManager.autoResolveAsset(chosen_asset_id);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		final AssetHandler reg = LoadedAssets.obtainAsset(chosen_asset_id, this);

		final ShapeData poly_specs = (ShapeData)reg.asset();

		// body = newPolyBody(poly_specs.getShapeSpecs());

		// return body;
		Err.throwNotImplementedYet();
		return this.body;

	}

}
