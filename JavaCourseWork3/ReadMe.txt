Specification
Scenario
The Boyd-Orr Sports Centre has decided that it wishes to encourage the participation of women in sport.  To implement this it has decided to designate Wednesday as Ladies’ Day and develop a sports programme designed to be attractive to women.  They propose to offer a series of scheduled tutored drop-in fitness classes.  Boyd-Orr Sports Centre is receiving a grant from the organisation Healthy West End to cover their costs, but Healthy West End will only continue this grant if they feel that there is adequate participation in the classes.  They insist that attendance is monitored every five weeks and, if the overall average attendance over the five weeks is below 12, unpopular classes must be cancelled and replaced with classes that, it is hoped, will be more popular.

An application is required to display the current class schedule and enable the monitoring and replacement to take place.
Data and constraints
The object to be manipulated in the program is a list of Fitness Classes.  A Fitness Class has:

•	an ID which is a short String and may be assumed to be unique;
•	a name, which may be assumed to be a one word String (no spaces), e.g. “aerobics”;
•	a tutor’s name which again may be assumed to be a one word String, e.g. “lizzie”;
•	a start time which is an integer representing an hour, e.g. 9 represents 9am;
•	a set of five integers representing attendances for each of five weeks.

You may assume that classes always start on the hour and last for an hour.  Currently the earliest class available to Ladies Day participants starts at 09.00 and the latest class starts at 15.00.  You can assume that only one class can take place at a time (i.e., start times are unique) and there are no “forbidden” start times, i.e., any hour between 9.00 and 15.00 is a potential start time for a class.  This means that the list can contain up to seven Fitness Classes.  You should not assume that there will be exactly seven classes offered, and where there are fewer than seven classes, gaps can occur at any time of day.  You should allow for the possibility that additional classes may be offered at some later date.

Program Design
Your program should consist of five classes – these will be provided upon setup (see the Setup section), and some of these are only partially complete.

The class FitnessClass should define a Fitness Class object.  It should have a class constant representing the number of weeks over which attendances are recorded (5).  Also it should have appropriate instance variables to represent the class ID and name, the tutor’s name, the start time and the set of attendance records.

There should also be:

•	an optional default constructor, and at least one non-default constructor;
•	accessor and mutator (set and get) methods to enable all the instance variables to be accessed and given values;
•	a method to return the average attendance for the class;
•	a method to return a String formatted appropriately for the attendance report;
•	a method to compare two Fitness Class objects appropriately on average attendance.

The class FitnessProgram will maintain a list (implemented as an array) of FitnessClass objects.  It should have a class constant representing the maximum number of classes (7) and instance variables to represent the array of FitnessClass objects and the current actual number of (non-null) objects in the list.  To implement the full requirements, the methods should include:

•	A default constructor to instantiate the array.  Entry X of the array should contain the Fitness Class starting at time 9+X (or null if that time-slot is currently free).  There should also be a method to populate the attendance lists for a given Fitness Class in the array, given a String representing a single line of AttendancesIn.txt as a parameter.
•	Appropriate methods to return instance variables including a method to return the FitnessClass object at index X.
•	A method to return the FitnessClass starting at a particular time (or null if no such class exists) and a method to return the first start time that is available for a class.
•	A method to return a FitnessClass object with a given ID number in the array (or null if not present)
•	Methods to insert and delete a FitnessClass object to/from the list.
•	A method to return a list sorted in non-increasing order on the average attendance at each class.  You should use the Arrays.sort method.
•	A method to return the overall average attendance.

The class ReportFrame is a JFrame window used to display the report.  It will require FitnessProgram and JTextArea objects as instance variables.  The methods will be:

•	A constructor with a FitnessProgram parameter used to initialise the FitnessProgram instance variable and add the JTextArea component to the window.
•	A method to build the report for display on the JTextArea.

The GUI class SportsCentreGUI will provide the user interface, handle file input, process events and update the display.  In particular, it will contain:

•	A constructor which sets up the GUI and calls file input methods.
•	Methods to input the timetable and attendance data from the relevant files.
•	Methods to construct the GUI components.
•	Methods to listen for and process events, including adding and deleting a Fitness Class, displaying the report, and saving and closing.
•	A method to update the display after Add and Delete operations.

The class will have as its instance variables suitable buttons, text fields, labels and panels.  In addition it will need a FitnessProgram object.

