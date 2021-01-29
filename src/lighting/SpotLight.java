package lighting;

import org.joml.Vector3f;

import graphics.Model;
import loaders.ModelLoader;

public class SpotLight {
	private Model model;
	
	private Vector3f position;
	private Vector3f direction;
	private float cutOff;
	private Vector3f colour;

	private final float constant;
	private final float linear;
	private final float quadratic;
	
	public SpotLight(Vector3f position, Vector3f direction, float cutOff, Vector3f colour) {
		this.position = position;
		this.direction = direction;
		this.cutOff = cutOff;
		this.colour = colour;
		this.constant = 1.0f;
		this.linear = 0.14f;
		this.quadratic = 0.07f;
		
		try {
			this.model = ModelLoader.loadModel("src/res/spotlight/model/spotlight.obj", "src/res/spotlight/textures");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public float getCutoff() {
		return cutOff;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public float getConstant() {
		return constant;
	}
	
	public float getLinear() {
		return linear;
	}
	
	public float getQuadratic() {
		return quadratic;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void changePosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
}
