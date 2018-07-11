/**
 * Programming AE2
 * Contains monoalphabetic cipher and methods to encode and decode a character.
 */
public class MonoCipher
{
	/** The size of the alphabet. */
	private final int SIZE = 26;

	/** The alphabet. */
	private char [] alphabet;
	
	/** The cipher array. */
	private char [] cipher;
	
	
	/**
	 * Instantiates a new mono cipher.
	 * @param keyword the cipher keyword
	 */
	public MonoCipher(String keyword)
	{
		int duplicate=0;
		
		// alphabet array  
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = (char)('A' + i);
			
		//cipher array
		cipher = new char[SIZE];
		int length = keyword.length();
	
		//to copy keyword into the first section of cipher array
		for(int k = 0; k<length; k++) {
			cipher[k] = keyword.charAt(k);
			}
		
		//loop through alphabet 
			for(int j = SIZE-1; j>=0; j--)
		{
				//loop each letter through the first part of cipher and make sure it is present
				for(int y = 0; y<length; y++) 
					if(alphabet[j] == cipher[y])
						duplicate++; // addition if the letter is present
				   
				   if(duplicate==0) //if not add to second part of cipher
				  {
					   cipher[length] = alphabet[j];
					   length++;
					   }
					
					else
						duplicate = 0;
			}
		
			//Printing the alphabet
			for(char abc : alphabet) {
				System.out.print(abc+"|");
			}
			
			//Printing the monocipher array
			System.out.print("\n");
			for(int a = 0; a < cipher.length; a++){			
				if(a!=cipher.length){				
					System.out.print(cipher[a] + "|");				
			}
			}
		}

	
	
	/**
	 * Encode a character
	 * @param ch the character to be encoded
	 * @return the encoded character
	 */
	public char encode(char ch)
	{
		int position; //position of char within alphabet
		int correct=0; //initialising the variable
		for(int e = 0; e<SIZE; e++)
			{
			if( alphabet[e] == ch) 
            	correct++; //addition if ch is a capital letter
			}
		if(correct==0)   //when there is a blank space do not change ch
            return ch;
       else{ 
    	   position =  ch - 'A';
			  return cipher[position]; //encoded char
			}
	}
	

	/**
	 * Decode a character
	 * @param ch the character to be encoded
	 * @return the decoded character
	 */
	public char decode(char ch)
	{
		int position =0; //position of char within alphabet
		int correct=0; //initialising the variable
		for(int d = 0; d<SIZE; d++)
		{
            if( alphabet[d] == ch) 
            	correct++; //addition if ch is a capital letter
         }
		if(correct==0) //when there is a blank space do not change ch
            return ch;
      	else{
      		
			for(int i = 0; i<SIZE; i++)
				{
				if(cipher[i] ==ch) //locate letter in cipher
					{
						position = i;
						break;
					}
				}
			return alphabet[position]; //return decoded letter
			}
		}
}