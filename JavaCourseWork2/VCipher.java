/**
 * Programming AE2
 * Class contains Vigenere cipher and methods to encode and decode a character
 */
public class VCipher
{	
	/*
	 * Variables:
	 */
	private char [] alphabet;   //the letters of the alphabet
	private final int SIZE = 26;
	private char [][] cipher ; 
	private String magicWord ;
	private int numChar ;
	
	/** 
	 * The constructor generates the cipher
	 * @param keyword the cipher keyword
	 */
	public VCipher(String keyword)
	{
		magicWord = keyword ; 
		
		//create alphabet
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++) 
		{
			alphabet[i] = (char)('A' + i);
		}
		
		//create cipher array		
		cipher = new char[magicWord.length()][SIZE];
		
		//put keyword in first column of cipher 
		for(int i = 0; i < magicWord.length(); i++)
		{
			cipher[i][0] = magicWord.charAt(i) ;
		}
        
		
		
		for(int i = 0; i <magicWord.length(); i++){ 
			for(int h = 1; h < SIZE; h++){  //begin at second col
				char previous = cipher[i][h-1] ; //assign the preceding character
				
				if (previous =='Z') 
				{
					previous = 'A'-1 ; //make these equivalent
				}
				cipher[i][h] = (char) (previous + 1); //put the following letter to this location
			}
		}
		//Print alphabet (for testing and clarity)
				for(char abc : alphabet) {
					System.out.print(abc+"|");
				}
				
				System.out.println();
				System.out.print("\n");
				
				//print the cipher in appropriate rows and columns (for testing and clarity)
				 for(int r=0; r < magicWord.length() ; r++){
					for(int c=0; c < SIZE; c++){
						System.out.print(cipher[r][c]);
						
						if(c == SIZE-1){					
							System.out.print("|");
						}else{					
							System.out.print("|");
						}
					}
					if(r == magicWord.length()-1){				
						System.out.print("");
					}else{				
						System.out.print("\n");
					}
				}
	}
	/**
	 * Encode a character
	 * @ch the character to be decoded. 
	 * @return the encoded character
	 */	
	public char encode(char ch)
	{	
		char encoded = 0 ; 
		
		
		//row of cipher array to encode 
		int k = numChar % magicWord.length() ;
		
		for (int i=0; i<alphabet.length;i++) //iterate over columns	
		{				
			if (ch==alphabet[i]) //check for character in alphabet
			{	
				numChar++ ;
				
				encoded = cipher[k][i] ; //character at equivalent position in cipher
			}	
			else if (!(ch>='A'&&ch<='Z')) {
				encoded = ch ;
			}
		}
		
	    return encoded;
	}
	
	/**
	 * Decode a character
	 * @param ch the character to be decoded 
	 * @return the decoded character
	 */  
	public char decode(char ch)
	{
		//row of cipher  to decode
		int l = numChar % magicWord.length();
		
		char decoded = 0 ; 
		for (int j=0; j<alphabet.length;j++)   //go over columns	
			if (ch == cipher[l][j]) //check for character in cipher 
			{	
				numChar++ ; //add number of characters
				
				decoded =   alphabet[j] ; //character at equivalent position in alphabet
			}
			else if (!(ch>='A'&&ch<='Z'))
			{
				decoded = ch ;
			}
	    return decoded; 
	}


	}

