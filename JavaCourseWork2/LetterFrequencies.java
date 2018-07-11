/**
 * Programming AE2
 * Processes report on letter frequencies
 */
	public class LetterFrequencies
	{
	/** Size of the alphabet */
	private final int SIZE = 26;
	
	/** Count for each letter */
	private int [] alphaCounts;
	
	/** The alphabet */
	private char [] alphabet; 
												 	
	/** Average frequency counts */
	private double [] avgCounts = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0,
			0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,  
			6.3, 9.1, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1};

	/** Character that occurs most frequently */
	private char maxCh;

	/** Total number of characters encrypted/decrypted */
	private int totChars;
	
	/** frequency of most frequent character */
	private double maxFreq ;
	
	/**
	 * Instantiates a new letterFrequencies object.
	 */
	public LetterFrequencies()
	{
		//create alphabet
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = (char)('A' + i);
		//initialise variables
		totChars = 0 ;
		alphaCounts = new int [SIZE];
		 maxCh = ' ' ;
		 }
		
	/**
	 * Increases frequency details for given character
	 * @param ch the character just read
	 */
	public void addChar(char ch)
	{	
	    	for (  int k = 0 ; k < alphabet.length ; k++)
	    	{
	    	    //frequency of characters
	            if(ch == alphabet[k]) {
	            	alphaCounts[k] = alphaCounts[k] + 1; 
	                 totChars++;}
	            } 
	    }
	
	/**
	 * Gets the most frequent character
	 * @return the most frequent character
	 */
	public char  getMaxPC()
     {
		int maximum  = alphaCounts[0];
		for ( int m = 0; m< alphaCounts.length; m++)
		{
			if (alphaCounts[m]>=maximum) //going over alphabet 
			{
				maximum = alphaCounts[m] ; // amending maximum frequency
				maxCh = alphabet[m] ; // getting most frequent character
			}
		}
		return maxCh ; 
		}
	
	/**
	 * The method to obtain the frequency report from the encryption/decryption
	 * @return the frequency report 
	 */
	public String getReport(char freqChar, int num)
	{	
		String frqReport = "" ; 
		freqChar = alphabet[num] ; //character to report
		
		//Calculating the elements of the frequency report table:
		int frequency = alphaCounts[num] ; //frequency of  a character
		double percent = alphaCounts[num]*100/ totChars ; //frequency in %
		double average = avgCounts[num] ; //average frequency
		double difference = percent - average ; 
		
		frqReport = String.format("%s   %s     %d     %.1f     %.1f       %.1f", 
				"", freqChar, frequency, percent, average, difference) ;	
	  
		return frqReport;
	}
	
	// A method that obtains the character with the maximum frequency 
	
	public double getmaxFreq()
	{
		maxFreq  = alphaCounts[0]; 
		for ( int i = 0; i< alphaCounts.length; i++)
		{
			if (alphaCounts[i]>maxFreq) //going over the alphabet 
			{
				maxFreq = alphaCounts[i] ; //changing maximum frequency
			}
		}
		return maxFreq*100/totChars; // returning the maximum frequency of the character
	}
}