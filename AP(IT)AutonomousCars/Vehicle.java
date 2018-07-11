
/*
 *This class provides the vehicles with functionality and 
 *specifies their velocity/course/origin
 * 
 */
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Vehicle extends Thread {

	protected int course, velocity, origin, present; 
	protected String sign;
	protected Grid followingPosition, presentPosition; 
	protected Grid[][] roads; 
	protected long originTime,totalTime;
	protected Engine engine1;

    // spec1
	public Vehicle(Grid[][] street, int route, String symbol ) {

		course = route; 
		sign = symbol;
		roads = street;
		Random rand1 = new Random();
		velocity = rand1.nextInt(500);
		

		if (route == 0) {// going down (south)
			present = 0;
			origin = rand1.nextInt(street[0].length);
			followingPosition = roads[present][origin];
		}
		else if (route == 1) {//going right (east)
			present = 0;
			origin = rand1.nextInt(street.length);
			followingPosition = roads[origin][present];
		}
		
	}

	//spec2 (linked to the engine)
	public Vehicle(int route, String symbol, Grid[][] street, Engine newEng, int inferiorOrigin, int superiorOrigin) {

		course = route;
		sign = symbol;
		roads = street;
		Random rand2 = new Random();
		velocity = rand2.nextInt(500);

		origin = ThreadLocalRandom.current().nextInt(inferiorOrigin, superiorOrigin);

		if (route == 0) { //going down (south)
			present = 0;
			followingPosition = roads[present][origin];
		}
		else if (route == 1) { //going right (east)
			present = 0;
			followingPosition = roads[origin][present];
		}
		else if (route == 2) { //going left (west)
			present = street.length - 1;
			followingPosition = roads[present][origin];
		}
		else if (route == 3) { //going up (north)
			present = street.length - 1;
			followingPosition = roads[origin][present];
		}
		
		engine1 = newEng;
	}

	    //Getters
	
		public int getRoute() {
			return course;
		}

		public int getVelocity() {
			return velocity;
		}

		public String getFigure() {
			return sign;
		}
		
		

	
	// a method for the current cell the car is in
	public Grid getPresentPosition() {
		if (getRoute() == 0) {
			presentPosition = roads[present - 1][origin];
		}
		else if (getRoute() == 1) {
			presentPosition = roads[origin][present - 1];
		}
		else if (getRoute() == 2) {
			presentPosition = roads[present +1][origin];
		}
		else if (getRoute() == 3) {
			presentPosition = roads[origin][present +1];
		}

		return presentPosition;
	}
	
	// this method checks the following cell and moves the car if it's free
	public void followingPosition() {

			if (getRoute() == 0) {
				followingPosition = roads[++present][origin]; 
			}
			else if (getRoute() == 1) {
				followingPosition = roads[origin][++present]; 
				}
			else if (getRoute() == 2) {
				followingPosition = roads[--present][origin]; 
			}
			else if (getRoute() == 3) {
				followingPosition = roads[origin][--present]; 
			}
		}
	
	// a method to make sure the end of the road has been reached 
	 public boolean finishedRoad() {
			if (course == 0 && present == roads.length - 1) {
				return false;
			}
			else if (course == 1 && present == roads[0].length - 1) {
				return false;
			}
			else if ((course == 2 || course == 3) && present == 0) {
				return false;
			}
			else
				return true;
		}
   		
	 public void run() {
		 
				originTime = System.currentTimeMillis(); //record time
			    followingPosition.carInserted(this);
				
			    while (finishedRoad() == true) {

					followingPosition();
					followingPosition.carInserted(this);
					getPresentPosition().carVacated(this);
				}
				
				try {
					Thread.sleep(velocity); 
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();  // set interrupt 
				      System.out.println("An error has occured with the thread.");
					  e.printStackTrace();
				}
				followingPosition.carVacated(this); 
				
				totalTime = System.currentTimeMillis() - originTime; // calculate the time
				if (engine1 != null) {
					engine1.getTime(totalTime);
					} // send to engine
			 
			}
			
		public long takeTime() {
			return totalTime;
		}
	


}