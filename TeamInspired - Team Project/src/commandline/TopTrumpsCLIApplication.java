package commandline;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	/**
	 * This main method is called by TopTrumps.java when the user specifies that
	 * they want to run in command line mode. The contents of args[0] is whether
	 * we should write game logs to a file.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CLIGameController gameController = new CLIGameController(args[0]);
		
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		int optionMenu;
		int gameCounter = 1;
		
		while (!userWantsToQuit) {
			
			// Display Main Menu and get user input
			optionMenu = gameController.getMainMenu();
			
			if (optionMenu == 1) // View Statistic
			{
				gameController.viewReport(); 
			} 
			else if (optionMenu == 2)// Play Game
			{
				// Init game engine
				gameController.initGame(gameCounter);
				gameController.playGame();
				gameController.printGameResult();
				gameCounter++;
			} 
			else if (optionMenu == 3)
			{
				userWantsToQuit = true;
				gameController.endGame();
			}
		}
	}
}
