package fu.tjk.databaseAction;

public interface Createable {
	
	void addRealtion(String rel);
	
	void addAttribute(String name, String[] datatype);
	
	void addValueLine(String s);
	
	void createFile();

}
