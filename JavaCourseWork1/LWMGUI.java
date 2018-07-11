 import java.awt.*;
 import java.awt.event.*;
 import javax.swing.*;

 
   // A class that creates the GUI components and deals with its events


   public class LWMGUI extends JFrame implements ActionListener  {
    
	 // Instance variables:
	
	  private JButton salePress, returnPress;
	  private JLabel name, quantity, price, wineTitle, amount, balance;
	  private JTextField nameSpace, quantitySpace, priceSpace,  amountSpace, balanceSpace; 
	  private JPanel top, bottom, middle, superiorMiddle, inferiorMiddle;
	  private CustomerAccount newAccount;

	// A constructor for the GUI, which passes the CustomerAccount class information

   public LWMGUI(CustomerAccount customerAcc) {
	
	
	  newAccount = customerAcc;

	// Adjusting the frame properties and name
	  this.setTitle(String.format("Lilybank Wine Merchants: %s ", newAccount.getName())); 
	  
	  this.setSize(600, 160);
	  this.setLocation(500, 300);
	  
	  this.setResizable(false);
	
	  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


     // A method that adds the layout components onto the GUI
		
	  this.layoutComponents();
		
	}

	//Layout components - creation and positioning on the frame
	

	private void layoutComponents() {
		
		//Top panel and its components-creation and addition on certain positions of the frame
		
		top = new JPanel();
		top.setBackground(Color.white);
		
		name = new JLabel("Name: ");
		nameSpace = new JTextField(18);
		quantity = new JLabel("Quantity: ");
		quantitySpace = new JTextField(7);
		price = new JLabel("Price £: ");
		priceSpace = new JTextField(7);
		
		top.add(name);
		top.add(nameSpace);
		top.add(quantity);
		top.add(quantitySpace);
		top.add(price);
		top.add(priceSpace);

		this.add(top, BorderLayout.NORTH);
		
			
		//Middle panel and its components-creation and addition on certain positions of the frame
		
		middle = new JPanel();
		middle.setBackground(Color.white);
					
		BorderLayout layout1 = new BorderLayout();
		middle.setLayout(layout1);

		
		//The superior part of the middle panel and the addition of the buttons and their action listeners
		
		superiorMiddle = new JPanel();
		superiorMiddle.setBackground(Color.white);
		
		salePress = new JButton("Process Sale");
		returnPress = new JButton("Process Return");
		
		superiorMiddle.add(salePress);
	    salePress.addActionListener(this);
	    superiorMiddle.add(returnPress);
		returnPress.addActionListener(this);

		middle.add(superiorMiddle, BorderLayout.NORTH);

		//The inferior part of the middle panel and the addition of the wine label
		
		inferiorMiddle = new JPanel();
		inferiorMiddle.setBackground(Color.white);
					
		BorderLayout layout2 = new BorderLayout();
		inferiorMiddle.setLayout(layout2);

		wineTitle = new JLabel("  Wine purchased:");
		
		inferiorMiddle.add(wineTitle, BorderLayout.SOUTH);

		middle.add(inferiorMiddle);		
		
		//Adding the complete middle panel to the frame
		add(middle, BorderLayout.CENTER);		
			
		
		// Bottom panel and its components-creation and addition on certain positions of the frame
			
		bottom = new JPanel();
		bottom.setBackground(Color.white);

		amount = new JLabel("Amount of Transaction £ :");
		amountSpace = new JTextField(7);
		
		balance = new JLabel("Current balance £ :");
		balanceSpace = new JTextField(7);
		
		bottom.add(amount);
		bottom.add(amountSpace);
		amountSpace.setEditable(false);
		bottom.add(balance);
		bottom.add(balanceSpace);
		balanceSpace.setEditable(false);
			
		add(bottom, BorderLayout.SOUTH);	
		

		//Showing the initial balance in the "current balance" text field that the user input in the dialog box
	
		if(newAccount.getBalance() < 0) {
		     balanceSpace.setText( String.format(" %6.2f CR",newAccount.getBalance()*-1)); // CR is used to indicate negative value
		  }
		  else {
			 balanceSpace.setText( String.format(" %6.2f", newAccount.getBalance()));
		  }
	}
	
	
    //Obtains information from the text fields, checks its validity and shows error messages if needed.
	
	private Wine wineShopping() {
	
		//Variables
		
		String brandOfWine = nameSpace.getText();
			
		double money = 0;
		int bottles = 0;
		
			//Making sure the brand/name of the wine is input + error message
			
			if(brandOfWine.isEmpty()) { 
				
				JOptionPane.showMessageDialog(null,"Please enter a brand of wine.", "Error",JOptionPane.ERROR_MESSAGE);
			}

		    //Making sure the price of the wine is in numbers 	+ error message if it's not
			
			try { 	
				money=Double.parseDouble(priceSpace.getText());
				
				      if(money <= 0) { 
					      JOptionPane.showMessageDialog(null,"Please enter a valid price of the wine.", "Error",JOptionPane.ERROR_MESSAGE);
				 }
			     }
			
		    catch(NumberFormatException exp) { 
					JOptionPane.showMessageDialog(null,"Please enter a valid price of the wine.", "Error",JOptionPane.ERROR_MESSAGE); }
			
			//Making sure the number of bottles of wine is in numbers + error message if it's not
			
			try {
				bottles = Integer.parseInt(quantitySpace.getText());
				
				     if(bottles <=0) {
					     JOptionPane.showMessageDialog(null,"Please enter a valid number of bottles.", "Error",JOptionPane.ERROR_MESSAGE);
			}
			}
			
			catch(NumberFormatException exp) { 
				JOptionPane.showMessageDialog(null,"Please enter a valid number of bottles.", "Error",JOptionPane.ERROR_MESSAGE); }
			
			//If there are no errors a new wine item is returned that passes the info to the constructor.
			
			return new Wine(brandOfWine, money, bottles); 
			
	   }
	
	
   // A method for when the process sale button is activated, updating the text field for transaction amount as appropriate
	 
	private void processWine(Wine wineDeal) {
		
		 int quantWine = wineDeal.bottleNumber();
		 double money = wineDeal.pricePerBottle();
		
		 amountSpace.setText( String.format("%6.2f", newAccount.saleMethod(quantWine, money))); 
	}

	// A method for when the process return button is activated, updating the text field for transaction amount as appropriate
	
	 private void returnWine(Wine wineDeal) {
		
		int quantWine = wineDeal.bottleNumber();
		double money = wineDeal.pricePerBottle();
		
		amountSpace.setText( String.format("%6.2f", newAccount.returnMethod(quantWine, money)));
	}
	
	/* The actionPerformed method passes the info from wineShopping to purchaseWine,
	 which then (if all information that has been input is correct) passes the info to either
	 methods for processing a sale or a return (whichever is pressed).In all instances, if
	 an incorrect data is input, an error message will pop up.*/
	
	public void actionPerformed(ActionEvent e) {
		
	  //Checking which button was pressed
	
	      if(e.getSource() == salePress) { 
				
	    	  processWine(wineShopping());
	    	  wineTitle.setText("  Wine purchased : " + nameSpace.getText()); //updating the "Wine purchased" text field with the correct wine
			}
	      else if(e.getSource() == returnPress) {
				
	    	 returnWine(wineShopping());
	    	  wineTitle.setText("  Wine returned : " + nameSpace.getText()); //updating the "Wine returned" text field with the correct wine
			}
		
	 
	     //Showing the updated balance 
	      
	      if(newAccount.getBalance() < 0) {
			     balanceSpace.setText( String.format(" %6.2f CR",newAccount.getBalance()*-1)); // CR is used to indicate negative value
			  }
			  else {
				 balanceSpace.setText( String.format(" %6.2f", newAccount.getBalance()));
			  }

		
	    //Resetting the text fields to empty
	
			nameSpace.setText(null);
			quantitySpace.setText(null);
			priceSpace.setText(null); 
	}

	}
	
