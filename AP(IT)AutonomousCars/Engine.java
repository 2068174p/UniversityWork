/*
 * The engine class makes and executes threads with vehicles
 * and sends the data needed for the statistics to the Data class
 */

import java.util.ArrayList;

public class Engine extends Thread {
	
	private int transit, course, inferiorSt, superiorSt; 
	private String type, title;
	private Vehicle car;
	private Grid[][] roads;
	private Printer display; 
	
	private ArrayList<Long> timeKeeper = new ArrayList<>(); // store times
	private ArrayList<Vehicle> cars = new ArrayList<>(); // store cars
	
	
	public Engine(Grid[][] streets, int traffic, int route, String form, String heading, int inferior, int superior, Printer runner) {
		roads = streets;
		transit = traffic;
		course = route;
		type = form;
		inferiorSt = inferior;
		superiorSt = superior;
	    title = heading;
		display = runner;
	}
	
	 public void run() {
	
		while (display.isWorking() == true) { 	
			
			
			try {
				Thread.sleep(transit);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();  // set interrupt 
		        System.out.println("An error has occured with the thread.");
				e.printStackTrace();
			} finally {
				car = new Automobile(course, type, roads, this, inferiorSt, superiorSt);
				car.start();
				cars.add(car);
			}
		}
		passData();//send to the Data class
		
		//make a new Data object
		if(!timeKeeper.isEmpty()) {
			new Data (title, timeKeeper);
			
		}
		
	 } 
	
	   // times for the Vehicle class
	   public void getTime(long timeTake) {
			timeKeeper.add(timeTake);
		}
	
	   // send data to the data handling class
	   public void passData() {
	
		for (int x = 0; x <cars.size(); x++) {
			long time = cars.get(x).takeTime();
			if (time !=0) {
				timeKeeper.add(time);
			}
		}
	}
		
}