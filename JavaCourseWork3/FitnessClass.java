/** Defines an object representing a single fitness class
 */
public class FitnessClass implements Comparable<FitnessClass> {


		public static final int WEEKS = 5;
		
		//Instance variables
		private int startTime;	//the start time.
		private String classID, className, tutorName;	// the  class ID and the name of the class and the tutor.
		private int[] attendance;	// attendance as an array of integers

		/**constructor that takes id, name, tutor name, start time and an array of attendances*/
	    public FitnessClass(String data)
	    {
	    	String [] info = data.split("[ ]+"); // make it a String array
	    	//get class ID, name, tutor and class time 
	    	classID = info[0]; 
	    	className = info[1]; 
	    	tutorName = info[2]; 
	    	startTime= Integer.parseInt(info[3]); //make the time an integer
	    	
	    	attendance = new int[WEEKS]; // array
	    	

	    }
	    
	    
		 //Second constructor that sets the values of instance variables
		
	    public FitnessClass (String id, String name, String tutor) {
	    	
	    	classID = id;
	    	className = name;
	    	tutorName= tutor;
	    	attendance= new int [WEEKS];
	    	
	    	 }
	    
	    //Accessor methods
	    
	    public String obtainID() {
	    	
	    	return classID;
	    }
		
	    public String obtainName() {
	    	
	    	return className;
	    }
	    
	    public String obtainTutor () {
	    	
	    	return tutorName;
	    
	    }
	    public int obtainTime() {
	    	
	    	return startTime;
	    	
	    }
	    //Mutator methods
	    
	    public void setID (String idNum) {
	    	this.classID = idNum;
	    }
	    public void setName (String nameC) {
	    	this.className=nameC;
	    	
	    }
	    public void setTutor( String tut) {
	    	this.tutorName=tut;	
	    }
	    public void setTime(int time) {
	    	this.startTime = time;
	    }
	   

	    public void setAtt(int week, int att) {
	    	
	    	int index = week-1; //array has indices 0 to 4, hence the -1
	    	attendance[index]=att; //set the attendance for the position
	    	}
	    
	   
	    
	    public int getAtt (int week) {
	    	int index = week-1;
	    	return attendance[index];
	    }
	    
	    /**
		 * Method to calculate the average attendance for 
		 * the class 
		 */
	    
	    public double getAverage() {
	    	
	    	int sum = 0;
	    	double avg =0;
	    	for(int att:attendance) {
	    		sum +=att;
	    	avg = (double)(sum)/attendance.length; }
	    	
	    	return avg;
	    	
	    }
	    
	    
	
	    //Method that compares the average attendance of two Fitness Class objects 
	   public int compareTo(FitnessClass other) {
		   
		   double newAvg = getAverage();
		   double againAvg= other.getAverage();
		   
		   if (newAvg<againAvg) { 
			   return 1;   
		   }
		   else if (newAvg == againAvg)
			   return 0;
		   else
			   return -1;
		   
	  }
	   
	   //Attendance records in String format
	   
		private String recordAtt() {
			StringBuilder builder = new StringBuilder();
			for(int i=0; i < WEEKS; i++) { 
				int at = attendance[i];
				builder.append(String.format("%3d", at));
			}
			String attrecordString = builder.toString();
			return attrecordString;
		}
		
		
		  // the format in which the attendance report will be printed into
		   
	    public String report() {
			String info = String.format("%5s %15s %15s %25s %13s\n", classID, className, tutorName, recordAtt(), getAverage());
			return info;
			}
	   
		
		/**
		 * Constructs a String in which the Fitness Class will be printed out
		 * in the ClassesOut file.
		 */
		public String classesOut() {
			String last = classID + " " + className + " " + tutorName + " " + startTime;
		
			return last;
		}
	    
	    
	
	
	
	
	
	
	

   
}
