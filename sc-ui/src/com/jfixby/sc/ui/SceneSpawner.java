
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.r3.ext.scene2d.api.Scene2DSpawner;
import com.jfixby.r3.ext.scene2d.api.Scene2DSpawningConfig;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.PoolElementsSpawner;

public class SceneSpawner implements PoolElementsSpawner<Scene2DComponent> {

	private final ID id;
	private final ComponentsFactory components_factory;

	public SceneSpawner (final ID id, final ComponentsFactory components_factory) {
		this.id = id;
		this.components_factory = components_factory;
	}

	@Override
	public Scene2DComponent spawnNewInstance () {
		final Scene2DSpawningConfig config = new Scene2DSpawningConfig();
		config.structureID = (this.id);
		final Scene2DComponent scene = Scene2DSpawner.spawnScene(this.components_factory, config);
		scene.startAllAnimations();
		return scene;
	}

}
