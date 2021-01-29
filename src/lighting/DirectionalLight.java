package lighting;

import org.joml.Vector3f;

public class DirectionalLight {
	private Vector3f direction;
	private Vector3f colour;
	
	public DirectionalLight(Vector3f direction, Vector3f colour) {
		this.direction = direction;
		this.colour = colour;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public Vector3f getColour() {
		return colour;
	}
}
