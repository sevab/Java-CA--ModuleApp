/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sevabaskin
 */
public class ModuleApp {
    String[][] database;
    int lengthOfDatabase;    
    Pattern csvRegex;


    ModuleApp() {
    	this.database = new String[100][4];
    	this.lengthOfDatabase = 0;
    	this.csvRegex = Pattern.compile("\"(.*?)\"");
    	
    }
    
    
    // maybe no need to throw IOException
    public void loadCSVFile(String fileDirectory) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
        
        String line;
		while ((line = reader.readLine()) != null) {
			Matcher csvMatcher = csvRegex.matcher(line);
			for (int j=0; j<4;j++) {
				csvMatcher.find();
			   	database[lengthOfDatabase][j] = csvMatcher.group(1);
			   	System.out.println(database[lengthOfDatabase][j]);
			}
			lengthOfDatabase++;
		}


    }
    
    
    public String[][] getDatabase() {
        return database;
    }
}
