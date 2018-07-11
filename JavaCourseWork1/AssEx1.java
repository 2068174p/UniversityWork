
 // The main class for the wine program
 import javax.swing.*;

  public class AssEx1{

	public static void main(String[] args) {
		
		double  personalBalance = 0.;
		
		// Creating a pop up window that asks for the account name and exits if the input is empty, also displaying an error message
		
		String personalName = JOptionPane.showInputDialog(null,"Please enter account name") ;
	
			if (personalName.length()==0) {
				      JOptionPane.showMessageDialog(null, "Please enter a name.", "Error message", JOptionPane.ERROR_MESSAGE );
			}
			
		boolean properBalance = false ;
		
		while (!properBalance) {
			
		     // Another pop up window that asks for the balance and checks if it is entered in numbers
			//If the user input invalid balance an error message will appear 
			try {
			    personalBalance = Double.parseDouble(JOptionPane.showInputDialog(null,"Please enter the account's current balance ")) ;
			    properBalance = true ;
			    }
			catch (NumberFormatException e){
			    JOptionPane.showMessageDialog(null, "Please enter numerical data", "Error Message", JOptionPane.ERROR_MESSAGE);
			
			}
		}
		
		// A new customer account object is made and the data is passed
		CustomerAccount customer = new CustomerAccount(personalBalance, personalName) ;
	
		//Linking CustomerAccount and the GUI and making the GUI visible
		LWMGUI firstGui = new LWMGUI(customer) ;
		firstGui.setVisible(true);
	
	}

}







