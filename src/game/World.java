package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import entities.Camera;
import entities.GameItem;
import graphics.AnimatedModel;
import graphics.Model;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import loaders.ModelLoader;

public class World {
	private List<GameItem> gameItems;
	private List<PointLight> pointLights;
	private List<SpotLight> spotLights;
	private DirectionalLight sun;
	
	float rotY = 0.0f;
	
	public World() {
		this.gameItems = new ArrayList<GameItem>();
		this.pointLights = new ArrayList<PointLight>();
		this.spotLights = new ArrayList<SpotLight>();
		this.sun = new DirectionalLight(new Vector3f(2, -1, -1), new Vector3f(0.3f, 0.3f, 0.3f));
		//this.sun = new DirectionalLight(new Vector3f(2, -1, -1), new Vector3f(1, 1, 1));
	}
	
	public void load() {
		//****Point Lights****//
		PointLight pointLight1 = new PointLight(new Vector3f(0, 8, 0), new Vector3f(1, 0, 1));
		PointLight pointLight2 = new PointLight(new Vector3f(-8, 8, 0), new Vector3f(1, 1, 0));
		PointLight pointLight3 = new PointLight(new Vector3f(8, 8, 0), new Vector3f(0, 1, 1));
		PointLight pointLight4 = new PointLight(new Vector3f(-10, 12, 4.0f), new Vector3f(1, 0.8745f, 0.365f), 0.09f, 0.032f);
//		pointLights.add(pointLight1);
//		pointLights.add(pointLight2);
//		pointLights.add(pointLight3);
		pointLights.add(pointLight4);
		//********************//
		
		//****Spot Lights****//
		SpotLight spotLight1 = new SpotLight(new Vector3f(-10, 12, 4.0f), new Vector3f(0, -1, 0), 20.0f, new Vector3f(1, 0.8745f, 0.365f));
		spotLights.add(spotLight1);
		//*******************//
		
		//****Game Items****//
		AnimatedModel animatedModel = null;
		AnimatedModel bowModel = null;
		Model model = null;
		Model planeModel = null;
		Model tableModel = null;
		Model lampModel = null;
		Model cupModel = null;
		Model doorModel = null;
		try {
			animatedModel = ModelLoader.loadAnimatedModel("src/res/character_2/model/character_2.dae", "src/res/character_2/animations", "src/res/character_2/textures");
			//bowModel = ModelLoader.loadAnimatedModel("src/res/bow/model/bow.dae", "src/res/bow/animations", "src/res/bow/textures");
			model = ModelLoader.loadModel("src/res/character_2/model/character_2.dae", "src/res/character_2/textures");
			planeModel = ModelLoader.loadModel("src/res/plane/model/plane.obj", "src/res/plane/textures");
			tableModel = ModelLoader.loadModel("src/res/table/model/table.obj", "src/res/table/textures");
			lampModel = ModelLoader.loadModel("src/res/lamppost/model/lamppost.obj", "src/res/lamppost/textures");
			cupModel = ModelLoader.loadModel("src/res/cup/model/cup.obj", "src/res/cup/textures");
			doorModel = ModelLoader.loadModel("src/res/door/model/door.obj", "src/res/door/textures");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Model modelTest = new Model(animatedModel.getMeshes(), animatedModel.getRootTransform());
		GameItem animGameItem = new GameItem(animatedModel);
		animGameItem.setPosition(12, 0, 0);
		gameItems.add(animGameItem);

		GameItem animBowItem = new GameItem(bowModel);
		animBowItem.setPosition(8, 0, 0);
		//gameItems.add(animBowItem);
		
		GameItem gameItem = new GameItem(model);
		gameItem.setPosition(4, 0, 0);
		gameItems.add(gameItem);
		
		GameItem planeItem = new GameItem(planeModel);
		planeItem.setPosition(0, 0, 0);
		gameItems.add(planeItem);
		
		GameItem tableItem = new GameItem(tableModel);
		tableItem.setPosition(8, 0, 4.0f);
		tableItem.setScale(2f);
		gameItems.add(tableItem);
		
		GameItem lampItem = new GameItem(lampModel);
		lampItem.setPosition(-10, 12, 4.0f);
		lampItem.setScale(2f);
		gameItems.add(lampItem);
		
		GameItem cupItem = new GameItem(cupModel);
		cupItem.setPosition(4, 4.775f, 4.0f);
		cupItem.setScale(2f);
		gameItems.add(cupItem);
		
		GameItem doorItem = new GameItem(doorModel);
		doorItem.setPosition(-12, 0, 0.0f);
		doorItem.setRotation(0, 0, 0);
		doorItem.setScale(2f);
		gameItems.add(doorItem);
		//*******************//
	}
	
	public void update() {
		for(PointLight pointLight : pointLights) {
			//pointLight.changePosition(0, 0.025f, 0);
		}
		rotY += 1f;
		gameItems.get(6).setRotation(0, (float)Math.sin(Math.toRadians(rotY)) * 30, 0);
	}
	
	public List<GameItem> getGameItems() {
		return gameItems;
	}
	
	public DirectionalLight getSun() {
		return sun;
	}
	
	public List<PointLight> getPointLights() {
		return pointLights;
	}
	
	public List<SpotLight> getSpotLights() {
		return spotLights;
	}
}
