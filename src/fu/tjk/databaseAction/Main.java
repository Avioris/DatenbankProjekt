package fu.tjk.databaseAction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

	private DBCon con = new DBCon();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	private WekaFile wekaFile = new WekaFile("/home/tj/DBS2014/");

	public void go() {
		connect();
		initWekaFile();
		getTeamInfos();
		createFeatures();
		createWekaFile();
		close();

	}

	private void createWekaFile() {
		wekaFile.createFile();
	}

	private void close() {
		con.closeConnection();
	}

	private void connect() {
		con.createConncetion();
	}

	private void initWekaFile() {
		// Set Relation
		wekaFile.addRealtion("Features");

		// Name of team and enemy
		wekaFile.addAttribute("Team", names.toArray(new String[names.size()]));
		wekaFile.addAttribute("Gegner", names.toArray(new String[names.size()]));

		// Goals for team of last 3 games against enemy
		wekaFile.addAttribute("Team-Tore-Letztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Vorletztes-Spiel", WekaFile.NUMERIC);
		wekaFile.addAttribute("Team-Tore-Vorvorletztes-Spiel", WekaFile.NUMERIC);

		// Goals for enemy of last 3 games against team
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
		
		// all teams
		for (int i = 0; i < names.size()-1; i++) {
			// versus every other team thats not calculated already
			for (int j = i+1; j < names.size(); j++) {
				String valueLine = names.get(i)+", "+names.get(j);
				
				
				
				wekaFile.addValueLine(valueLine);
			}
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		m.go();
	}
}
