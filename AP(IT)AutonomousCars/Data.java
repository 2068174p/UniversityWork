
// this class is used to calculate the statistics and print them out
import java.util.ArrayList;

public class Data {
	
	private long minimum, maximum, total, avg, number, variance, square, squareTotal;
	private ArrayList<Long> dataList;
	private String engName; 
	
	
	public Data(String name, ArrayList<Long> timesData ) {
		
		name=engName;
		dataList = timesData;
		number = dataList.size();
		
		//Instantiate the maximum and minimum
		maximum = dataList.get(0);
		minimum = dataList.get(0);

		//Find out the maximum and minimum by comparison
		for (int x = 0; x < dataList.size(); x++) {
			if (maximum < dataList.get(x)) {
				maximum = dataList.get(x);
			} else if (minimum > dataList.get(x)) {
				minimum = dataList.get(x);
			}
			total += dataList.get(x);
		}
		avg = total/number; //get the average
		
	   // Get variance
		for (int y = 0; y < dataList.size(); y++) {
			square = dataList.get(y) - avg;
			square = (long) Math.pow(square, 2);
			squareTotal += square;
			}
			variance = (long) Math.sqrt(squareTotal / number);
			
			printData();
		}
	
	public void printData() {
		StringBuilder data = new StringBuilder("\nStatistics for " + engName + " :\n");
		data.append(String.format("Minimum time: %d ms\nMaximum time: %d ms\nAverage time: %d ms\nVariance: %d ms\n", minimum,
				maximum, avg, variance));
		data.append("The total number of cars is: "+ number + "\n");
		System.out.println(data.toString());
		
	}
	
	
	

}