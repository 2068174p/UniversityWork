import java.util.*;

/**
 * Maintains a list of Fitness Class objects
 * The list is initialized in order of start time
 * The methods allow objects to be added and deleted from the list
 * In addition an array can be returned in order of average attendance
 */

   public class FitnessProgram {
     //class constants
	 public static final int MAXIMUM = 7;
	 private static final int LENGTH = 1;
	 public static final int EARLIEST_TIME = 9;
     public static final int LATEST_TIME = EARLIEST_TIME + MAXIMUM - LENGTH;
	 public static final int FULL = -1;
     //instance variables
	 private FitnessClass [] gymClass; 
	 private int countClass; //counter 

		//constructor
		public FitnessProgram() {
			gymClass = new FitnessClass [MAXIMUM]; 
			countClass = 0; 
		}
			
		//obtain the number of objects in the array	 
		public int obtainClassesNumber() {
			return countClass;
		}
		
		/**
		 * Instantiate a new fitnessclass object
		 * via the data from the ClassesIn file
		 * and adds it to the array.
		 */
		public void fromFile(String infoClass) {
			
			if(!(infoClass==null)) {
				FitnessClass one = new FitnessClass(infoClass); 
				int location = one.obtainTime() - EARLIEST_TIME; //location in array
				gymClass[location] = one; //add to array
				countClass++; 
			}
		}

		/**
		 * new FitnessClass object established via the input in the GUI
		 * the new class is given the first available time slot and is added to the array 
		 */
		public void fromGUI(String identification, String type, String instructor) {
			FitnessClass two = new FitnessClass(identification, type, instructor);
			int slot = obtainEarliest(); //obtain the earliest free timeslot
			two.setTime(slot); //set class in the appropriate time
			int location = slot - EARLIEST_TIME; //find the location in the array
			gymClass[location] = two; //add to array
			countClass++; 
		}
		
		
	 //clear a certain FitnessClass object from the array
		public void clear(String identification) {
			FitnessClass three = obtainID(identification); //obtain the obejct via its identification
			int location = three.obtainTime() - EARLIEST_TIME; //obtain location in array
			gymClass[location] = null; //clear said class
			countClass--; //decrease counter
		}
		
		/**
		 * Obtains the information from a certain location in the array
		 * or returns null if there is no object at that position
		 */
		public FitnessClass obtainClass(int index) {
			return gymClass[index];
		}
		
		
      //Obtains the information for a class via its ID or null if it doesn't exist
		
		public FitnessClass obtainID(String identification){
			FitnessClass four = null;
			boolean check = false;
			int count = 0;
			
			//find class with identification
			while(!check && count < MAXIMUM) {
				four = gymClass[count];
				if(four != null) { 
					if(four.obtainID().equals(identification))
						check = true; // if the input=an ID in the array, exit loop
					else
						count++; //go on
				}
				else //if the location is empty go on
					count++;
			}
			
			//null if the id can't found or if it is found return the appropriate object
			if(!check) 
				return null;
			else 
				return four;
		}		
		
		
		 //get class from its starting time
		public FitnessClass obtainClassfromTime(int h) {
			int location = h - EARLIEST_TIME; 
			return gymClass[location]; //return the class in that time
		}
		
		
		 //returns the earliest available starting time for a FitnessClass.
		public int obtainEarliest() {
			//make sure there are free slots
			if(countClass == MAXIMUM)
				return -1; //return -1 if so
			
			//if there are vacancies look for the earliest one
			int slot = EARLIEST_TIME;
			boolean time = false;

			while(!time && slot <= LATEST_TIME) {
				if(obtainClassfromTime(slot)==null) //when slot is empty
					time = true; //if an empty slot is found exit
				else
					slot++; //if not, check next
			}
			return slot; 
		}
		
		
		
		 // Obtain information from FitnessClass and set attendance
		public void populateFromFile(String line) {
			String [] attenArray = line.split("[ ]+"); //use the line of text from the attendance file to make it into an array
			String id = attenArray[0]; //obtain ID from 1st position 
			FitnessClass five = obtainID(id); //obtain the class with the corresponding ID
			
			//loop through weeks and set attendance
			for(int week = 1; week <= FitnessClass.WEEKS; week++) {
				int attendance = Integer.parseInt(attenArray[week]);
				five.setAtt(week, attendance);
			}
		}

		
		// Create new array from the classes and sorts it into decreasing order
			public FitnessClass[] organizeClasses() {
			FitnessClass [] organized; 
			if(countClass == MAXIMUM) {
				organized = (FitnessClass []) gymClass.clone(); // clone the array if no null values
			}
			else { 
			//delete any null values in the array and set its size to the updated number of classes
			
				organized = new FitnessClass [countClass];
				int neew=0; 
				for(int i=0; i < MAXIMUM; i++) { 
					FitnessClass five = gymClass[i]; 
					if(five != null) { 
						organized[neew] = five; 
						neew++; 
					}
				}
			}
		
			Arrays.sort(organized);
			return organized;
		} 

			
		//Calculate the average attendance for the classes
			
		public double obtainAverage() {
			double total = 0;
			for(int i=0; i<MAXIMUM; i++) { 
				FitnessClass six = gymClass[i];
				if(six != null) 
					total += six.getAverage();
			}
			//if the number is 0, change to 1 for division purposes
			if(countClass == 0)
				countClass = 1;
			
			double average = total/countClass; 
			return average; 
		}

		
		 // sort out the text to be printed in the ClassOut file
		public String classesOutText() {
			StringBuilder builder = new StringBuilder();
			FitnessClass seven = null;
			int k = 0;
			for(int i = 0; i < MAXIMUM; i++) { 
				seven = gymClass[i];
				if(seven != null) {
					k++;
					String text = seven.classesOut();
					builder.append(text); 
					if(k != countClass)
						builder.append("\r\n");
				}
			}
			String classesOutText = builder.toString(); //convert to string
			return classesOutText;
		}
			
}
