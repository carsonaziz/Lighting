package graphics;

import org.joml.Matrix4f;

public class Model {
	private final Matrix4f rootTransformation;
	private final Mesh[] meshes;
	
	public Model(Mesh[] meshes, Matrix4f rootTransformation) {
		this.meshes = meshes;
		this.rootTransformation = rootTransformation;
	}
	
	public Matrix4f getRootTransform() {
		return rootTransformation;
	}
	
	public Mesh[] getMeshes() {
		return meshes;
	}
}
