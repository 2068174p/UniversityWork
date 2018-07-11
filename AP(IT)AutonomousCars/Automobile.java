
/*
 * The automobile class which extends from vehicle
 *  and the constructors for the two specs are made.
 *  Further details can be added here if the project is
 *  to be extended. 
 */

   public class Automobile extends Vehicle{
	


	
	// Constructor for spec 1 (used in APSpec1)
	public Automobile(Grid[][] roads, int course, String form ) {
		super(roads, course, form);
	}
	
	
	
	// spec 2 (this is used in the engine class)
	public Automobile(int course, String form, Grid[][] roads, Engine newEng, int inferiorPos, int superiorPos) {
		super(course, form, roads, newEng, inferiorPos, superiorPos);
	}
	

}