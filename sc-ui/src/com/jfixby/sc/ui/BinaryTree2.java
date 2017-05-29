
package com.jfixby.sc.ui;

import com.jfixby.r3.api.ui.unit.ComponentsFactory;
import com.jfixby.r3.api.ui.unit.layer.Layer;
import com.jfixby.r3.ext.scene2d.api.Scene2DComponent;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;

public class BinaryTree2 implements LayersTree {
	private final ComponentsFactory components_factory;
	private final Map<Scene2DComponent, Node> adress = Collections.newMap();

	public BinaryTree2 (final ComponentsFactory components_factory) {
		this.components_factory = components_factory;
		this.root_layer = components_factory.newLayer();
		this.root = new Node();
		this.root_layer.attachComponent(this.root.base);
	}

	final Layer root_layer;
	final Node root;

	@Override
	public void attachComponent (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
		Debug.checkNull("scene", scene);
		Debug.checkNull("canvas_position", canvas_position);
		this.root.push(scene, canvas_position);

	}

	@Override
	public void detatchComponent (final Scene2DComponent scene) {
		final Node remove_node = this.adress.get(scene);
		this.adress.remove(scene);
		if (remove_node.right == null && remove_node.left == null) {
			remove_node.clear();
		}
		if (remove_node.right != null && remove_node.left == null) {
			final Node next = remove_node.right;
			remove_node.right_mount.detatchAllComponents();
			remove_node.center_mount.detatchAllComponents();
			remove_node.left_mount.detatchAllComponents();
			remove_node.left = next.left;
			remove_node.right = next.right;
			remove_node.scene = next.scene;
			remove_node.canvas_position = next.canvas_position;
			remove_node.left_mount.attachComponents(next.left_mount.listChildren());
			remove_node.right_mount.attachComponents(next.right_mount.listChildren());
			remove_node.center_mount.attachComponents(next.center_mount.listChildren());
		}
		if (remove_node.right == null && remove_node.left != null) {
			final Node next = remove_node.left;
			remove_node.right_mount.detatchAllComponents();
			remove_node.center_mount.detatchAllComponents();
			remove_node.left_mount.detatchAllComponents();
			remove_node.left = next.left;
			remove_node.right = next.right;
			remove_node.scene = next.scene;
			remove_node.canvas_position = next.canvas_position;
			remove_node.left_mount.attachComponents(next.left_mount.listChildren());
			remove_node.right_mount.attachComponents(next.right_mount.listChildren());
			remove_node.center_mount.attachComponents(next.center_mount.listChildren());
		}
		if (remove_node.right != null && remove_node.left != null) {
			final Node next = remove_node.left;
			remove_node.right_mount.detatchAllComponents();
			remove_node.center_mount.detatchAllComponents();
			remove_node.left_mount.detatchAllComponents();
			remove_node.left = next.left;
			remove_node.right = next.right;
			remove_node.scene = next.scene;
			remove_node.canvas_position = next.canvas_position;
			remove_node.left_mount.attachComponents(next.left_mount.listChildren());
			remove_node.right_mount.attachComponents(next.right_mount.listChildren());
			remove_node.center_mount.attachComponents(next.center_mount.listChildren());
		}
		if (remove_node.scene != null) {
			this.adress.put(remove_node.scene, remove_node);
		}
		// node.unSetScene(scene);
		// if (node.right != null) {
		// getRemoveSumstitute(node.right);
		// }
	}

	class Node {
		final Layer base = BinaryTree2.this.components_factory.newLayer();
		final Layer left_mount = BinaryTree2.this.components_factory.newLayer();
		final Layer center_mount = BinaryTree2.this.components_factory.newLayer();
		final Layer right_mount = BinaryTree2.this.components_factory.newLayer();
		private Scene2DComponent scene;
		private ReadOnlyFloat2 canvas_position;

		Node left = null;
		Node right = null;

		public Node () {
			this.base.closeInputValve();
			this.base.attachComponent(this.left_mount);
			this.base.attachComponent(this.center_mount);
			this.base.attachComponent(this.right_mount);
		}

		public void clear () {
			this.scene = null;
			this.canvas_position = null;
			this.center_mount.detatchAllComponents();

		}

		public void unSetScene (final Scene2DComponent scene) {

		}

		public void push (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
			if (this.isEmpty()) {
				this.setScene(scene, canvas_position);
				return;
			}
			final double new_y = canvas_position.getY();
			final double current_y = this.canvas_position.getY();
			if (current_y > new_y) {
				this.pushLeft(scene, canvas_position);
			} else {
				this.pushRight(scene, canvas_position);
			}
		}

		private void pushRight (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
			if (this.right == null) {
				this.right = new Node();
				this.right_mount.attachComponent(this.right.base);
			}
			this.right.push(scene, canvas_position);
		}

		private void pushLeft (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
			if (this.left == null) {
				this.left = new Node();
				this.left_mount.attachComponent(this.left.base);
			}
			this.left.push(scene, canvas_position);
		}

		public void setScene (final Scene2DComponent scene, final ReadOnlyFloat2 canvas_position) {
			this.scene = scene;
			this.canvas_position = canvas_position;
			this.center_mount.attachComponent(scene);
			BinaryTree2.this.adress.put(scene, this);
		}

		public boolean isEmpty () {
			return this.canvas_position == null;
		}

	}

	@Override
	public Layer getRoot () {
		return this.root_layer;
	}
}
