package fu.tjk.databaseAction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WekaFile implements Createable {
	
	public static final String[] NUMERIC = new String[]{"NUMERIC"}; 

	private String relation;
	private ArrayList<String> attributes = new ArrayList<String>();
	private ArrayList<String[]> datatypes = new ArrayList<String[]>();
	private ArrayList<String> values = new ArrayList<String>();
	private String path;

	public WekaFile(String path) {
		this.path = path;
	}

	public void addRealtion(String rel) {
		relation = rel;
	}

	public void addAttribute(String name, String[] datatype) {
		attributes.add(name);
		datatypes.add(datatype);
	}

	public void addValueLine(String v) {
		values.add(v);
	}

	public void createFile() {
		System.out.println("Creating " + relation + ".arff...");
		String nl = "\n", dnl = "\n\n";

		try {
			File file = new File(path + relation + ".arff");
			if (!file.createNewFile()) {
				System.err.println("File already exists!");
				return;
			}
			BufferedWriter br = new BufferedWriter(new FileWriter(file));

			br.write("@RELATION " + relation + nl);

			for (int i = 0; i < attributes.size(); i++) {
				br.write("@ATTRIBUTE " + attributes.get(i) + " ");
				if (datatypes.get(i).length == 1) {
					br.write(datatypes.get(i)[0] + nl);
				} else {
					br.write("{ '" + datatypes.get(i)[0]);
					for (int j = 1; j < datatypes.get(i).length; j++) {
						br.write("', '" + datatypes.get(i)[j]);
					}
					br.write("' }" + dnl);
				}
			}

			br.write("@DATA" + nl);
			for (int i = 0; i < values.size(); i++) {
				br.write(values.get(i) + nl);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Created " + relation + ".arff Successfull!");
	}

}
