package game;

import java.util.HashMap;

/**
 * Maintains concurrent game played by multiple users
 */
public class ConcurrentGame {
	/** Object that store concurrent game game */
	HashMap<Integer,GameEngine> concurrentGame;
	
	
	/** Class Constructor
	 * 
	 * Instantiates a new list of game
	 */
	public ConcurrentGame(){
		concurrentGame = new HashMap<Integer,GameEngine>();
	}
	
	/** 
	 * Add new game to list
	 * 
	 * @param game to be added to list
	 */
	public void addGame(GameEngine gameEngine, int key){
		concurrentGame.put(key,gameEngine);
	}
	
	/** 
	 * Get game engine for given key
	 * 
	 * @param key of object (game engine)
	 * @return game engine
	 */
	public GameEngine getElement(int key){
		return concurrentGame.get(key);
	}
}
