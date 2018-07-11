import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;

/** 
 * Programming AE2
 * Class to display cipher GUI and listen for events
 */
    public class CipherGUI extends JFrame implements ActionListener
  {
	//instance variables which are the components
	private JPanel top, bottom, middle;
	private JButton monoButton, vigenereButton;
	private JTextField keyField, messageField;
	private JLabel keyLabel, messageLabel;
	private String magicWord;

	// application instance variables
	private MonoCipher monoCipher;
	private VCipher vigenereCipher;
	private LetterFrequencies frequencyLetter;
	private String fileName; 
	
	private boolean whichAction; // used to identify which button was pressed
	private FileReader fInRead; 
	private FileWriter fOutWrite;	
	private PrintWriter freqWrite ;
	
	/**
	 * The constructor adds all the components to the frame
	 */
	public CipherGUI()
	{
		this.setSize(400,150);
		this.setLocation(100,100);
		this.setTitle("Cipher GUI");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.layoutComponents();
	}
	
	/**
	 * Helper method to add components to the frame
	 */
	public void layoutComponents()
	{
		//top panel is yellow and contains a text field of 10 characters
		top = new JPanel();
		top.setBackground(Color.yellow);
		keyLabel = new JLabel("Keyword : ");
		top.add(keyLabel);
		keyField = new JTextField(10);
		top.add(keyField);
		this.add(top,BorderLayout.NORTH);
		
		//middle panel is yellow and contains a text field of 10 characters
		middle = new JPanel();
		middle.setBackground(Color.yellow);
		messageLabel = new JLabel("Message file : ");
		middle.add(messageLabel);
		messageField = new JTextField(10);
		middle.add(messageField);
		this.add(middle,BorderLayout.CENTER);
		
		//bottom panel is green and contains 2 buttons
		
		bottom = new JPanel();
		bottom.setBackground(Color.green);
		//create mono button and add it to the top panel
		monoButton = new JButton("Process Mono Cipher");
		monoButton.addActionListener(this);
		bottom.add(monoButton);
		//create vigenere button and add it to the top panel
		vigenereButton = new JButton("Process Vigenere Cipher");
		vigenereButton.addActionListener(this);
		bottom.add(vigenereButton);
		//add the top panel
		this.add(bottom,BorderLayout.SOUTH);
	}
	
	/**
	 * Listen for and react to button press events
	 * (use helper methods below)
	 * @param e the event
	 */
	 public void actionPerformed(ActionEvent e) 
	{
		 
	    boolean whichCipher=false;   //initialise boolean
	    String keyWord = keyField.getText(); //get the keyword the user has entered
	    
	    	if (getKeyword() && processFileName()) {
	    	 
	    		if(e.getSource().equals(monoButton)) {  // the mono cipher button pressed 
	    		
	    		  monoCipher = new MonoCipher(keyWord); // new MonoCipher object
	    		  whichCipher = false;  //set boolean to false  
	    	    }
	    	     else if (e.getSource().equals(vigenereButton)) {  // the vigenere cipher button pressed
	    		
	    		vigenereCipher = new VCipher(keyWord); // new VCipher object
	    		 whichCipher = true; //set boolean to true
	    	   }
	    	
	    	frequencyLetter = new LetterFrequencies(); // new LetterFrequencies object
	
	    	if(processFile(whichCipher)) {
	    		freqWriter(); //write the frequency report
	    		System.exit(0); //exit the program 
	    		}
	    	}
	   }
	
	/** 
	 * Obtains cipher keyword
	 * If the keyword is invalid, a message is produced
	 * @return whether a valid keyword was entered
	 */
     private boolean getKeyword()  {
	 
    	magicWord = keyField.getText(); // obtain keyword
		
		if (!magicWord.isEmpty()) {  //make sure it's not empty
			
			for (int i = 0; i <magicWord.length(); i++) {  // go over keyword
				for (int d = 0; d < magicWord.length(); d++) {
				    if (magicWord.charAt(i)< 'A' || magicWord.charAt(i)> 'Z') { //Make sure the keyword is in capital letters
					    JOptionPane.showMessageDialog(null, "Please use only capital letters for the keyword");
					    keyField.setText(""); // reset the keyword field 
					    return false;

						}
					  if (magicWord.charAt(i) == magicWord.charAt(d) && i!=d) { //Make sure there are no repeated characters
						 JOptionPane.showMessageDialog(null, "Please don't repeat letters in the keyword");
						 keyField.setText("");
						 return false;
					    }	
					  }
				}
			}
		else  { // error message if keyword has not been entered
			JOptionPane.showMessageDialog(null, "Please input keyword.");
			return false;
			}
		return true;  
		}
	
	/** 
	 * Obtains filename from GUI
	 * The details of the filename and the type of coding are extracted
	 * If the filename is invalid, a message is produced 
	 * The details obtained from the filename must be remembered
	 * @return whether a valid filename was entered
	 */
	private boolean processFileName() { 
		fileName = messageField.getText(); //obtain the file name
	   
	      //Make sure there is a file name entered and show error message if not
	       if(fileName.isEmpty() || fileName == null) {
	    	JOptionPane.showMessageDialog(null, "Please enter file name","Error", JOptionPane.ERROR_MESSAGE);
	    	return false; 
	    	} 
	       
	       char method = fileName.charAt(fileName.length()-1);  //has the last letter of the file name 
	       if(method == 'P' || method == 'C') { //check if it is P or C
	    	   fileName = fileName.substring(0,fileName.length()-1); //removing the last character from the file name
	    	if(method == 'P')
	    		whichAction = true; // encryption
	    	  else
	    		whichAction=false; // decryption

	 
	    	return true;
	    }
	   
	    else { //error message for an invalid file name 
	    
	    	JOptionPane.showMessageDialog(null, "Please enter a file name that ends in a 'P' or 'C'", "Error", JOptionPane.ERROR_MESSAGE);
	    	
	    	messageField.setText(null); //reset the textfield
	    	return false;
	    }
	}
	

	/** 
	 * Reads the input text file character by character
	 * Each character is encoded or decoded as appropriate
	 * and written to the output text file
	 * @param vigenere whether the encoding is Vigenere (true) or Mono (false)
	 * @return whether the I/O operations were successful
	 */
	private boolean processFile(boolean vigenere)
    {
		  try {
				fInRead = new FileReader(readerName()); //create new FileReader object
				fOutWrite = new FileWriter(writerName()); //create new FileWriter object
			
				for(;;) { 
					int end = fInRead.read();
					if(end == -1) { //when the end of the file is reached
						break;   // exit loop
					}
					else {
						char actual = (char) end;
						//encrypt/decrypt a char and print the result on the file out
						fOutWrite.write(processChar(actual, vigenere)); 
						}
					}
				//close files
				fInRead.close();
				fOutWrite.close(); 
			    return true; 
			    }

		catch(IOException e) { //error message if the file cannot be found
		JOptionPane.showMessageDialog(null, "The file you entered doesn't exist", "Error", JOptionPane.ERROR_MESSAGE);
	     messageField.setText(""); // reset the file name field 
		 return false; 
		 }
   }

	/**
	 * Obtain the name of the file to be read from, when 
	 * encryption or decryption is to be executed.
	 * @return The correct name of the file.
	 */
	private String readerName() 
	{
		if(whichAction) // encryption
			return fileName + "P.txt";
		else // decryption
			return fileName + "C.txt";
	}
	
	/**
	 *Obtain the name of the file to be written to,
	 *when encryption or decryption is to be executed.
	 * @return The correct name of the file.
	 */
	private String writerName() 
	{
		if(whichAction) //encryption
			return fileName + "C.txt";
		else // decryption'
			return fileName + "D.txt";
	}
	
	/**
	 * A method that processes each letter,namely
	 * encode or decode it and pass it to letter frequencies
	 * method addChar. @param letter the character to be encoded or decoded
	 * @param vigCode boolean indicating which cipher is being used
	 * @return letter (which has either been encoded or decoded)
	 */

	      private char processChar(char letter, boolean vigCode) {	
	    	
	    	  if(whichAction) { // if encryption is required
	    		  if(!vigCode) { // if the Mono cipher encryption will be used
	    			  letter = monoCipher.encode(letter);
	    			  }
	    		  else { // if the Vigenere cipher encryption will be used
	    			  letter = vigenereCipher.encode(letter); 
	    			  }
	    		  }
	    	  else { //if decryption is needed
	    		  if(!vigCode) { //if the Mono cipher decryption will be used
	    			  letter = monoCipher.decode(letter);
	    			  }
	    		  else { // if Vigenere cipher decryption will be used
	    			 letter = vigenereCipher.decode(letter);
	    			  }
	    		
	    		  }
	    	//pass the new character to LetterFrequencies object   
	    	  frequencyLetter.addChar(letter);
			 
		   
			// return the character (encoded/decoded)
		    return letter;
	      }
	
	/**
	 * Writes the letter frequencies report to a file
	 * 
	 */
	public void freqWriter()
	{
		try {	
			freqWrite = new PrintWriter(fileName+"F.txt"); // frequencies file
			
			//column titles on the report
			freqWrite.println("" +"Letter" +" "+"Freq"  +"   " +" Freq%" +"  " +"AvgFreq%"+"   "+"Diff");
			
			for (int j = 0; j < 26; j++) { //go over alphabet
				
				//obtain report for a certain character with the corresponding char from the alphabet
				freqWrite.println(frequencyLetter.getReport((char)j,j)); 
			}
			//obtain and print the most frequent character
			double maxFreq = frequencyLetter.getmaxFreq(); 
			freqWrite.println(String.format("The most frequent character is %s "
			 + "at %.1f %s ", frequencyLetter.getMaxPC(), maxFreq,"%")); 
			
					freqWrite.close(); //close writer
		}
		catch (IOException excep) //in case it is not possible to write this file
		{
			JOptionPane.showMessageDialog(null, "The file cannot be written", "Error", JOptionPane.ERROR_MESSAGE);
			messageField.setText(null);
		}
		
	} 
	
  }