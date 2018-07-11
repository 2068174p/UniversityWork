
// A class that calculates and returns the cost of the transaction and also updates the value of the balance.

   public class CustomerAccount {
	
	    private double updatedBalance;
	
	    private String personalName;

	
	public CustomerAccount(double theBalance, String customerName) {
		
	     updatedBalance = Math.round(theBalance * 100) ; //Math.round() is used to obtain the absolute value of the double
		
	     personalName = customerName;
	}


	//Sale calculations
	
	public double saleMethod(int quantWine, double priceWine) {
		
		double priceTransaction = quantWine * priceWine;
		
		//Updating and returning the balance
		
		updatedBalance = updatedBalance + Math.round(priceTransaction *100 ); 
		
		return priceTransaction;		
	}

    //Return calculations
	
	public double returnMethod(int quantWine, double priceWine) {
	
		double serviceFee=0.2; // Service charge 20%
		
		//Applying the service charge to the transaction
		double priceTransaction = quantWine * priceWine * (1 -serviceFee); 
		
		//Updating and returning the balance
		updatedBalance = updatedBalance - Math.round(priceTransaction *100); 
		
		return priceTransaction;		
	}
	

	// Accessor method for obtaining balance 
	
	public double getBalance() {
		
		double pennies = 100.0;
		double sumCost = updatedBalance / pennies;
		return sumCost;
	}

	// Accessor method for obtaining name
	
	public String getName() {
		
		return personalName;
	}
}
	

	
	


