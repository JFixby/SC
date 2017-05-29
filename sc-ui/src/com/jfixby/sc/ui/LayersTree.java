
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.layer.Layer;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;

public interface LayersTree {

	void attachComponent (Scene2DComponent scene, ReadOnlyFloat2 canvas_position);

	void detatchComponent (Scene2DComponent scene);

	Layer getRoot ();

}
