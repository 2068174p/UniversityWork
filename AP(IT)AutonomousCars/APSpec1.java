/*
 * The main method used for spec1.
 * A new grid object is made and vehicles
 * are active on the roads while the executor is active.
 * 
 */

  public class APSpec1 {
	
	public static void main(String args[]) {
		
			int row = 10;
			int col = 20;
			
			Grid[][] road = new Grid[row][col];
			
			//Instantiate grid
			 for(int x=0;x<row;x++) {
				System.out.println("");
					for(int y=0;y<col;y++) {
						road[x][y] = new Grid(); 
					}				
			} 
			 
			 Printer display = new Printer(road);
			 display.start(); // start the grid 
				
				while (display.isWorking()==true) { //Make new cars as long as the printer is working
					
					try {
						Thread.sleep(700); 
					} 
					catch (InterruptedException e) {
				        Thread.currentThread().interrupt();  // set interrupt 
				        System.out.println("An error has occured with the thread.");
						e.printStackTrace();
					}
					
					new Automobile(road, 0, "V").start(); //the vehicles moving south(down)
					new Automobile(road, 1, ">").start(); // the vehicles moving east(right)
				}
			}
   }