package game;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines an object representing a game player
 */
public class Player {

	/** List of player's cards */
	private List<Card> cardList;

	/** Player topmost Card */
	private Card topCard;
	
	/** Player's type (1:User, 0:AI Players) */
	private int type;
	
	/** Player's name */
	private String name;
	
	/** Player's id in database */
	private int id;

	
	/** Class Constructor
	 * 
	 * Instantiates a new player
	 * @param1 String of player's name
	 * @param2 String of player's type
	 */
	public Player(String playerName, int playerType, int playerID) {
		name = playerName;
		type = playerType;
		id = playerID;
		cardList = new ArrayList<Card>();
	}

	/** Class Accessor */
	public int getPlayerType() {
		return type;		
	}

	/** Class Accessor */
	public String getName() {
		return name;
	}
	
	/** Class Accessor */
	public int getID() {
		return id;
	}

	/** Class Accessor */
	public Card getTopCard() {
		return topCard;
	}
	
	/** 
	 * Set player's top card
	 */
	public void setTopCard() {
		topCard = cardList.get(0);
	}
	
	/** 
	 * Add a card to player's list of card
	 * 
	 * @param card
	 */
	public void addCard(Card card) {
		cardList.add(card);		
	}

	/** 
	 * Remove topmost card from player's list of card
	 */
	public void removeCard() {
		cardList.remove(0);
	}

	/** 
	 * Get how many cards that player has
	 * 
	 * @return number of player cards 
	 */
	public int getRemainCardCount() {
		return cardList.size();
	}

	/** 
	 * Get list index of maximum characteristic value of player's top card 
	 * 
	 * @return list index of maximum characteristic value
	 */
	public int getMaxCharacteristic() {
		int characteristicIndex = 0;
		int maxValue = 0;
		for (int i = 0; i < topCard.getCharacteristic().length; i++)
			if (maxValue < topCard.getCharacteristic()[i]) {
				characteristicIndex = i;
				maxValue = topCard.getCharacteristic()[i];
			}
		return characteristicIndex;
	}

	/** 
	 * Generate string of player's card list for logging and report purpose 
	 * 
	 * @return string of player's card list
	 */	
	public String printCardList() {
		String result="";
		for(Card card:cardList)
			result += card.getdescription()+"~";
		return result;
	}

}