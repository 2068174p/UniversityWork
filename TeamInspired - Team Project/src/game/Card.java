package game;

/**
 * Defines an object representing a top trump card
 */
public class Card {

	/** description of card */
	private String description;
	
	/** list of characteristic values */
	private int [] characteristicValue;

	
	/** Class Constructor
	 * 
	 * Instantiates a new card
	 * @param String of characteristic values
	 */
	public Card(String inputStr) {
		String[] strArray = inputStr.split("\\s+");
		characteristicValue = new int[strArray.length-1];
		description = strArray[0];
		for (int i = 0; i < strArray.length-1; i++)
			characteristicValue[i] = Integer.parseInt(strArray[i+1]);
	}
	
	/** Class Accessor */
	public String getdescription() {
		return description;
	}
	
	/** Class Accessor */
	public int[] getCharacteristic() {
		return characteristicValue;
	}
	
}
