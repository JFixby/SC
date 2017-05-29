
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.layer.Layer;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;

public class TilesContainer implements LayersTree {
	class SceneNode {
		Scene2DComponent scene;
		ReadOnlyFloat2 canvas_position;

	}

	final private Layer root;
	List<SceneNode> scenes = Collections.newList();

	public TilesContainer (final Layer layer) {
		this.root = layer;
	}

	@Override
	public void detatchComponent (final Scene2DComponent scene) {
		for (int i = 0; i < this.scenes.size(); i++) {
			final SceneNode element = this.scenes.getElementAt(i);
			if (element.scene == scene) {
				this.scenes.remove(element);
				break;
			}
		}
		this.reorder();
	}

	private void reorder () {
		this.root.detatchAllComponents();
		for (int i = 0; i < this.scenes.size(); i++) {
			final SceneNode element = this.scenes.getElementAt(i);
			this.root.attachComponent(element.scene);
		}
	}

	@Override
	public void attachComponent (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
		boolean done = false;
		for (int i = 0; i < this.scenes.size(); i++) {
			final SceneNode element = this.scenes.getElementAt(i);
			if (element.canvas_position.getY() > canvas_position.getY()) {
				this.insert(scene, canvas_position, i);
				done = true;
				break;
			}
		}
		if (!done) {
			this.insert(scene, canvas_position, 0);
		}
		this.reorder();
	}

	private void insert (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position, final int i) {
		final SceneNode e = new SceneNode();
		e.canvas_position = canvas_position;
		e.scene = scene;
		this.scenes.insertElementAt(e, i);
	}

	@Override
	public Layer getRoot () {
		return null;
	}

}
