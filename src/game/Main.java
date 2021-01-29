package game;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import animation.Animation;
import animation.Animator;
import animation.MaskedAnimation;
import engine.Keyboard;
import engine.Mouse;
import engine.Window;
import engine.render.Renderer;
import entities.Camera;
import entities.GameItem;
import graphics.AnimatedModel;
import graphics.Mesh;
import graphics.Model;
import lighting.DirectionalLight;
import lighting.PointLight;
import loaders.ModelLoader;
import shaders.GBuffer;
import shaders.ShaderProgram;
import utilities.Timer;
import utilities.TransformationMatrix;
import utilities.Utils;

public class Main {
	public static void main(String[] args) {
		Window window = new Window(1600, 900, "OpenGL Lighting", true);
		window.init();
		Timer timer = new Timer();
		Camera camera = new Camera();
		camera.setPosition(0, 7.5f, 20);
		World world = new World();
		world.load();
		
		Animator animator = new Animator(world.getGameItems());
		Mouse mouse = new Mouse(world.getGameItems());
		Keyboard keyboard = new Keyboard(world.getGameItems());
		mouse.init(window);
		keyboard.init(window);
		
		//****Deferred Rendering Code****//
		ShaderProgram geometryPassShader = new ShaderProgram("src/shaders/vertex_shader.vs", "src/shaders/gbuffer.fs");
		ShaderProgram animGeometryPassShader = new ShaderProgram("src/shaders/anim_vertex_shader.vs", "src/shaders/gbuffer.fs");
		ShaderProgram lightingPassShader = new ShaderProgram("src/shaders/deferred_vertex_shader.vs", "src/shaders/deferred_fragment_shader.fs");
		
		GBuffer gBuffer = new GBuffer(window.getWidth(), window.getHeight());
		
		Renderer renderer = new Renderer(window, gBuffer, geometryPassShader, animGeometryPassShader, lightingPassShader);
		renderer.init();
		//*******************************//
		
		//FPS data
		double lastTime = timer.getTime();
		int numFrames = 0;
		
		while(!window.windowShouldClose()) {
			//FPS Counter
			double currentTime = timer.getTime();
			numFrames++;
			
			if(currentTime - lastTime >= 1.0d) {
				System.out.println(numFrames + "fps");
				System.out.println(1000.0d/numFrames + " ms/frame");
				numFrames = 0;
				lastTime += 1.0d;
			}
			
			animator.update(timer.getElapsedTime());
			world.update();
			
			
			//Temporary camera movement
			if(keyboard.getKeyMap().get("W")) {
				camera.changePosition(0, 0, -0.075f);
			}
			if(keyboard.getKeyMap().get("S")) {
				camera.changePosition(0, 0, 0.075f);
			}
			if(keyboard.getKeyMap().get("A")) {
				camera.changePosition(-0.075f, 0, 0);
			}
			if(keyboard.getKeyMap().get("D")) {
				camera.changePosition(0.075f, 0, 0);
			}
			if(keyboard.getKeyMap().get("SPACE")) {
				camera.changePosition(0, 0.075f, 0);
			}
			if(keyboard.getKeyMap().get("LEFT_SHIFT")) {
				camera.changePosition(0, -0.075f, 0);
			}
			
			
			renderer.render(world, camera, animator);
			
			renderer.update();
		}
		
		window.close();
	}
}
