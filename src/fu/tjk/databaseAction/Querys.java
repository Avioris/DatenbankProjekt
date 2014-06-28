package fu.tjk.databaseAction;

/**
 * This class saves useful query.
 */
public class Querys {

	public static String GOALS_OF_LAST_THREE_GAMES = 
			"SELECT Heim, Tore_Heim, Gast, Tore_Gast "
			+ "FROM Spiel "
			+ "WHERE Heim='TEAM' OR Gast='TEAM' "
			+ "ORDER BY Spiel.Datum DESC LIMIT 3";
	
	public static String GOALS_OF_LAST_FIVE_GAMES = 
			"SELECT Heim, Tore_Heim, Gast, Tore_Gast "
			+ "FROM Spiel "
			+ "WHERE Heim='TEAM' OR Gast='TEAM' "
			+ "ORDER BY Spiel.Datum DESC LIMIT 5";
	
	public static String PASS_AND_GOALS_OF_TEAM =
			"SELECT Vorlagen, Tore "+
			"FROM Spieler "+
			"WHERE Vereins_ID='TEAM'";
	
	public static String TEAMS_IDS = 
			"SELECT Name, V_ID "
			+ "FROM Verein ";
	
	public static String GOALS_OF_LAST_THREE_HOME_GAMES=
			"SELECT Tore_Heim "
			+ "FROM Spiel "
			+ "WHERE Heim='TEAM' "
			+ "ORDER BY Spiel.Datum DESC "
			+ "LIMIT 3";
	
	public static String NAME = 
			"SELECT V_ID "
			+ "FROM Verein"
			+ "WHERE Name = 'TEAM'";
	
	

}
