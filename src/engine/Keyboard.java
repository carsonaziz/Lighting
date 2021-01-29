package engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

import entities.GameItem;

public class Keyboard {
	private List<GameItem> gameItems;
	private Map<String, Boolean> keyMap;
	
	public Keyboard(List<GameItem> gameItems) {
		this.gameItems = gameItems;
		this.keyMap = new HashMap<String, Boolean>();
		keyMap.put("W", false);
		keyMap.put("S", false);
		keyMap.put("A", false);
		keyMap.put("D", false);
		keyMap.put("SPACE", false);
		keyMap.put("LEFT_SHIFT", false);
	}
	
	public void init(Window window) {
		glfwSetKeyCallback(window.getWindowHandle(), (windowHandle, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(windowHandle, true);
			}
			if(key == GLFW_KEY_UP && action == GLFW_PRESS) {
				gameItems.get(0).getAnimatedModel().transitionToAnimation(3, 0.5f);
			}
			if(key == GLFW_KEY_UP && action == GLFW_RELEASE) {
				gameItems.get(0).getAnimatedModel().transitionToAnimation(4, 0.5f);
			}

			if(key == GLFW_KEY_W && action == GLFW_PRESS) {
				keyMap.put("W", true);
			}
			if(key == GLFW_KEY_W && action == GLFW_RELEASE) {
				keyMap.put("W", false);
			}
			if(key == GLFW_KEY_S && action == GLFW_PRESS) {
				keyMap.put("S", true);
			}
			if(key == GLFW_KEY_S && action == GLFW_RELEASE) {
				keyMap.put("S", false);
			}
			if(key == GLFW_KEY_A && action == GLFW_PRESS) {
				keyMap.put("A", true);
			}
			if(key == GLFW_KEY_A && action == GLFW_RELEASE) {
				keyMap.put("A", false);
			}
			if(key == GLFW_KEY_D && action == GLFW_PRESS) {
				keyMap.put("D", true);
			}
			if(key == GLFW_KEY_D && action == GLFW_RELEASE) {
				keyMap.put("D", false);
			}
			if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
				keyMap.put("SPACE", true);
			}
			if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
				keyMap.put("SPACE", false);
			}
			if(key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS) {
				keyMap.put("LEFT_SHIFT", true);
			}
			if(key == GLFW_KEY_LEFT_SHIFT && action == GLFW_RELEASE) {
				keyMap.put("LEFT_SHIFT", false);
			}
		});
	}
	
	public Map<String, Boolean> getKeyMap() {
		return keyMap;
	}
}
