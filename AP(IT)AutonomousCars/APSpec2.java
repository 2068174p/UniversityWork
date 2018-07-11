
// displays spec2 functionality

public class APSpec2 {

	
	public static void main(String args[]) {
	
		int row = 10;
		int col = 20;
		
		Grid[][] roads = new Grid[row][col];
		
		
		//Instantiate grid
		for(int x=0;x<row;x++) {
				for(int y=0;y<col;y++) {
					roads[x][y] = new Grid(); 
				}				
		}

			Printer printer = new Printer(roads);
			printer.start();
			
			// 4 engines with grid, traffic, route, form, origin, printer
			
			Engine power1 = new Engine(roads, 1000, 0,"v", "down", 0,10,  printer); 
			power1.start();
			Engine power2 = new Engine(roads, 1000, 2,"^", "up", 10,20,  printer);  
			power2.start();
			Engine power3 = new Engine(roads, 1000, 1,">", "right", 0,5,  printer);  
			power3.start();
			Engine power4 = new Engine(roads, 1000, 3,"<", "left", 5,10,  printer);  
			power4.start();
			
			
			try {
				power1.join();
				power2.join();
				power3.join();
				power4.join();
				} 
			catch (InterruptedException e) {
				  Thread.currentThread().interrupt();  // set interrupt 
			      System.out.println("An error has occured with the thread.");
				  e.printStackTrace();
			}
	}

}