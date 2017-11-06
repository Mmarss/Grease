package net.mmarss.grease.demo.plane;

import net.mmarss.grease.core.BasicGame;
import net.mmarss.grease.core.BasicGameEngine;
import net.mmarss.grease.exception.GreaseFileException;
import net.mmarss.grease.graphics.Graphics2d;
import net.mmarss.grease.graphics.Image;
import net.mmarss.grease.input.BasicInput;
import net.mmarss.grease.input.Key;

/**
 * The core class for the plane game demo.
 */
public class PlaneGame extends BasicGame {
	
	/* The number of buildings in the game. */
	private static final int NUM_BUILDINGS = 22;
	
	/* The game engine used by this game. */
	private BasicGameEngine engine;
	
	/* The plane rendered in this demo. */
	private Image plane;
	
	/* The heights of the buildings in this demo. */
	private float[] buildingHeights;
	
	/* The widths of the buildings in this demo. */
	private float[] buildingWidths;
	
	/* The shades of the buildings in this demo. */
	private float[] buildingShades;
	
	/* The angle at which the plane should be rendered. */
	private float angle;
	
	/* The y-coordinate of the plane's center. */
	private float height;
	
	/* The x-coordinate of the left edge of the map. */
	private float x;
	
	/*
	 * Constructs a new plane game instance. Only ever called from the program entry
	 * point, within this class.
	 */
	private PlaneGame() {
		
		engine = new BasicGameEngine(this);
		
		buildingHeights = new float[NUM_BUILDINGS];
		buildingWidths = new float[NUM_BUILDINGS];
		buildingShades = new float[NUM_BUILDINGS];
		
		for (int i = 0; i < NUM_BUILDINGS; i++) {
			buildingWidths[i] = 40f + (float) Math.random() * 60f; // From 40 to 100
			buildingHeights[i] = 60f + (float) Math.random() * 340f; // From 60 to 400
			buildingShades[i] = 0.1f + (float) Math.random() * 0.5f; // From 0.1 to 0.6
		}
	}
	
	/**
	 * The program entry point.
	 * 
	 * @param args
	 *            the command line arguments passed to this application.
	 */
	public static void main(String[] args) {
		
		PlaneGame game = new PlaneGame();
		game.engine.run();
	}
	
	@Override
	public void init() {
		
		try {
			plane = new Image("../res/plane.png");
		} catch (GreaseFileException e) {
			e.printStackTrace();
			plane = new Image();
		}
		
		height = 300;
	}
	
	@Override
	public void update(double delta, BasicInput input) {
		
		if (input.isKeyDown(Key.KEY_UP)) {
			angle += delta * Math.PI * 2 / 1000 / 4;
			if (angle > Math.PI / 6) {
				angle = (float) Math.PI / 6;
			}
		}
		
		if (input.isKeyDown(Key.KEY_DOWN)) {
			angle -= delta * Math.PI * 2 / 1000 / 4;
			if (angle < -Math.PI / 6) {
				angle = (float) -Math.PI / 6;
			}
		}
		
		height += delta * Math.sin(angle) * 300 / 1000;
		x += delta * Math.cos(angle) * 300 / 1000;
		
		if (height < 100) {
			height = 100;
		}
		
		if (height > 500) {
			height = 500;
		}
		
		while (x > buildingWidths[0]) {
			x -= buildingWidths[0];
			for (int i = 1; i < NUM_BUILDINGS; i++) {
				buildingWidths[i - 1] = buildingWidths[i];
				buildingHeights[i - 1] = buildingHeights[i];
				buildingShades[i - 1] = buildingShades[i];
			}
			
			buildingWidths[NUM_BUILDINGS - 1] = 40f + (float) Math.random() * 60f; // From 40 to 100
			buildingHeights[NUM_BUILDINGS - 1] = 60f + (float) Math.random() * 340f; // From 60 to 400
			buildingShades[NUM_BUILDINGS - 1] = 0.1f + (float) Math.random() * 0.5f; // From 0.1 to 0.6
		}
	}
	
	@Override
	public void render(Graphics2d g) {
		
		float offsetx = -x;
		for (int i = 0; i < NUM_BUILDINGS; i++) {
			g.setColor(buildingShades[i], buildingShades[i], buildingShades[i]);
			g.drawRect(offsetx, 600, offsetx + buildingWidths[i], 600 - buildingHeights[i]);
			offsetx += buildingWidths[i];
		}
		
		g.setColor(1.0f, 1.0f, 1.0f);
		g.drawImageRotated(plane, 400, 600 - height, angle);
	}
	
	@Override
	public void cleanup() {
		
	}
}
