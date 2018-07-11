// A class that has the data from the input transaction in the interface.

public class Wine {

	private String typeWine;
	private double wineCost;
	private int numberWine;

 //Constructing a wine object, which has the name, price and number of the wine purchased/returned
	
	public Wine(String type, double cost, int bottles) {
		typeWine = type;
		wineCost = cost;
		numberWine = bottles;
	}
 
	
	public String nameBottle() {
		return typeWine;
	}

	public double pricePerBottle() {
		return wineCost;
	}

	public int bottleNumber() {
		return numberWine;
	}
}