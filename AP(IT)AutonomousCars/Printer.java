
//the printer class  prints the grid and the divider in between frames

public class Printer extends Thread{

	private Grid[][] connection; 
	private StringBuilder divider;
	private int i;
	private final int limit = 2000;
	private final int update = 20;
	
	public Printer(Grid[][] roads) {
		connection = roads;
			
	}
	
	// print the grid and the divider
	public void run() {
		
		while(i<limit)  { 	// draw 2000 times
			try {
				divider = new StringBuilder();
				
				for(int x=0;x<connection.length;x++) {
					divider.append("\n");
					for(int y=0;y<connection[x].length;y++) {
						divider.append(connection[x][y].makeCell());
					}		
				}
				divider.append("\n----------------------------------------");
				Thread.sleep(update); // sleep for 20millisec
				i++;
				System.out.print(divider);
			
			} catch (InterruptedException e) {
				 Thread.currentThread().interrupt();  // set interrupt 
			     System.out.println("An error has occured with the thread.");
				 e.printStackTrace();
			}		
		}
	}
	
	
	//this method gets the status of the thread
	public boolean isWorking() {

		if (this.isAlive()) {
			return true;
		} else
			return false;
	} 
	
}