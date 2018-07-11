package online.dwResources;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import online.configuration.TopTrumpsJSONConfiguration;
import game.*;
import database.Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user
 * requests a particular URL. In this case, the URLs are associated to the
 * different REST API methods that you will need to expose the game commands
 * to the Web page.
 * 
 * Below are provided some sample methods that illustrate how to create
 * REST API methods in Dropwizard. You will need to replace these with
 * methods that allow a TopTrumps game to be controled from a Web page.
 */
public class TopTrumpsRESTAPI {

	/** Game Configuration */
	private TopTrumpsJSONConfiguration gameConf;
	
	/** Session id, to maintain multiple connection */
	private int sessionID;
	
	/** List of game played concurrently */
	private ConcurrentGame concurrentGame;
	
	/** Object for data communication to database */
	private Data data;
	
	
	/** A Jackson Object writer. It allows us to turn Java objects
	 * into JSON strings easily. */
	ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	
	/**
	 * Contructor method for the REST API. This is called first. It provides
	 * a TopTrumpsJSONConfiguration from which you can get the location of
	 * the deck file and the number of AI players.
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		
		// Instantiates game configuration
		gameConf = conf;
		concurrentGame = new ConcurrentGame();	
		
		data = new Data();
		sessionID = data.getGameCounter();
	}
	
	// ----------------------------------------------------
	// API methods
	// ----------------------------------------------------
	
	@GET
	@Path("/resetGame")
	/**
	 * Reset or create new Game.
	 * @param option : to reset game or create new game
	 * @throws IOException
	 */
	public int resetGame(@QueryParam("option") String option, @QueryParam("id") String id) throws IOException {
		sessionID++;
		if (option.equals("cancel"))
			concurrentGame.getElement(Integer.parseInt(id)).cancelGame();
		
		GameEngine gameEngine = new GameEngine(gameConf.getNumAIPlayers(),gameConf.getDeckFile(),sessionID);
		gameEngine.suffleCard();
		gameEngine.setGameData(gameConf.getNumAIPlayers()+1);
		concurrentGame.addGame(gameEngine,sessionID);
		
		return sessionID;
	}
	
	
	@GET
	@Path("/gameInitInfo")
	/**
	 * Get initial information of game
	 * @return - String of information
	 * @throws IOException
	 */
	public String gameInitInfo(@QueryParam("id") String id) throws IOException {

		List<String> listOfInfos = new ArrayList<String>();
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getRoundCount()+"");
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getName());
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getPlayerType()+"");
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).printCharacteristicsHeader().trim().replace("    ", " "));
		listOfInfos.add((concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getMaxCharacteristic()+1)+"");
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getCommunalPileCardCount()+"");
		String listAsJSONString = oWriter.writeValueAsString(listOfInfos);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/gameDetailInfo")
	/**
	 * Get game detail information
	 * @return - String of information
	 * @throws IOException
	 */
	public String gameDetailInfo(@QueryParam("id") String id) throws IOException {
		return concurrentGame.getElement(Integer.parseInt(id)).printOnlineModeGameDetails();
	}
	
	
	@GET
	@Path("/playGame")
	/**
	 * Play a round of a game
	 * @param option of selected category
	 * @throws IOException
	 */
	public void playGame(@QueryParam("option") String option, @QueryParam("id") String id) throws IOException {
		concurrentGame.getElement(Integer.parseInt(id)).playCard(Integer.parseInt(option)-1);
	}
	
	@GET
	@Path("/eliminateLostPlayer")
	/**
	 * Remove eliminated player from player list
	 * @throws IOException
	 */
	public void eliminateLostPlayer(@QueryParam("id") String id) throws IOException {
		concurrentGame.getElement(Integer.parseInt(id)).removeEliminatedPlayer();
	}
	
	@GET
	@Path("/automateGame")
	/**
	 * Automate game round until user turn or game is finished
	 * @throws IOException
	 */
	public void automateGame(@QueryParam("id") String id) throws IOException {
		boolean isHumanTurn = false;
		while ((concurrentGame.getElement(Integer.parseInt(id)).getActivePlayer() > 1) && (!isHumanTurn)) {
			
			// user is the active player
			if (concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getPlayerType() == 1)
			{ 
				isHumanTurn = true;
			}
			else // set AI Player choosen characteristic
			{
				// play the round
				concurrentGame.getElement(Integer.parseInt(id)).playCard(concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getMaxCharacteristic());
				concurrentGame.getElement(Integer.parseInt(id)).removeEliminatedPlayer();
			}
		}
	}
	
	@GET
	@Path("/gameResult")
	/**
	 * Display game result
	 * @param option to add characteristic information
	 * @return - String of game result infromation
	 * @throws IOException
	 */
	public String gameResult(@QueryParam("option") String option, @QueryParam("id") String id) throws IOException {
		List<String> listOfInfos = new ArrayList<String>();
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getIsDraw()+"");
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getFirstPlayer().getName());
		listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getCommunalPileCardCount()+"");
		if (!option.equals("-1"))
			listOfInfos.add(concurrentGame.getElement(Integer.parseInt(id)).getCharacteristicDescription(Integer.parseInt(option)-1));
		String listAsJSONString = oWriter.writeValueAsString(listOfInfos);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/gameActivePlayer")
	/**
	 * Get number of active player
	 * @return - number of active player
	 * @throws IOException
	 */
	public int gameActivePlayer(@QueryParam("id") String id) throws IOException {
		return concurrentGame.getElement(Integer.parseInt(id)).getActivePlayer();
	}
	
	@GET
	@Path("/getImage")
    @Produces("image/jpg")
	/**
	 * Get image file
	 * @return - image as web service
	 * @throws IOException
	 */
	public Response getImage(@QueryParam("image") String image) throws IOException {
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("online/image/"+image+".jpg");
		BufferedImage imageBuffer = ImageIO.read(is);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(imageBuffer, "jpg", baos);
	    byte[] imageData = baos.toByteArray();
	    
	    return Response.ok(imageData).build();
	}
	
	@GET
	@Path("/getFile")
	/**
	 * Get text file
	 * @return - text file as web service
	 * @throws IOException
	 */
	public String getFile() throws IOException {
		String result = "";
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("online/image/Chart.txt");
			Scanner in = new Scanner(is);

			while (in.hasNextLine()) {
				result += in.nextLine();
			}
			
			in.close();
			is.close();
		} catch (IOException e) {
			System.out.println("Chart.txt is not found");
		}
	    
	    return result;
	}
	
	@GET
	@Path("/gameStats")
	/**
	 * Get game statistics.
	 * @return - String of game statistics
	 * @throws IOException
	 */
	public String gameStats() throws IOException {
		return oWriter.writeValueAsString(data.getGameStats());
	}
	
	@GET
	@Path("/getPrevGameStats")
	/**
	 * Retrieve previous game statistics
	 * @return - String of previous game statistics
	 * @throws IOException
	 */
	public String getPrevGameStats() throws IOException {
		return oWriter.writeValueAsString(data.getPrevGameStats());
	}
	
	@GET
	@Path("/getPrevGameWinnerStats")
	/**
	 * Retrieve previous round winner statistics
	 * @return - String of previous round winner statistics
	 * @throws IOException
	 */
	public String getPrevGameWinnerStats() throws IOException {
		return oWriter.writeValueAsString(data.getPrevGameWinnerStats());
	}
	
	@GET
	@Path("/getPlayerStats")
	/**
	 * Retrieve player statistics of 5 latest game
	 * @return - String of player statistics of 5 latest game
	 * @throws IOException
	 */
	public String getPlayerStats() throws IOException {
		return oWriter.writeValueAsString(data.getPlayerStats());
	}
	
	@GET
	@Path("/createGame")
	/**
	 * Create a new game
	 * @return - id for concurrent game
	 * @throws IOException
	 */
	public int createGame() throws IOException {
		
		sessionID++;
		GameEngine gameEngine = new GameEngine(gameConf.getNumAIPlayers(),gameConf.getDeckFile(),sessionID);
		gameEngine.suffleCard();
		gameEngine.setGameData(gameConf.getNumAIPlayers()+1);
		concurrentGame.addGame(gameEngine,sessionID);
		
		return sessionID;
	}
}
