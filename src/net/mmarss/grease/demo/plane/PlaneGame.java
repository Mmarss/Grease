package net.mmarss.grease.demo.plane;

import net.mmarss.grease.core.BasicGame;
import net.mmarss.grease.core.BasicGameEngine;
import net.mmarss.grease.core.Graphics2d;
import net.mmarss.grease.input.BasicInput;

/**
 * The core class for the warehouse game demo.
 */
public class PlaneGame extends BasicGame {
	
	/* The game engine used by this game. */
	private BasicGameEngine engine;
	
	/*
	 * Constructs a new plane game instance. Only ever called from the program entry
	 * point, within this class.
	 */
	private PlaneGame() {
		
		engine = new BasicGameEngine(this);
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
		
	}
	
	@Override
	public void update(double delta, BasicInput input) {
		
	}
	
	@Override
	public void render(Graphics2d g) {
		
	}
	
	@Override
	public void cleanup() {
		
	}
}
