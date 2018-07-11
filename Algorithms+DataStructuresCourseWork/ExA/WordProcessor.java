
import java.io.FileNotFoundException;
import java.util.Set;
import java.io.File;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * @author Martina Pironkova
 * Student ID 2068174
 */



  public class WordProcessor {
	
	private static <E> String displaySet(Set<E> inputSet){
	
		
	
		StringBuilder stringer = new StringBuilder();
		int elements = 0;
		
		for(E element : inputSet) {
			if(elements < 4) {
				stringer.append(element.toString() + ", ");
				elements++;
				} 
			else {
				stringer.append(element.toString() + ", ");
				stringer.append("\n");
				elements = 0;
	           }
		   }
		return stringer.toString();
	}
		
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
         // A set of type String
        Set<String> wordSet = new TreeSet<>();
        
        //A set of type CountedElement
        Set<CountedElement<String>> countedWordSet = new TreeSet<>();
        
        Scanner scan = null;

        for(String file : args) {
            try {
                scan = new Scanner(new File(file));
                
                while(scan.hasNext()) { // for every word
                    String word = scan.next();
                    
                    if(!wordSet.contains(word)) {
                    	wordSet.add(word);
                       countedWordSet.add(new CountedElement<>(word,1));
                        
                    } else {
                    	
                        for(CountedElement<String> element : countedWordSet) {
                        	
                        	//if the word and the element are "equal"
                            if(element.getElement().equals(word)) {
                                int currentCount = element.getCount();
                                element.setCount(currentCount + 1);//add 1
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

	System.out.println(displaySet(countedWordSet));

	}
}
