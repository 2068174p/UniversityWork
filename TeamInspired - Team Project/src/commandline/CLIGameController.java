package commandline;

import java.io.*;
import java.util.Scanner;

import database.Data;
import game.GameEngine;

/**
 * Maintains user input/output and connection between user interface and game engine 
 */

public class CLIGameController {

	/** Contain game configuration */
	private CommandLineJSONConfiguration conf;
	
	/** Obtain user input from console */
	private Scanner scanner;
	
	/** Instance of game engine */
	private GameEngine gameEngine;
	
	/** Writer to log file */
	private PrintWriter log;
	
	/** Contain information of eliminated player */
	private String eliminatedPlayer;
	
	/** Flag if game has to be logged */
	private boolean writeGameLogsToFile;

	/** Object for data communication to database */
	private Data data;
	
	/** representing id game in database */
	private int gameID;
	
	
	/** Class Constructor
	 * 
	 * @param flag indicates log is enable or not
	 */
	public CLIGameController(String isEnabledLog) {

		// Variable Initialisation
		data = new Data();
		gameID = data.getGameCounter() + 1;
		scanner = new Scanner(System.in);
		conf = new CommandLineJSONConfiguration("TopTrumps.json");

		writeGameLogsToFile = false;
		if (isEnabledLog.equalsIgnoreCase("true")) {
			writeGameLogsToFile = true; // Command line selection
			try {
				log = new PrintWriter("LogFile.txt");
			} catch (Exception e) {
				System.out.println("LogFile.txt is not found");
			}
		}
	}		
	
	/** 
	 *  Instantiates a new game, construct card deck, player, and suffle card
	 */
	public void initGame(int gameCount) {
		//init game engine
		gameEngine = new GameEngine(conf.getNumAIPlayers(), conf.getDeckFile(),gameID);
		gameID++;
		gameEngine.setGameData(conf.getNumAIPlayers());
		eliminatedPlayer = "";
		
		if (writeGameLogsToFile){
			log.println("\r\n\r\n*************************************** Game - "+gameCount+" ***************************************\r\n");
			log.println("Original Deck :\r\n" + gameEngine.printCardDeck() + "\r\n");
		}
		
		//suffle card
		gameEngine.suffleCard();
		
		if (writeGameLogsToFile)
			log.println("Suffled Deck :\r\n" + gameEngine.printCardDeck() + "\r\n");
	}
	
	/** 
	 *  Play a round of game
	 */
	public void playGame() {
		
		int choosenCharacteristic = 0;
		boolean isManual = true;
		while (gameEngine.getActivePlayer() > 1) {
			
			System.out.println("\n\n>> ROUND : " + gameEngine.getRoundCount() + " <<\n");
			System.out.println("Active Player : " + gameEngine.getFirstPlayer().getName());
			System.out.println("---------------- Active Player Card ----------------");
			System.out.println(gameEngine.printCharacteristicsHeader());
			System.out.println(gameEngine.printFirstPlayerCard()+"\n");
			if (writeGameLogsToFile) {
				log.println("------------------- Round - " + gameEngine.getRoundCount() + " -------------------");
				log.println("Active Player : " + gameEngine.getFirstPlayer().getName() + "\r\n");
				log.println("All Player Card :\r\n" + gameEngine.printAllPlayerCard());
				log.println("All Player Top Card :\r\n" + gameEngine.printAllPlayerTopCard());
			}
			
			// if user is the active player, ask user to select characteristic
			if (gameEngine.getFirstPlayer().getPlayerType() == 1)
			{ 
				choosenCharacteristic = getUserCategory();
				isManual = true;
			}
			else // set AI Player choosen characteristic
			{
				choosenCharacteristic = gameEngine.getFirstPlayer().getMaxCharacteristic();
				System.out.println("Choosen Characteristic : " + gameEngine.getCharacteristicDescription(choosenCharacteristic));
			}

			if (writeGameLogsToFile) 
				log.println("Choosen Characteristic : " + gameEngine.getCharacteristicDescription(choosenCharacteristic)
				+ ", Characteristic Value : " + gameEngine.getFirstPlayer().getTopCard().getCharacteristic()[choosenCharacteristic]);
			
			// play the round
			gameEngine.playCard(choosenCharacteristic);

			if (gameEngine.getIsDraw()) {
				System.out.println("\nRound is draw");
				if (writeGameLogsToFile){
					log.println("Game is draw");
					log.println("Cummonal Card : " + gameEngine.printCommunalCard() + "\r\n");
				}
			} else {
				System.out.println("\nRound Winner is : " + gameEngine.getFirstPlayer().getName());
				System.out.println("---------------- Winning Card ----------------");
				System.out.println(gameEngine.printCharacteristicsHeader());
				System.out.println(gameEngine.printFirstPlayerCard()+"\n");
				if (writeGameLogsToFile)
					log.println("Round Winner is : " + gameEngine.getFirstPlayer().getName() + "\r\n");
			}
			
			// eliminate player
			eliminatedPlayer += gameEngine.printEliminatedPlayer();
			if (!eliminatedPlayer.trim().equals(""))
				System.out.println("Eliminated player = " + eliminatedPlayer);
			gameEngine.removeEliminatedPlayer();
			
			// game mode : manual or automatic
			if (isManual && (gameEngine.getActivePlayer() > 1)) {
				System.out.print("\nPress \"c\" to next round or any other key to fast forward round ");
				if (scanner.next().trim().toLowerCase().equals("c"))
					isManual = true;
				else
					isManual = false;
			}
		}
	}
	
	/** 
	 *  Display main menu for user, and get user input
	 */
	public int getMainMenu() {
		int option = -1;
		System.out.println("\n\n\nMain Menu ");
		System.out.println("1. View Statistics");
		System.out.println("2. Play Game");
		System.out.println("3. Exit Game");

		while (option > 3 || option < 1) {
			System.out.print("Please choose menu (1..3) : ");
			try {
				option = Integer.parseInt(scanner.next());
			} catch (Exception e) {
			}
			if (option > 3 || option < 1)
				System.out.print("Invalid input. ");
		}

		return option;
	}

	/** 
	 *  Get user selected characteristic option
	 */
	public int getUserCategory() {
		int option = -1;
		System.out.println("Category Option :");
		String[] categoryOption = gameEngine.printCharacteristicsHeader().split("\\s+");

		for (int i = 1; i < categoryOption.length; i++)
			System.out.println(i + "." + categoryOption[i]);

		while (option > (categoryOption.length-2) || option < 0) {
			System.out.print("Please input option category index (1.."+(categoryOption.length - 1)+") : ");
			try {
				option = Integer.parseInt(scanner.next()) - 1;
			} catch (Exception e) {
			}
			if (option > (categoryOption.length-2) || option < 0)
				System.out.print("Invalid input. ");
		}

		return option;
	}

	/** 
	 *  Display and write log of the game result
	 */
	public void printGameResult() {
		if (writeGameLogsToFile){
			log.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"); 
			log.println("Game Winner is : " + gameEngine.getFirstPlayer().getName());
			log.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		System.out.println("\n****************************************");
		System.out.println("Game Winner is : " + gameEngine.getFirstPlayer().getName());
		System.out.println("****************************************");
	}

	/** 
	 *  Closed log file if game is ended.
	 */
	public void endGame() {
		if (writeGameLogsToFile)
			log.close();
	}
	
	public void viewReport() {
		String tmpString = "",tmpString2 = "";
		String[] tmp_array,tmp_array2;
		
		if (data.getGameStats().size() > 1){			
			System.out.println("\n-------------------- All Game Summary -------------------- " );
			tmpString=data.getGameStats().get(0).replace("\"", "");
			tmpString=tmpString.replace("[", "");
			tmpString=tmpString.replace("]", "");
			tmp_array=tmpString.split(",");	
			
			tmpString2=data.getGameStats().get(1).replace("\"", "");
			tmpString2=tmpString2.replace("[", "");
			tmpString2=tmpString2.replace("]", "");
			tmp_array2=tmpString2.split(",");	
			for (int j = 0; j < tmp_array2.length ;j++){
				System.out.print(tmp_array[j] + " : " + tmp_array2[j] + "\n");
				if (j == 3)
					System.out.print(" Average Draw : " + String.format("%.2f", Double.parseDouble(tmp_array2[3].trim()) / Double.parseDouble(tmp_array2[0].trim())) + "\n");
			}
			System.out.println("\n-------------------- Previous Game Stats -------------------- " );
			tmpString = "";tmpString2 = "";
			tmpString=data.getPrevGameStats().get(0).replace("\"", "");
			tmpString=tmpString.replace("[", "");
			tmpString=tmpString.replace("]", "");
			tmp_array=tmpString.split(",");	

			tmpString2=data.getPrevGameStats().get(1).replace("\"", "");
			tmpString2=tmpString2.replace("[", "");
			tmpString2=tmpString2.replace("]", "");
			tmp_array2=tmpString2.split(",");	
			for (int j = 0; j < tmp_array2.length ;j++)
				System.out.print(tmp_array[j] + " : " + tmp_array2[j] + "\n");			
		
			System.out.println("\n-------------------- Previous Round Winner -------------------- " );
			tmpString = "";tmpString2 = "";
			for (int i=0;i<data.getPrevGameWinnerStats().size();i++){
				tmpString=data.getPrevGameWinnerStats().get(i).replace("\"", "");
				tmpString=tmpString.replace("[", "");
				tmpString=tmpString.replace("]", "");
				tmp_array=tmpString.split(",");	
				System.out.print(tmp_array[0] + " : won " + tmp_array[1] + " round\n");
			}
		}
		else
			System.out.println("\n-------------------- No Game Statistics Available -------------------- ");
	}
	
}
