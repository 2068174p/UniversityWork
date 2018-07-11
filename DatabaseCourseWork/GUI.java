
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

// A class that creates the GUI components and deals with its events
public class GUI extends JFrame implements ActionListener {

	// instance variables 
	private JPanel topPanel, bottomPanel, middlePanel;
	private JButton viewCourses, viewBooking, makeBooking;
	private JTextField member, course;
	private static JTextField messageField;
	private JLabel generalLabel,memberLabel, courseLabel, messageLabel;

	private dataJDBC dataBase = new dataJDBC("m_17_2068174p", "m_17_2068174p", "2068174p");

	

	/**
	 * Adjusting the frame properties and name
	 */

	public GUI() {
		this.setSize(730, 145);
		this.setLocation(500, 300);
		this.setTitle("Gym courses bookings");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.layoutComponents();
	}

	/**
	 * Helper method to add components to the frame
	 */
	public void layoutComponents() {
		// top panel	
		topPanel = new JPanel();
		generalLabel = new JLabel ("Click here to view all the courses information :");
		viewCourses = new JButton("Display Courses Information");
		viewCourses.addActionListener(this);
		topPanel.setBackground(Color.pink);
		topPanel.add(generalLabel);
		topPanel.add(viewCourses);
		this.add(topPanel, BorderLayout.NORTH);

		// middle panel
		middlePanel = new JPanel();
		messageLabel = new JLabel("Enter what course you would like to view the bookings for :");
		messageField = new JTextField(12);
		middlePanel.add(messageField);
		viewBooking = new JButton("View Bookings");
		viewBooking.addActionListener(this);
		middlePanel.add(messageLabel);
		middlePanel.add(viewBooking);
		middlePanel.setBackground(Color.pink);
		this.add(middlePanel, BorderLayout.CENTER);

		// bottom panel 
		bottomPanel = new JPanel();
		memberLabel = new JLabel("Enter your ID");
		member = new JTextField(6);
		courseLabel = new JLabel("Enter the ID of the course you want to book");
		course = new JTextField(6);
		makeBooking = new JButton("Make a Booking");
		makeBooking.addActionListener(this);
		bottomPanel.add(memberLabel);
		bottomPanel.add(member);
		bottomPanel.add(courseLabel);
		bottomPanel.add(course);
		bottomPanel.add(makeBooking);
		bottomPanel.setBackground(Color.pink);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
	}

	
	 // Listen for and do something when button is pressed
	

	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getSource() == viewCourses) {

				dataBase.connect();
				dataBase.deafultOption();
				dataBase.closeConnection();
				
			}

			if (e.getSource() == viewBooking) {
				dataBase.connect();
				dataBase.option1(messageField.getText());
				dataBase.closeConnection();
				
				
			}

			if (e.getSource() == makeBooking) {
				dataBase.connect();
				dataBase.option2(member.getText(), course.getText(), dataBase.capacity(course.getText()));
				dataBase.closeConnection();
				
			}
			
		}

		catch (Exception excp) {
			JOptionPane.showMessageDialog(middlePanel, excp.getMessage(), "Error: Something went wrong",
					JOptionPane.ERROR_MESSAGE);
			return;
			
		
		}
	
	}
	
}
