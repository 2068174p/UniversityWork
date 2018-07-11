package commandline;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.toIntExact;

public class CommandLineJSONConfiguration {

	/** This is the location of the deck file to load */
	String deckFile;

	/** This is the number of AI players to use */
	int numAIPlayers;

	public CommandLineJSONConfiguration(String fileName) {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;
			deckFile = (String) jsonObject.get("deckFile");
			numAIPlayers = toIntExact((Long) jsonObject.get("numAIPlayers"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/** Get the Deck File location */
	public String getDeckFile() {
		return deckFile;
	}

	/** Get the number of AI players to use */
	public int getNumAIPlayers() {
		return numAIPlayers;
	}

}
