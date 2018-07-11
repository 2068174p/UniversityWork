package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import database.Data;

/**
 * Maintains a list of deck card, game player, and communal pile card, and card theme characteristic 
 * The methods contain logic and rule of plaing top trump game
 */
public class GameEngine {
	/** List of top trump card */
	private List<Card> cardDeck;
	
	/** List of communal pile card which contain players cards temporarily */
	private List<Card> communalPileCard;
	
	/** List of game players */
	private List<Player> playerList;
	
	/** List of game characteristic theme */
	private String[] characteristicTheme;

	/** number of round in one game */
	private int roundCount;
	
	/** Player who act as first player */
	private Player firstPlayer;
	
	/** Flag to determine whether round of game is draw or not */
	private boolean isDraw;
	
	/** Object for data communication to database */
	private Data data;
	
	/** ID (primary key) of the game in database */
	private int gameID;

	
	/** Class Constructor
	 * 
	 * Instantiates a new game
	 * @param1 number of AI player
	 * @param2 String of deck filename
	 */
	public GameEngine(int numAIPlayers, String deckFileName,int gid) {
		roundCount = 1;
		gameID = gid;
		cardDeck = new ArrayList<Card>();
		communalPileCard = new ArrayList<Card>();
		playerList = new ArrayList<Player>();

		// Set up Player and Card
		initPlayer(numAIPlayers);
		initCard(deckFileName);
		
		// Set up database
		data = new Data();
	}

	/** Class Accessor */
	public Boolean getIsDraw() {
		return isDraw;
	}

	/** Class Accessor */
	public int getRoundCount() {
		return roundCount;
	}
	
	/** Class Accessor */
	public Player getFirstPlayer() {
		return firstPlayer;
	}

	/** 
	 * Construct initial deck
	 * 
	 * @param textfile filename
	 */
	public void initCard(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			Scanner in = new Scanner(reader);

			// header file contain characteristic theme
			if (in.hasNextLine())
				setCharacteristicDescription(in.nextLine());

			// generate cards
			Card card;
			while (in.hasNextLine()) {
				card = new Card(in.nextLine());
				cardDeck.add(card);
			}
			
			in.close();
			reader.close();
		} catch (IOException e) {
			System.out.println(fileName + " is not found");
		}
	}

	/** 
	 * Set up game player
	 * 
	 * @param number of AI player
	 */
	public void initPlayer(int numAIPlayers) {
		Player player;
		player = new Player("Human", 1,0);
		playerList.add(player);
		
		for (int i = 1; i <= numAIPlayers; i++) {
			player = new Player("AI-" + i, 0, i);
			playerList.add(player);
		}

		
	}
	
	/** 
	 * Suffle the card and deal the card to each player
	 */
	public void suffleCard() {
		// Shuffle that list
		Collections.shuffle(cardDeck);

		// deal card to each player
		for (int i = 0; i < cardDeck.size(); i++)
			playerList.get(i % playerList.size()).addCard(cardDeck.get(i));

		generateFirstPlayer();
		setPlayerTopCard();
	}

	/** 
	 * choose first player randomly
	 */
	public void generateFirstPlayer() {
		int i;
		Random r = new Random();
		i = r.nextInt(playerList.size());
		firstPlayer = playerList.get(i);
	}

	/** 
	 * set each player top card
	 */
	public void setPlayerTopCard() {
		for (Player player : playerList)
			player.setTopCard();
	}

	/** 
	 * Play a game round
	 * 
	 * @param index of choosen Characteristic
	 */
	public void playCard(int indexCharacteristic) {
		Player player = playerList.get(0);
		communalPileCard.add(player.getTopCard());
		player.removeCard();
		isDraw = false;

		// compare each player card
		for (int i = 1; i < playerList.size(); i++) {
			communalPileCard.add(playerList.get(i).getTopCard());
			playerList.get(i).removeCard();
			if (player.getTopCard().getCharacteristic()[indexCharacteristic] < playerList.get(i).getTopCard()
					.getCharacteristic()[indexCharacteristic]) {
				player = playerList.get(i);
				isDraw = false;
			} else if (player.getTopCard().getCharacteristic()[indexCharacteristic] == playerList.get(i).getTopCard()
					.getCharacteristic()[indexCharacteristic])
				isDraw = true;
		}

		// check if round is draw
		if (!isDraw) {
			firstPlayer = player;
			for (Card card : communalPileCard)
				firstPlayer.addCard(card);

			// reset communal pile card
			communalPileCard.clear();
		}

		data.addRound(isDraw?1:0, firstPlayer.getID(), gameID);
		roundCount++;
	}

	/** 
	 * Remove eliminated player from list
	 */
	public void removeEliminatedPlayer() {
		// remove player 
		for (int i = playerList.size() - 1; i >= 0; i--) {
			if (playerList.get(i).getRemainCardCount() < 1)
				playerList.remove(i);
		}

		// set remain active player top card
		setPlayerTopCard();
		
		//update game winner id database
		if (playerList.size() < 2)
			data.updateGame(gameID, firstPlayer.getID());
	}	
	
	/** 
	 * get number card in communal pile
	 * 
	 * @return number of communal card
	 */
	public int getCommunalPileCardCount() {
		return communalPileCard.size();
	}

	/** 
	 * get how many active player left
	 * 
	 * @return number of active player
	 */
	public int getActivePlayer() {
		return playerList.size();
	}

	/** 
	 * Set up game characteristic theme
	 * 
	 * @param string of game characteristic theme
	 */
	public void setCharacteristicDescription(String inputStr) {
		String[] strArray = inputStr.split("\\s+");
		characteristicTheme = new String[strArray.length];
		for (int i = 0; i < strArray.length; i++)
			characteristicTheme[i] = strArray[i];
	}

	/** 
	 * get characteristic theme description by index of list
	 * 
	 * @param index of list
	 * @return characteristic theme description of given index 
	 */
	public String getCharacteristicDescription(int i) {
		return characteristicTheme[i + 1];
	}

	/** 
	 * Set new game information to database
	 * 
	 * @param player number
	 */
	public void setGameData(int playerNumber) {
		data.addGame(playerNumber,gameID);
		
	}
	
	/** 
	 * Get overall game statistics
	 * 
	 * @return overall game statistics 
	 */
	public List<String> getGameStats() {
		return data.getGameStats();
	}
	
	/** 
	 * Get previous game round winner statistics
	 * 
	 * @param game id which will be deleted
	 */
	public void cancelGame() {
		data.cancelGame(gameID);
	}
	

	/** 
	 * ***************  Bellow are printing methods for log and report purpose   ***************  
	 */
	
	
	/** 
	 * Generate report contain each player details such as
	 * player name, number of remain card and details of top card
	 * 
	 * @return formated report of player details 
	 */
	public String printOnlineModeGameDetails() throws IOException {
		ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();		
		List<String> listOfInfos = new ArrayList<String>();
		List<String> listOfInfoDetail;
		for (Player player : playerList){
			listOfInfoDetail = new ArrayList<String>();
			
			listOfInfoDetail.add(player.getName());
			listOfInfoDetail.add("" + player.getRemainCardCount());
			listOfInfoDetail.add(player.getTopCard().getdescription());
			for(int i = 0; i < characteristicTheme.length-1; i++)
				listOfInfoDetail.add(characteristicTheme[i+1] + " : " + player.getTopCard().getCharacteristic()[i]+"");	
			listOfInfoDetail.add(player.getPlayerType()+"");
			listOfInfos.add(oWriter.writeValueAsString(listOfInfoDetail));		
		}	
		return oWriter.writeValueAsString(listOfInfos);
	}

	/** 
	 * Generate report contain details of first player card
	 * 
	 * @return formated report of first player card
	 */
	public String printFirstPlayerCard() {
		String card = setAlignment(firstPlayer.getTopCard().getdescription(), 15, 0);
		int j = (characteristicTheme[1].length()/2) + 1;
		card += setAlignment(firstPlayer.getTopCard().getCharacteristic()[0] + "", j, 1);
		for (int i = 2; i < characteristicTheme.length; i++){
			j  = ((characteristicTheme[i-1].length() + characteristicTheme[i].length()) / 2) + 4; 
			card += setAlignment(firstPlayer.getTopCard().getCharacteristic()[i-1] + "", j, 1);
		}
		
		return card;
	}

	/** 
	 * generate formated string of characteristic theme description
	 * 
	 * @return formated characteristic theme description
	 */
	public String printCharacteristicsHeader() {
		String characteristics = "";
		for (int i = 0; i < characteristicTheme.length; i++)
			characteristics += characteristicTheme[i] + "    ";
		return characteristics;
	}

	/** 
	 * generate formated string of eliminated player
	 * 
	 * @return formated string of eliminated player
	 */
	public String printEliminatedPlayer() {
		String result = "";
		for (Player player : playerList)
			if (player.getRemainCardCount() < 1)
				result += player.getName() + "  ";
		return result;
	}

	/** 
	 * generate report of each card in deck
	 * 
	 * @return formated report of each card in deck
	 */
	public String printCardDeck() {
		String result = "";
		for (Card card : cardDeck)
			result += card.getdescription() + "~";
		return result;
	}

	/** 
	 * generate report of each player card
	 * 
	 * @return formated report of each player card
	 */
	public String printAllPlayerCard() {
		String result = "";
		for (Player player : playerList) 
			result += "Player " + player.getName() + " : " + player.printCardList() + "\r\n";
		return result;
	}

	/** 
	 * generate report of each each player top card
	 * 
	 * @return formated report of each player top card
	 */
	public String printAllPlayerTopCard() {
		String result = "";
		for (Player player : playerList){
			result += "Player " + player.getName() + " : " + player.getTopCard().getdescription() + " -> ";
			for (int i = 1; i < characteristicTheme.length;i++)
			{
				result +=characteristicTheme[i]+":"+player.getTopCard().getCharacteristic()[i-1]+" ";
			}
			result += "\r\n";
		}
		return result;
	}

	/** 
	 * generate report of each card in communal pile
	 * 
	 * @return formated report of each card in communal pile
	 */
	public String printCommunalCard() {
		String result = "";
		for (Card card : communalPileCard)
			result += card.getdescription() + "~";
		return result;
	}

	/** 
	 * set alignment for report purpose
	 * 
	 * @param1 string to be align
	 * @param2 space allocated
	 * @param3 type of alignment (0:left-align, 1:right-align)
	 * @return formated characteristic theme description
	 */
	public String setAlignment(String str, int spaceLength, int type) {
		int space = spaceLength - str.length();
		for (int i = 0; i < space; i++)
			if (type == 0)
				str += " ";
			else 
				str = " " + str;
			
		return str;
	}
	
}