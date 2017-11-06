package net.mmarss.grease.demo.skeleton;

import net.mmarss.grease.core.BasicGame;
import net.mmarss.grease.core.BasicGameEngine;
import net.mmarss.grease.core.Graphics2d;
import net.mmarss.grease.input.BasicInput;

/**
 * The core class for the warehouse game demo.
 */
public class SkeletonMain extends BasicGame {
	
	/* The game engine used by this game. */
	private BasicGameEngine engine;
	
	/*
	 * Constructs a new instance of a project skeleton main class. Only ever called
	 * from the program entry point, within this class.
	 */
	private SkeletonMain() {
		
		engine = new BasicGameEngine(this, "Project Skeleton");
	}
	
	/**
	 * The program entry point.
	 * 
	 * @param args
	 *            the command line arguments passed to this application.
	 */
	public static void main(String[] args) {
		
		SkeletonMain game = new SkeletonMain();
		game.engine.run();
	}
	
	@Override
	public void init() {
		
		// Load any resources used by the game here.
	}
	
	@Override
	public void update(double delta, BasicInput input) {
		
		// Update the game state for the next frame here.
	}
	
	@Override
	public void render(Graphics2d g) {
		
		// Render the current game state here.
	}
	
	@Override
	public void cleanup() {
		
		// Release any resources that are still held by the game here.
	}
	
}
