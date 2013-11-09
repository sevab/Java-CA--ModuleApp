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
    int elementsInDatabase;    
    Pattern csvRegex;


    ModuleApp() {
    	this.database = new String[100][4];
    	this.elementsInDatabase = 0;
    	this.csvRegex = Pattern.compile("\"(.*?)\"");
    	
    }
    
    
    // maybe no need to throw IOException
    // normalize all queries by upcasing; normalize results as well?
    public void loadCSVFile(String fileDirectory) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
        
        String line;
		while ((line = reader.readLine()) != null) {
			Matcher csvMatcher = csvRegex.matcher(line);
			for (int j=0; j<4;j++) {
				csvMatcher.find();
			   	database[elementsInDatabase][j] = csvMatcher.group(1);
			   	// System.out.println(database[elementsInDatabase][j]);
			}
			elementsInDatabase++;
		}
    }


    int findModuleRowByCode(String moduleCodeQuery) {
    	int resultRow = -1; // will be returned if nothing's found
    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		if (this.database[i][0].equals(moduleCodeQuery)) {
    			resultRow = i;
    			break;
    		}
    	}
    	return resultRow;
    }

    int[] findModuleRowsByYear(String moduleYearQuery) {
    	// Finish testing
    	return int[] {100,101,102};
    }
    
    
    public String[][] getDatabase() {
        return database;
    }
}
