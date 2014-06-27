package fu.tjk.databaseAction;

public class Querys {

	public static String GOALS_OF_LAST_THREE_GAMES = 
			"SELECT Heim, Tore_Heim, Gast, Tore_Gast "
			+ "FROM Spiel "
			+ "WHERE Heim='TEAM' OR Gast='TEAM' "
			+ "ORDER BY Spiel.Datum DESC LIMIT 3";
	
	public static String TEAMS = 
			"SELECT DISTINCT Name "
			+ "FROM Verein ";
	
	public static String NAME = 
			"SELECT V_ID "
			+ "FROM Verein"
			+ "WHERE Name = 'TEAM'";
	
	

}
