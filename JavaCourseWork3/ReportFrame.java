import java.awt.*;
import javax.swing.*;

/**
 * Class to define window in which attendance report is displayed.
 */
public class ReportFrame extends JFrame {
	
		private JTextArea display;
		private FitnessProgram fitt;
		//Constructor for ReportFrame
		public ReportFrame(FitnessProgram fitness) {
			fitt = fitness;
			setTitle(" Attendance Report ");
			setSize(700, 300); 
			setLocation(500, 500);
			
			display = new JTextArea();
			display.setFont(new Font("Courier", Font.PLAIN, 14));
			
			add(display, BorderLayout.CENTER);
			report(); //show report
		}
		
	 //Organize and show report
	  private void report() {
		
	    String topLine = String.format("%5s %15s %15s %20s %20s\n", "ID", "Class", "Tutor", "Attendance", "Average attendance");
	    String divide = "=====================================================================================";
	    StringBuilder infTable = new StringBuilder();
			
	     FitnessClass [] orgArray = fitt.organizeClasses();// obtain for average
		 for(int i=0; i < orgArray.length; i++) { 
			FitnessClass gym = orgArray[i];
			String classLine = gym.report(); //get line of report for each class
			infTable.append(classLine); 
			}
			
			double avgg = fitt.obtainAverage(); //get overall average
			String avggAll = String.format("%n%70s %.2f", "Overall Average:", avgg);
			display.setText(topLine + divide+ infTable + avggAll); // show all
		}
		
	}