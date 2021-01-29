package lighting;

import org.joml.Vector3f;

import graphics.Model;
import loaders.ModelLoader;

public class PointLight {
	private Model model;
	
	private Vector3f position;
	private Vector3f colour;

	private final float constant = 1.0f;
	private final float linear;
	private final float quadratic;
	
	public PointLight(Vector3f position, Vector3f colour) {
		this(position, colour, 0.14f, 0.07f);
	}
	
	//TODO: Instead of taking in constant, linear, and quadratic values. Take in the direction value and make set the constant, linear, and quadratic values accordingly.
	//		Maybe even calculate what the values should be for any given distance (instead of just following that table
	//TODO: Add ambient value to each light so it does not need to be set in each method in the shaders
	public PointLight(Vector3f position, Vector3f colour, float linear, float quadratic) {
		this.position = position;
		this.colour = colour;
		this.linear = linear;
		this.quadratic = quadratic;
		try {
			this.model = ModelLoader.loadModel("src/res/light/model/light.obj", "src/res/light/textures");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vector3f getPosition() {
		return position;
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
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void changePosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
}
