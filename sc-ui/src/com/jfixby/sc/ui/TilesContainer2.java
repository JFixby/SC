
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.layer.Layer;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;

public class TilesContainer2 {

	private Layer root;

	public void setRoot (final Layer root) {
		this.root = root;
	}

	public void detatchComponent (final Scene2DComponent scene) {
		this.root.detatchComponent(scene);
	}

	public void attachComponent (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
		this.root.attachComponent(scene);
	}

}
