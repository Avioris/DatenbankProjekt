package fu.tjk.databaseAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used for communication with the database.
 */
public class DBCon {

	private final String HOST = "localhost";
	private final String PORT = "3306";
	private final String DBNAME = "dumpbundesliga";
	private final String DBUSER = "root";
	private final String DBPW = "dbs2014";

	private Connection connect;
	private Statement statement;

	/**
	 * Connection to database. Call this method before you execute query.
	 */
	public void createConncetion() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + HOST + ":"
					+ PORT + "/" + DBNAME + "?" + "user=" + DBUSER + "&"
					+ "password=" + DBPW);
			statement = connect.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method executes a sqlquery on the database. Throws an exception if
	 * the query is wrong.
	 * 
	 * @param sqlquery
	 *            The query to execute.
	 * @return A ResultSet with the data. Can be null.
	 */
	public ResultSet searchInDB(String sqlquery) {
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sqlquery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Closes the connection to the database. Call this method after you
	 * finished.
	 */
	public void closeConnection() {
		try {
			statement.close();
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
