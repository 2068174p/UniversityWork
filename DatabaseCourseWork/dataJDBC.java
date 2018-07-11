import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import java.sql.Connection;
import javax.swing.JFrame;

public class dataJDBC extends JFrame {

	private Connection connection = null;
	private String database;
	private String memberName;
	private String password;

	public dataJDBC(String database, String memberName, String password) {
		this.database = database;
		this.memberName = memberName;
		this.password = password;
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/" + database, memberName, password);
		} catch (SQLException e) {
			System.err.println("Connection Failed!");
			e.printStackTrace();
			return;
		}
		if (connection != null) {
		} else {
			System.err.println("Failed to make connection!");
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection could not be closed – SQL exception");
		}
	}

	public void deafultOption() {
		Statement stmt = null;
		String query = "SELECT count(membercourse.membership_number), course.max_places, course.course_name, course.course_id, instructor.instructor_name FROM gym.course \r\n"
				+ "INNER JOIN gym.instructor ON gym.course.instructor_number = gym.instructor.instructor_number\r\n"
				+ "INNER JOIN gym.membercourse ON membercourse.course_id = course.course_id\r\n"
				+ "GROUP BY course.course_name, course.course_id, course.max_places, instructor.instructor_name;";
		
		StringBuilder s = new StringBuilder();
		s.append(String.format("%25s%25s%25s%25s%25s\n\n", "Course Name", "Course ID", "Maximum members", "Instructor", "Number of current members"));
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// the next method of ResultSet allows you to iterate through the results

			while (rs.next()) {
				// next string contains the names of the columns
				
				String courseName = rs.getString("course_name");
				String courseID = rs.getString("course_id");
				String capacity = rs.getString("max_places");
				String instructorName = rs.getString("instructor_name");
				String count = rs.getString("count");
				s.append(String.format("%25s%25s%25s%25s%25s\n", courseName, courseID, capacity,
						instructorName, count));
			}
			
			JTextArea messageArea = new JTextArea(s.toString());
			messageArea.setFont(new Font(Font.SERIF, Font.TYPE1_FONT, 18));
			messageArea.setOpaque(false);
			JOptionPane.showMessageDialog(null, messageArea);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Something went wrong");
		}
	}

	public void option1(String courseName) {
		Statement stmt = null;
		String query = "SELECT membercourse.membership_number, member.member_name, course.course_name FROM gym.membercourse \r\n"
				+ "INNER JOIN gym.member ON member.membership_number=membercourse.membership_number\r\n"
				+ "INNER JOIN gym.course ON course.course_ID=membercourse.course_ID WHERE course_name = '" + courseName
				+ "'";
		StringBuilder s = new StringBuilder();
		s.append(String.format("%25s%25s%25s\n\n", "Membership Number", "Name", "Course"));
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// the next method of ResultSet allows to iterate through the results
			while (rs.next()) {
				String membership_number = rs.getString("membership_number");
				String member_name = rs.getString("member_name");
				String course_name = rs.getString("course_name");
				System.out.println("");
				s.append(String.format("%25s%25s%25s\n", membership_number, member_name, course_name));
			}
			JTextArea messageArea = new JTextArea(s.toString());
			messageArea.setFont(new Font(Font.SERIF, Font.TYPE1_FONT, 18));
			messageArea.setOpaque(false);
			JOptionPane.showMessageDialog(null, messageArea);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void option2(String memberNumber, String courseID, int bookingNumber) {
		Statement stmt = null;

		String query = "INSERT INTO gym.MemberCourse (membership_number, course_ID, booking_number) VALUES ('"
				+ memberNumber + "','" + courseID + "','" + bookingNumber + "')";
		try {
			stmt = connection.createStatement();
			if (bookingNumber <= 40) {
				stmt.executeUpdate(query);
				JOptionPane.showMessageDialog(null, "The booking was made successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "There are no more available places in this course.Sorry.");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: Invalid values entered", "Error", JOptionPane.ERROR_MESSAGE);
		
		}
	}

	public int capacity(String courseID) {
		Statement stmt = null;
		int capacity = 0;
		String query = "SELECT MAX(booking_number) FROM gym.membercourse WHERE course_id = '" + courseID + "'";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				capacity = rs.getInt("max") + 1;

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return capacity;
	}
}
