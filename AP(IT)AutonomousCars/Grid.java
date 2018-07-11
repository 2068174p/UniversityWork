
/*
 * This class handles the behaviour of the vehicles in the grid
 * In particular the condition that only one vehicle should be
 * in one cell at a given time.(locks)
 * 
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Grid {
			
	private ReentrantLock lock = new ReentrantLock();
	private Condition taken = lock.newCondition();
	private String cell = "| "; 
	private boolean unavailable = false;
	
	
	// this makes the car appear inside a grid cell
	public void carInserted(Vehicle motor) {
		try {
			Thread.sleep(motor.getVelocity());
		} catch (InterruptedException excp) {
			Thread.currentThread().interrupt();  // set interrupt 
	        System.out.println("An error has occured with the thread.");
			excp.printStackTrace();
		}	
		
		lock.lock();

		try {
		    while (unavailable) {
		        taken.await();
		    }	    
		    unavailable = true;
		    cell = "|"+ motor.getFigure();	//insert the sign for the specific car into the cell
				
		} catch (InterruptedException excp2) {
			Thread.currentThread().interrupt();  // set interrupt 
	        System.out.println("An error has occured with the thread.");
			excp2.printStackTrace();
	
		} finally {
		    lock.unlock(); 
		}
		
	}
	
	// the printer class uses this to create the cells
		public String makeCell() {
				return cell;
		}
	
	//this is used only after the car has vacated the cell
	public void carVacated(Vehicle moto) {	
		lock.lock();
		try {
		    unavailable = false;	    
		    taken.signalAll(); 
		} finally {
		   lock.unlock();
		   cell = "| ";
		   }	
	}
	
}