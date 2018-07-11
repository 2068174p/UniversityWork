package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Maintains database connection
 * The methods contain query to insert and retrieve data from database
 */
public class Data {
	/** Object representing connection (session) to database */
	private Connection connection;
	
	/** Object used for executing a static SQL statement */
	private Statement st;
	
	/** Object representing a database result set */
	private ResultSet rs;
	
	/** Object that represents a precompiled SQL statement */
	private PreparedStatement pst;

	
	/** Class Constructor 
	 *  Initiate database connection
	 */
	public Data() {
		// create only one database connection
		if (connection == null)
			try {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection("jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/m_17_2140148s", "m_17_2140148s", "2140148s");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**  
	 *  Insert new game to 
	 * 
	 * @param number of player in the game 
	 */
	public void addGame(int playerNumber,int id) {
		try {
			pst = connection.prepareStatement("INSERT INTO game(id,playerNumber) VALUES(?,?)");
			pst.setInt(1, id);
			pst.setInt(2, playerNumber);
			pst.executeUpdate();

			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 * Update game winner  
	 * 
	 * @param1 id of game (primary key)
	 * @param2 winner of the game
	 */
	public void updateGame(int gameID, int winner) {
		try {
			pst = connection.prepareStatement("UPDATE game SET winner = ? WHERE id= ?");
			pst.setInt(1, winner);
			pst.setInt(2, gameID);
			pst.executeUpdate();

			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 * Insert round information  
	 * 
	 * @param1 round status (1:draw, 0:there is a round winner)
	 * @param2 winner of the round
	 * @param3 game id (game primary key)
	 */
	public void addRound(int status,int winner,int game) {
		try {
			pst = connection.prepareStatement("INSERT INTO round(status,winner,game) VALUES(?,?,?)");
			pst.setInt(1, status);
			pst.setInt(2, winner);
			pst.setInt(3, game);
			pst.executeUpdate();

			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 *  Get newly created game id (primary key)
	 *  
	 *  @return game id
	 */
	public int getGameCounter() {
		int result = 0;
		try {
			st = connection.createStatement();
			rs = st.executeQuery("select coalesce(max(id),0) maxid from game");

			if (rs.next())
				result = rs.getInt(1);
				
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**  
	 *  Retrieve overall game statistics
	 *  
	 *  @return list of game statistics
	 */
	public List<String> getGameStats() {
		ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();	
		List<String> listOfInfo = new ArrayList<String>();
		List<String> listOfInfoDetail = new ArrayList<String>();
		try {

			String sql = "select (select count(id) from game where winner is not null) gamecount, "
						+"(select count(id) from game where winner=5) humanwincount, "
						+"(select count(id) from game where winner<>5) computerwincount, "
						+"(select count(id) from round where status=1) drawcount, "
						+"count(game) from round group  by game order by count(game) desc limit 1";

			st = connection.createStatement();
			rs = st.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			listOfInfoDetail.add("Total Game");
			listOfInfoDetail.add("Human Win");
			listOfInfoDetail.add("AI Player Win");
			listOfInfoDetail.add("Total Draw");
			listOfInfoDetail.add("Largest Round");
			listOfInfo.add(oWriter.writeValueAsString(listOfInfoDetail));
			
			while (rs.next()){
				listOfInfoDetail = new ArrayList<String>();
				for (int j = 1; j <= columnCount; j++) 
					listOfInfoDetail.add(rs.getString(j));
				listOfInfo.add(oWriter.writeValueAsString(listOfInfoDetail));
			}
			
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listOfInfo;
	}

	/**  
	 *  Retrieve previous game statistics
	 *  
	 *  @return list of previous game statistics
	 */
	public List<String> getPrevGameStats() {
		ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();	
		List<String> listOfInfo = new ArrayList<String>();
		List<String> listOfInfoDetail = new ArrayList<String>();
		
		try {
			String sql = "select  "
						+"(select name from game,player where player.id=game.winner and game.id =(select max(id) from game where winner is not null)), "
						+"(select count(id) from round where status=1 and game = (select max(id) from game where winner is not null)), "
						+"count(id) from round where game = (select max(id) from game where winner is not null)";

			st = connection.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			listOfInfoDetail.add("Game Winner");
			listOfInfoDetail.add("Total Draw");
			listOfInfoDetail.add("Total Round");
			listOfInfo.add(oWriter.writeValueAsString(listOfInfoDetail));

			while (rs.next()){
				listOfInfoDetail = new ArrayList<String>();
				for (int j = 1; j <= columnCount; j++) 
					listOfInfoDetail.add(rs.getString(j));				
				listOfInfo.add(oWriter.writeValueAsString(listOfInfoDetail));
			}
			
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listOfInfo;
	}
	
	/**  
	 *  Retrieve previous round winner statistics
	 *  
	 *  @return list previous round winner statistics
	 */
	public List<String> getPrevGameWinnerStats() {
		ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();	
		List<String> listOfInfo = new ArrayList<String>();
		List<String> listOfInfoDetail ;

		try {
			
			String sql = "select name,total from(select count(winner) total,winner from(  "
						+"select * from round where status <> 1 and game = (select max(id) from game where winner is not null )) lastgame "
						+"group by winner) winner,player where player.id = winner.winner ";

			st = connection.createStatement();
			rs = st.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
	
			while (rs.next()){
				listOfInfoDetail = new ArrayList<String>();
				for (int j = 1; j <= columnCount; j++) 
					listOfInfoDetail.add(rs.getString(j));
				listOfInfo.add(oWriter.writeValueAsString(listOfInfoDetail));
			}

			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listOfInfo;
	}

	/**  
	 *  Retrieve player statistics of 5 latest game
	 *  
	 *  @return list of player statistics of 5 latest game
	 */
	public List<String> getPlayerStats() {
		ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();	
		List<String> listPlayer = new ArrayList<String>();
		List<String> listHuman = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		List<String> list4 = new ArrayList<String>();
		
		for (int j = 0; j < 5; j++) {
			listHuman.add("0");
			list1.add("0");
			list2.add("0");
			list3.add("0");
			list4.add("0");
		}

		try {
			
			String sql = "select * from(select count(winner),game,player.id from round "
						+"left join player on round.winner=player.id "
						+"inner join (select id from game  order by id desc limit 5)x on x.id = round.game "
						+"where status <> 1 "
						+"group by game,player.id order by game desc) y order by game,id ";

			st = connection.createStatement();
			rs = st.executeQuery(sql);
			int c = -1;
			String gameStr = "";
			
	
			while (rs.next()){
				if (!rs.getString(2).equals(gameStr)){
					gameStr = rs.getString(2);
					c++;
				}
				
				if (rs.getString(3).equals("0"))
					listHuman.set(c,rs.getString(1));
				else if (rs.getString(3).equals("1"))
					list1.set(c,rs.getString(1)); 
				else if (rs.getString(3).equals("2"))
					list2.set(c,rs.getString(1)); 
				else if (rs.getString(3).equals("3"))
					list3.set(c,rs.getString(1)); 
				else if (rs.getString(3).equals("4"))
					list4.set(c,rs.getString(1));
				
			}

			listPlayer.add(oWriter.writeValueAsString(listHuman));
			listPlayer.add(oWriter.writeValueAsString(list1));
			listPlayer.add(oWriter.writeValueAsString(list2));
			listPlayer.add(oWriter.writeValueAsString(list3));
			listPlayer.add(oWriter.writeValueAsString(list4));
			
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return listPlayer;
	}
	
	/**  
	 *  Cancel a game and remove associated game data
	 *  
	 *  @param game id which will be deleted
	 */
	public void cancelGame(int idGame) {
		try {
			pst = connection.prepareStatement("delete from round where game = ?");
			pst.setInt(1, idGame);
			pst.executeUpdate();
			pst.close();
			
			pst = connection.prepareStatement("delete from game where id = ?");
			pst.setInt(1, idGame);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
