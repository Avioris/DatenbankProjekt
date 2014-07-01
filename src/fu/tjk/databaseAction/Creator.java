package fu.tjk.databaseAction;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class creates a wekafile with the realtion "Features" and following
 * attributes: 
 * 1. Name of the team
 * 2. Name of the enemy team
 * 3-5. Goals of the last three games for team
 * 6-8. Goals for enemy of last three games
 * 9. Average goal rate in the last 5 games of team
 * 10. Average goal rate in the last 5 games of team
 * 11. Team rate of goal to pass overall
 * 12. Enemy team rate of goal to pass overall
 * 13-15. Goals for team of last three games as home game
 * 16-18. Goals for enemy team of last three games as home game
 * 19-23. Outgoing of the last five games for team
 * 
 * File will be created in /home/user/dbs2014/
 */
public class Creator {

	private DBCon con = new DBCon();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	private WekaFile wekaFile;
	private StringBuilder valueLine;

	/**
	 * Creates directory and initialize the wekafile.
	 */
	public Creator() {
		java.util.Properties properties = new Properties(System.getProperties());

		String home = properties.getProperty("user.home");
		String separator = properties.getProperty("file.separator");
		String dirPath = separator + home + separator
				+ "DBS2014";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		wekaFile = new WekaFile(separator + home + separator 
				+ "DBS2014" + separator);
	}

	public void go() {
		connect();
		getTeamInfos();
		initWekaFile();
		createFeatures();
		createWekaFile();
		close();

	}

	private void createWekaFile() {
		System.out.println("Creating WekaFile...");
		wekaFile.createFile();
		System.out.println("Created WekaFile!");
	}

	private void close() {
		System.out.println("Closing Connection...");
		con.closeConnection();
		System.out.println("Connection closed!");
	}

	private void connect() {
		System.out.println("Creating Connection...");
		con.createConncetion();
		System.out.println("Connected!");

	}

	private void initWekaFile() {

		System.out.println("Init WekaFile...");
		// Set Relation
		wekaFile.addRealtion("Features");

		// Name of team and enemy
		wekaFile.addAttribute("Team", names.toArray(new String[names.size()]));
		wekaFile.addAttribute("Gegner", names.toArray(new String[names.size()]));

		// Goals for team of last 3 games
		wekaFile.addAttribute("Team-Tore-Letztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Vorletztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Vorvorletztes-Spiel", WekaFile.NUMERIC);

		// Goals for enemy of last 3 games
		wekaFile.addAttribute("Gegener-Tore-Letzes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Gegner-Tore-Vorletztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Gegner-Tore-Vorvorletztes-Spiel",
				WekaFile.NUMERIC);

		// average goal rate in the last 5 games of team
		wekaFile.addAttribute("Team-Durchschnittliche-Tore-Letzte-5-Spiele",
				WekaFile.NUMERIC);

		// average goal rate in the last 5 games of enemy
		wekaFile.addAttribute("Gegener-Durchschnittliche-Tore-Letzte-5-Spiele",
				WekaFile.NUMERIC);

		// team rate of goal to pass overall
		wekaFile.addAttribute("Team-Tor-zu-Vorlagen-Quote", WekaFile.NUMERIC);

		// enemy rate of goal to pass overall
		wekaFile.addAttribute("Gegner-Tor-zu-Vorlagen-Quote", WekaFile.NUMERIC);

		// goals for team of last three games as home game
		wekaFile.addAttribute("Team-Tore-Heim-Letztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Heim-Vorletztes-Spiel",
				WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Heim-Vorvorletztes-Spiel",
				WekaFile.NUMERIC);

		// goals for enemy of last three games as home game
		wekaFile.addAttribute("Gegner-Tore-Heim-Letztes-Spiel",
				WekaFile.NUMERIC);
		wekaFile.addAttribute("Gegner-Tore-Heim-Vorletztes-Spiel",
				WekaFile.NUMERIC);
		wekaFile.addAttribute("Gegner-Tore-Heim-Vorvorletztes-Spiel",
				WekaFile.NUMERIC);

		// outgoing of last 5 games for team
		wekaFile.addAttribute("Ausgang-Spiel1", new String[] { "Gewonnen",
				"Unentschieden", "Verloren" });
		wekaFile.addAttribute("Ausgang-Spiel2", new String[] { "Gewonnen",
				"Unentschieden", "Verloren" });
		wekaFile.addAttribute("Ausgang-Spiel3", new String[] { "Gewonnen",
				"Unentschieden", "Verloren" });
		wekaFile.addAttribute("Ausgang-Spiel4", new String[] { "Gewonnen",
				"Unentschieden", "Verloren" });
		wekaFile.addAttribute("Ausgang-Spiel5", new String[] { "Gewonnen",
				"Unentschieden", "Verloren" });
	}

	private void getTeamInfos() {
		System.out.println("Getting name and id for each team...");
		String query = Querys.TEAMS_IDS;

		try {
			ResultSet set = con.searchInDB(query);
			while (set.next()) {
				names.add(set.getString("Name"));
				ids.add(set.getInt("V_ID"));
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Got names(" + names.size() + ") and ids("
				+ ids.size() + ")!");

	}

	private void createFeatures() {
		System.out.println("Creating Features...");
		// all teams
		for (int i = 0; i < names.size() - 1; i++) {
			// versus every other team thats not calculated already
			for (int j = i + 1; j < names.size(); j++) {

				valueLine = new StringBuilder();
				String tName = names.get(i);
				String eName = names.get(j);
				String tID = ids.get(i).toString();
				String eID = ids.get(j).toString();

				// add team and enemy
				createNames(tName, eName);

				// Goals for team of last 3 games
				createFeature1(tID);

				// Goals for enemy of last 3 games
				createFeature2(eID);

				// average goal rate in the last 5 games of team
				createFeature3(tID);

				// average goal rate in the last 5 games of enemy
				createFeature4(eID);

				// team rate of goal to pass overall
				createFeature5(tID);

				// average goal rate in the last 5 games of enemy
				createFeature6(eID);

				// goals for team of last three games as home game
				createFeature7(tID);

				// goals for enemy of last three games as home game
				createFeatuere8(eID);

				// outgoing of last 5 games for team
				createFeature9(tID);

				// delete last char
				valueLine.deleteCharAt(valueLine.length() - 1);

				// add values to file
				wekaFile.addValueLine(valueLine.toString());

			}
		}
		System.out.println("Created all Features!");

	}

	/**
	 * Add the outgoings of the last five games for team to the value line.
	 * @param tID Id of the team.
	 */
	private void createFeature9(String tID) {
		String query = Querys.GOALS_OF_LAST_FIVE_GAMES.replace("TEAM", tID);
		ResultSet set = con.searchInDB(query);

		try {
			while (set.next()) {
				valueLine.append(" '");
				int g_home = set.getInt("Tore_Heim");
				int g_guest = set.getInt("Tore_Gast");

				if (set.getInt("Heim") == Integer.valueOf(tID)) {
					if (g_home > g_guest) {
						valueLine.append("Gewonnen");
					} else if (g_home == g_guest) {
						valueLine.append("Unentschieden");
					} else {
						valueLine.append("Verloren");
					}
				} else {
					if (g_home < g_guest) {
						valueLine.append("Gewonnen");
					} else if (g_home == g_guest) {
						valueLine.append("Unentschieden");
					} else {
						valueLine.append("Verloren");
					}
				}
				valueLine.append("',");
			}
			set.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the goals of the last three home games for enemy team.
	 * 
	 * @param eID
	 *            Id of enemy team.
	 */
	private void createFeatuere8(String eID) {
		String query = Querys.GOALS_OF_LAST_THREE_HOME_GAMES.replace("TEAM",
				eID);
		ResultSet set = con.searchInDB(query);

		try {
			while (set.next()) {
				int goals = set.getInt("Tore_Heim");
				valueLine.append(goals);
				valueLine.append(", ");
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the goals of the last three home games for team.
	 * 
	 * @param tID
	 *            Id of the team.
	 */
	private void createFeature7(String tID) {
		String query = Querys.GOALS_OF_LAST_THREE_HOME_GAMES.replace("TEAM",
				tID);
		ResultSet set = con.searchInDB(query);

		try {
			while (set.next()) {
				int goals = set.getInt("Tore_Heim");
				valueLine.append(goals);
				valueLine.append(", ");
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method adds the rate of goals to passes overall for enemy team.
	 * 
	 * @param eID
	 *            The Id of the enemy team.
	 */
	private void createFeature6(String eID) {
		String query = Querys.PASS_AND_GOALS_OF_TEAM.replace("TEAM", eID);
		ResultSet set = con.searchInDB(query);
		int goals = 0;
		int pass = 0;

		try {
			while (set.next()) {
				goals += set.getInt("Tore");
				pass += set.getInt("Vorlagen");
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double result = goals / (goals + pass * 1.0) * 100;
		valueLine.append(result);
		valueLine.append(", ");
	}

	/**
	 * This method adds the rate of goals to passes overall for team.
	 * 
	 * @param tID
	 *            The Id of the team.
	 */
	private void createFeature5(String tID) {
		String query = Querys.PASS_AND_GOALS_OF_TEAM.replace("TEAM", tID);
		ResultSet set = con.searchInDB(query);
		int goals = 0;
		int pass = 0;

		try {
			while (set.next()) {
				goals += set.getInt("Tore");
				pass += set.getInt("Vorlagen");
			}
			set.close();
			double result = goals / (goals + pass * 1.0) * 100;
			valueLine.append(result);
			valueLine.append(", ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the average goal rate per game for team of the last five games to the
	 * value line.
	 * 
	 * @param eID
	 *            Id of the enemy team.
	 */
	private void createFeature4(String eID) {
		String query = Querys.GOALS_OF_LAST_FIVE_GAMES.replace("TEAM", eID);
		ResultSet set = con.searchInDB(query);
		int goals = 0;

		try {
			while (set.next()) {
				if (set.getInt("Heim") == Integer.valueOf(eID)) {
					goals += set.getInt("Tore_Heim");
				} else {
					goals += set.getInt("Tore_Gast");
				}
			}
			set.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double result = goals / 5.0;
		valueLine.append(result);
		valueLine.append(", ");

	}

	/**
	 * Add the average goal rate per game for team of the last five games to the
	 * value line.
	 * 
	 * @param tID
	 *            Id of the team.
	 */
	private void createFeature3(String tID) {
		String query = Querys.GOALS_OF_LAST_FIVE_GAMES.replace("TEAM", tID);
		int goals = 0;
		try {
			ResultSet set = con.searchInDB(query);
			while (set.next()) {
				if (set.getInt("Heim") == Integer.valueOf(tID)) {
					goals += set.getInt("Tore_Heim");
				} else {
					goals += set.getInt("Tore_Gast");
				}
			}
			set.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double result = goals / 5.0;
		valueLine.append(result);
		valueLine.append(", ");
	}

	/**
	 * Adds the goals of the last three games of the enemy team to the value
	 * line.
	 * 
	 * @param eID
	 *            Id of enemy team.
	 */
	private void createFeature2(String eID) {
		String query = Querys.GOALS_OF_LAST_THREE_GAMES.replace("TEAM", eID);
		try {
			ResultSet set = con.searchInDB(query);
			while (set.next()) {
				int goals;

				if (set.getInt("Heim") == Integer.valueOf(eID)) {
					goals = set.getInt("Tore_Heim");
				} else {
					goals = set.getInt("Tore_Gast");
				}
				valueLine.append(goals);
				valueLine.append(", ");

			}
			set.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the goals of the last three games of the team to the value line.
	 * 
	 * @param tID
	 *            Id of the team.
	 */
	private void createFeature1(String tID) {
		String query = Querys.GOALS_OF_LAST_THREE_GAMES.replace("TEAM", tID);

		ResultSet set = con.searchInDB(query);

		try {
			while (set.next()) {
				int tore;

				if (set.getInt("Heim") == Integer.valueOf(tID)) {
					tore = set.getInt("Tore_Heim");
				} else {
					tore = set.getInt("Tore_Gast");
				}

				valueLine.append(tore);
				valueLine.append(", ");
			}
			set.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Appends team name and enemy name to the current value line.
	 * 
	 * @param tName
	 *            Name of the team.
	 * @param eName
	 *            Name of the enemy team.
	 */
	private void createNames(String tName, String eName) {
		valueLine.append("'");
		valueLine.append(tName);
		valueLine.append("', '");
		valueLine.append(eName);
		valueLine.append("', ");
	}

}
