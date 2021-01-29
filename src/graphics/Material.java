package graphics;

public class Material {
	private final Texture diffuse;
	private final Texture map;
	
	private boolean hasMap = false;
	
	public Material(Texture diffuse, Texture map) {
		this.diffuse = diffuse;
		this.map = map;
	}
	
	public Texture getDiffuseTexture() {
		return diffuse;
	}
	
	public Texture getMapTexture() {
		return map;
	}
	
	public boolean hasMap() {
		return hasMap;
	}
	
	public void setHasMap(boolean hasMap) {
		this.hasMap = hasMap;
	}
}
