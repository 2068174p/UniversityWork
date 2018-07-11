import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * Defines a GUI that displays details of a FitnessProgram object
 * and contains buttons enabling access to the required functionality.
 */
public class SportsCentreGUI extends JFrame implements ActionListener {
	
	/** GUI JButtons */
	private JButton closeButton, attendanceButton;
	private JButton addButton, deleteButton;

	/** GUI JTextFields */
	private JTextField idIn, classIn, tutorIn;

	/** Display of class timetable */
	private JTextArea display;

	/** Display of attendance information */
	private ReportFrame report;

	/** Names of input text files */
	private final String classesInFile = "ClassesIn.txt";
	private final String classesOutFile = "ClassesOut.txt";
	private final String attendancesFile = "AttendancesIn.txt";
	
	FitnessProgram oneFitness;
	
	/**
	 * Constructor for AssEx3GUI class
	 */
	public SportsCentreGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Boyd-Orr Sports Centre");
		setSize(720, 300);
		setLocation(300,150);
		display = new JTextArea();
		display.setFont(new Font("Courier", Font.PLAIN, 14));
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		layoutTop();
		layoutBottom();
		
		
		oneFitness = new FitnessProgram();
		initLadiesDay();
		initAttendances();
		updateDisplay();
		
	}

	/**
	 * Creates the FitnessProgram list ordered by start time
	 * using data from the file ClassesIn.txt
	 */
	public void initLadiesDay() {
		obtainInfoFromFile(classesInFile);
		
	}

	/**
	 * Initialises the attendances using data
	 * from the file AttendancesIn.txt
	 */
	public void initAttendances() {
		obtainInfoFromFile(attendancesFile);
		
	}
  // Reads text from file and passes relevant text to FitnessProgram method
	private void obtainInfoFromFile(String file) {
		FileReader fIn = null;
		Scanner scan = null;
		String text=null;

		try {
			try {
				fIn = new FileReader(file);
				scan = new Scanner(fIn);
				while(scan.hasNext()) {
					 text = scan.nextLine();
					if(file.equals(classesInFile))
						oneFitness.fromFile(text);
					else if(file.equals(attendancesFile))
						oneFitness.populateFromFile(text);
				}
			}
			finally {
				if(fIn != null)
					fIn.close();
				if(scan != null)
					scan.close(); 
			}
		}
		catch (FileNotFoundException e) {			
				e.printStackTrace(); }
		catch (IOException e) {
			e.printStackTrace(); }
		
	}
	
	
	
	/**
	 * Instantiates timetable display and adds it to GUI
	 */
	public void updateDisplay() {
		int begin = FitnessProgram.EARLIEST_TIME;
		int finish = FitnessProgram.LATEST_TIME;
		StringBuilder hours= new StringBuilder();
		StringBuilder names= new StringBuilder();
		StringBuilder tutors = new StringBuilder();
	

		//add each time
		for(int time = begin; time <= finish; time++) {
			hours.append(String.format(" %-12s", time + "-" + (time+1)));
			
			FitnessClass fit = oneFitness.obtainClassfromTime(time); 
			if(fit != null) { 
				//check if there is a class and add it to the display
				String name = fit.obtainName();
				names.append(String.format(" %-12s", name));
				//add tutor
				String tutor = fit.obtainTutor();
				tutors.append(String.format(" %-12s", tutor));
			}
			else { // if the slot is free
				names.append(String.format(" %-12s", "Available"));
				tutors.append(String.format(" %-12s", ""));
			}
		}
		hours.append("\n"); 
		names.append("\n");
		display.setText(hours.toString() + names + tutors);
	}

	/**
	 * adds buttons to top of GUI
	 */
	public void layoutTop() {
		JPanel top = new JPanel();
		closeButton = new JButton("Save and Exit");
		closeButton.addActionListener(this);
		top.add(closeButton);
		attendanceButton = new JButton("View Attendances");
		attendanceButton.addActionListener(this);
		top.add(attendanceButton);
		add(top, BorderLayout.NORTH);
	}

	/**
	 * adds labels, text fields and buttons to bottom of GUI
	 */
	public void layoutBottom() {
		// instantiate panel for bottom of display
		JPanel bottom = new JPanel(new GridLayout(3, 3));

		// add upper label, text field and button
		JLabel idLabel = new JLabel("Enter Class ID");
		bottom.add(idLabel);
		idIn = new JTextField();
		bottom.add(idIn);
		JPanel panel1 = new JPanel();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		panel1.add(addButton);
		bottom.add(panel1);

		// add middle label, text field and button
		JLabel nmeLabel = new JLabel("Enter Class Name");
		bottom.add(nmeLabel);
		classIn = new JTextField();
		bottom.add(classIn);
		JPanel panel2 = new JPanel();
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		panel2.add(deleteButton);
		bottom.add(panel2);

		// add lower label text field and button
		JLabel tutLabel = new JLabel("Enter Tutor Name");
		bottom.add(tutLabel);
		tutorIn = new JTextField();
		bottom.add(tutorIn);

		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Processes adding a class
	 */
	public void processAdding() {
		//check if class is full
		if(oneFitness.obtainEarliest() == FitnessProgram.FULL) {
			JOptionPane.showMessageDialog(null, "There are no available class times",
					"Error", JOptionPane.ERROR_MESSAGE);
			clearTextFields();
		}
		else {
			//read user input from GUI
			String id = idIn.getText();
			String course = classIn.getText();
			String tutor = tutorIn.getText();

			if(reviewInput(id, course, tutor)) { 
				oneFitness.fromGUI(id, course, tutor); //give to the relevant method in FitnessProgram
				clearTextFields();
				updateDisplay(); 
			}
		}
	}
	
	//Check if input is more than one word
		private boolean checkWording(String words) {
			return (words.indexOf(" ") != -1); 
		}
	
	// Check the input info
	private boolean reviewInput(String classID, String className, String classTutor) {
		boolean isFine = false; 
		
		//if empty fields exist
		if(classID.isEmpty() || className.isEmpty()|| classTutor.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Input all the information.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//if ID exists
		else if(oneFitness.obtainID(classID) != null) {
			JOptionPane.showMessageDialog(null, "A class with the same ID exists", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//if not one worded input
		else if(checkWording(classID) || checkWording(className) || checkWording(classTutor)) {
			JOptionPane.showMessageDialog(null, "Please input only single words", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		else {
			isFine = true;
		}
		return isFine; 
	}

	


	/**
	 * Processes deleting a class
	 */
	public void processDeletion() {
		String id = idIn.getText(); 
		//if ID is empty
		if(id.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Input ID, please", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//if ID exists
		else if(oneFitness.obtainID(id) == null) { 
			JOptionPane.showMessageDialog(null, "The ID was not recognized, try again", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		else { 
			oneFitness.clear(id);
			updateDisplay(); 
		}
		clearTextFields();
	}

	/**
	 * Instantiates a new window and displays the attendance report
	 */
	public void displayReport() {
		report = new ReportFrame(oneFitness); 
		report.setVisible(true);
	}
	
	//Clear GUI text fields
	private void clearTextFields(){
		idIn.setText("");
		classIn.setText("");
		tutorIn.setText("");
	}

	/**
	 * Writes lines to file representing class name, 
	 * tutor and start time and then exits from the program
	 */
	public void processSaveAndClose() {
		PrintWriter writer = null;
		try {
			try{
				writer = new PrintWriter(classesOutFile);
				String info = oneFitness.classesOutText(); 
				writer.write(info); 
			}
			finally {
				if(writer != null)
					writer.close();
			}
			System.exit(0); //exit
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "An issue with the file occured.", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Process button clicks.
	 * @param ae the ActionEvent
	 */
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(attendanceButton))
			displayReport();
		else if(ae.getSource().equals(addButton))
			processAdding();
		else if(ae.getSource().equals(deleteButton))
			processDeletion();
		else
			processSaveAndClose();
	}
}
