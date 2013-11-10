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
	Pattern moduleYearRegex;

    ModuleApp() {
    	this.database = new String[100][4];
    	this.elementsInDatabase = 0;

    	// Move to the specific methods if not used in multiple places:
    	this.csvRegex = Pattern.compile("\"(.*?)\"");
		this.moduleYearRegex = Pattern.compile("(?<=^...)(1|2|3|M|m)");
    }
    
    // Accessor methods
    public String[][] getDatabase() { return database; }
    

    // maybe no need to throw IOException
    // normalize all queries by upcasing; normalize results as well?
    // Expand database if reached the limit (keep on adding until an exception is thrown?)
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

    // Describe how strings are used to generte dynamyc-length arrays
    // or redo by increasing array length every time (see profiler to see which one's faster); go w/ elgnt solution
    int[] findModuleRowsByYear(String moduleYearQuery) {
    	String resultRows = ""; // if nothing's found, assign an empty array.
    	

    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		Matcher moduleYearMatcher = moduleYearRegex.matcher(database[i][0]);
    		moduleYearMatcher.find();
		   	String candidateResult = moduleYearMatcher.group();

    		if (candidateResult.equals(moduleYearQuery))
    			resultRows = resultRows + i + ",";
    	}
		// System.out.println(resultRows);
    	return convertStringToIntArray(resultRows);
    }

    int[] findModuleRowsByLeaderName(String moduleLeaderNameQuery) {
    	String resultRows = ""; // if nothing's found, assign an empty array.
    	Pattern moduleLeaderNameRegex = Pattern.compile(moduleLeaderNameQuery);

    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		Matcher moduleLeaderNameMatcher = moduleLeaderNameRegex.matcher(database[i][2]);

    		if (moduleLeaderNameMatcher.lookingAt())
    			resultRows = resultRows + i + ",";
    	}
		// System.out.println(resultRows);

    	return convertStringToIntArray(resultRows);
    }





    // Helpers
    int[] convertStringToIntArray(String str) {
		String[] strArray = str.split(",");
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++)
			intArray[i] = Integer.parseInt(strArray[i]);		
		return intArray;
    }
    

}
