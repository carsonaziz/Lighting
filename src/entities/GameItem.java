package entities;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import animation.Animation;
import animation.Bone;
import animation.MaskedAnimation;
import animation.Node;
import graphics.Mesh;
import graphics.Model;
import graphics.AnimatedModel;

public class GameItem {
	private static int numGameItems = 0;
	
	private final int id;
	
	private final AnimatedModel animatedModel;
	private final Model model;
	private final boolean animated;
	
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public GameItem(AnimatedModel model) {
		this.id = numGameItems++;
		this.animatedModel = model;
		this.model = null;
		this.animated = true;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = 1;
	}
	
	public GameItem(Model model) {
		this.id = numGameItems++;
		this.animatedModel = null;
		this.model = model;
		this.animated = false;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = 1;
	}
	
	public int getID() {
		return id;
	}
	
	public AnimatedModel getAnimatedModel() {
		return animatedModel;
	}
	
	public Model getModel() {
		return model;
	}
	
	public boolean isAnimated() {
		return animated;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position = new Vector3f(x, y, z);
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation = new Vector3f(x, y, z);
	}
	
	public void changePosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
}
