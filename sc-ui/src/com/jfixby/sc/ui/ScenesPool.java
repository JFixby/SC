
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.collections.Pool;
import com.jfixby.scarabei.api.debug.Debug;

public class ScenesPool {

	final Map<ID, Pool<Scene2DComponent>> pools = Collections.newMap();
	final Map<Scene2DComponent, ID> ids = Collections.newMap();

	public void dispose (final Scene2DComponent scene) {
		final ID id = this.ids.remove(scene);
		Debug.checkNull("id", id);
		final Pool<Scene2DComponent> pool = this.pools.get(id);
		pool.free(scene);
	}

	public Scene2DComponent spawn (final ComponentsFactory components_factory, final ID name) {
		Pool<Scene2DComponent> pool = this.pools.get(name);
		if (pool == null) {
			final SceneSpawner spawner = new SceneSpawner(name, components_factory);
			pool = Collections.newPool(spawner);
			this.pools.put(name, pool);
		}
		final Scene2DComponent scene = pool.obtain();
		this.ids.put(scene, name);
		return scene;
	}

}
