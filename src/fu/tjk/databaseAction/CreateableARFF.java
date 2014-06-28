package fu.tjk.databaseAction;

/**
 * Interface to define methods for the WekaFile-Class.
 */
public interface CreateableARFF {

	/**
	 * Add the relation to the file. There can only be one relation at a time.
	 * This will also be the name of the file.
	 * 
	 * @param rel
	 *            The relation to add. No points!
	 */
	void addRealtion(String rel);

	/**
	 * Add an attribute to the file.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param datatype
	 *            The data type of the attribute.
	 */
	void addAttribute(String name, String[] datatype);

	/**
	 * Add a value line to the file.
	 * 
	 * @param s
	 *            The value line.
	 */
	void addValueLine(String s);

	/**
	 * Create the physical file with relation, all attributes and value lines.
	 * Only successful if file doesn't exist.
	 */
	void createFile();

}
