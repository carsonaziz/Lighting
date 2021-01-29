package engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Matrix4f;

import animation.AnimatedFrame;
import animation.Animator;
import engine.Window;
import entities.Camera;
import entities.GameItem;
import game.World;
import graphics.Mesh;
import lighting.DirectionalLight;
import lighting.PointLight;
import shaders.GBuffer;
import shaders.ShaderProgram;
import utilities.TransformationMatrix;
import utilities.Utils;

public class Renderer {
	private final Window window;
	
	private final GBuffer gBuffer;

	private final ShaderProgram geometryPassShader;
	private final ShaderProgram animGeometryPassShader;
	private final ShaderProgram lightingPassShader;
	
	private final int quad;
	
	public Renderer(Window window, GBuffer gBuffer, ShaderProgram geometryPassShader, ShaderProgram animGeometryPassShader, ShaderProgram lightingPassShader) {
		this.window = window;
		this.gBuffer = gBuffer;
		this.geometryPassShader = geometryPassShader;
		this.animGeometryPassShader = animGeometryPassShader;
		this.lightingPassShader = lightingPassShader;
		this.quad = createQuad();
	}
	
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glClearColor(0, 0.5f, 1, 1);
		
		lightingPassShader.use();
		lightingPassShader.loadInt("gPosition", 0);
		lightingPassShader.loadInt("gNormal", 1);
		lightingPassShader.loadInt("gAlbedo", 2);
	}
	
	public void render(World world, Camera camera, Animator animator) {
		renderToGBuffer(world, camera, animator);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		lightingPassShader.use();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, gBuffer.getGPositionID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, gBuffer.getGNormalID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, gBuffer.getGAlbedoID());
		
		List<PointLight> pointLights = world.getPointLights();
		for(int i = 0; i < pointLights.size(); i++) {
			lightingPassShader.loadVector3f("pointLights[" + i + "].position", pointLights.get(i).getPosition());
			lightingPassShader.loadVector3f("pointLights[" + i + "].colour", pointLights.get(i).getColour());
			lightingPassShader.loadFloat("pointLights[" + i + "].constant", pointLights.get(i).getConstant());
			lightingPassShader.loadFloat("pointLights[" + i + "].linear", pointLights.get(i).getLinear());
			lightingPassShader.loadFloat("pointLights[" + i + "].quadratic", pointLights.get(i).getQuadratic());
		}

		lightingPassShader.loadVector3f("dirLight.direction", world.getSun().getDirection());
		lightingPassShader.loadVector3f("dirLight.colour", world.getSun().getColour());
		
		glBindVertexArray(quad);
		glDrawElements(GL_TRIANGLE_STRIP, 6, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}
	
	private void renderToGBuffer(World world, Camera camera, Animator animator) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glBindFramebuffer(GL_FRAMEBUFFER, gBuffer.getGBufferID());
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Matrix4f model = null;
		Matrix4f view = TransformationMatrix.createViewMatrix(camera);
		Matrix4f projection = TransformationMatrix.createProjectionMatrix(45.0f, (float)window.getWidth()/(float)window.getHeight(), 0.1f, 1500f);
		
		geometryPassShader.use();
		geometryPassShader.loadMatrix("view", view);
		geometryPassShader.loadMatrix("projection", projection);

		animGeometryPassShader.use();
		animGeometryPassShader.loadMatrix("view", view);
		animGeometryPassShader.loadMatrix("projection", projection);
		
		for(GameItem gameItem : world.getGameItems()) {
			if(gameItem.isAnimated()) {
				AnimatedFrame animatedFrame = animator.getAnimatedFrame(gameItem.getID());
				model = TransformationMatrix.createModelMatrix(gameItem.getPosition(), gameItem.getRotation().x, gameItem.getRotation().y, gameItem.getRotation().z, gameItem.getScale());
				animGeometryPassShader.use();
				animGeometryPassShader.loadMatrix("model", model);
				animGeometryPassShader.loadMatrices("bonesMatrix", animatedFrame.getBoneMatrices());
				
				Mesh[] meshes = gameItem.getAnimatedModel().getMeshes();
				for(Mesh mesh : meshes) {
					glActiveTexture(GL_TEXTURE0);
					glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getDiffuseTexture().getID());
					animGeometryPassShader.loadInt("meshTexture", 0);
					glBindVertexArray(mesh.getVaoID());
					glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
					glBindVertexArray(0);
				}
			} else {
				model = TransformationMatrix.createModelMatrix(gameItem.getPosition(), gameItem.getRotation().x, gameItem.getRotation().y, gameItem.getRotation().z, gameItem.getScale());
				geometryPassShader.use();
				geometryPassShader.loadMatrix("model", model);
				geometryPassShader.loadMatrix("root", gameItem.getModel().getRootTransform());
				
				Mesh[] meshes = gameItem.getModel().getMeshes();
				for(Mesh mesh : meshes) {
					glActiveTexture(GL_TEXTURE0);
					glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getDiffuseTexture().getID());
					geometryPassShader.loadInt("meshTexture", 0);
					glBindVertexArray(mesh.getVaoID());
					glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
					glBindVertexArray(0);
				}
			}
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void update() {
		window.update();
	}
	
	public int createQuad() {
//		float[] vertices = {
//				-0.5f, 0.5f, 0.0f,
//				0.5f, 0.5f, 0.0f,
//				0.5f, -0.5f, 0.0f,
//				-0.5f, -0.5f, 0.0f,
//				
//			};
		float[] vertices = {
				-1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				-1.0f, -1.0f, 0.0f,
				
			};
		
		int[] indices = {
			0, 1, 2,
			0, 2, 3
		};
		
		float[] textCoords = {
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f
		};
		
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer verticesBuffer = Utils.storeDataInFloatBuffer(vertices);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int textureVboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureVboID);
		FloatBuffer textCoordsBuffer = Utils.storeDataInFloatBuffer(textCoords);
		glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		IntBuffer indicesBuffer = Utils.storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
		
		return vaoID;
	}
}
