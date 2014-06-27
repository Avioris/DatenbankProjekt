package fu.tjk.databaseAction;

public class Main {
	
	private DBCon con = new DBCon();
	
	public void go() {
		
		con.closeConnection();
		
		createFeatures();
		
		
		
		con.closeConnection();
		
	}

	private void createFeatures() {
		
	}


}
