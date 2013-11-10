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
class ModuleApp {
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
    String[][] getDatabase() { return database; }
    

    // TODO: normalize all queries by upcasing; normalize results as well?
    // TODO: Expand database if reached the limit (keep on adding until an exception is thrown?)
    void loadCSVFile(String fileDirectory) throws FileNotFoundException, IOException {
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
    	int resultRow = -1;
    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		if (this.database[i][0].equals(moduleCodeQuery)) {
    			resultRow = i;
    			break;
    		}
    	}
    	return resultRow;
    }

    // JavaDoc: Describe how strings are used to generte dynamyc-length arrays
    
    int[] findModuleRowsByYear(String moduleYearQuery) {
    	// if (moduleYearQuery.length() == 0) return new int[]{-1};

    	String resultRows = ""; // if nothing's found, assign an empty array.	
    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		Matcher moduleYearMatcher = moduleYearRegex.matcher(database[i][0]);
    		moduleYearMatcher.find();
		   	String candidateResult = moduleYearMatcher.group();
    		if (candidateResult.equals(moduleYearQuery))
    			resultRows = resultRows + i + ",";
    	}
    	return convertStringToIntArray(resultRows);
    }

    int[] findModuleRowsByLeaderName(String moduleLeaderNameQuery) {
    	// if (moduleLeaderNameQuery.length() == 0) return new int[]{-1};

    	String resultRows = ""; // if nothing's found, assign an empty array.
    	Pattern moduleLeaderNameRegex = Pattern.compile(moduleLeaderNameQuery);
    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		Matcher moduleLeaderNameMatcher = moduleLeaderNameRegex.matcher(database[i][2]);
    		if (moduleLeaderNameMatcher.lookingAt())
    			resultRows = resultRows + i + ",";
    	}
    	return convertStringToIntArray(resultRows);
    }

    int[] findModuleRowsByLeaderEmail(String moduleLeaderEmailQuery) {
    	// if (moduleLeaderEmailQuery.length() == 0) return new int[]{-1};

    	String resultRows = ""; // if nothing's found, assign an empty array.
    	Pattern moduleLeaderEmailRegex = Pattern.compile(moduleLeaderEmailQuery);
    	for (int i=0; i < this.elementsInDatabase ; i++) {
    		Matcher moduleLeaderEmailMatcher = moduleLeaderEmailRegex.matcher(database[i][3]);
    		if (moduleLeaderEmailMatcher.lookingAt())
    			resultRows = resultRows + i + ",";
    	}
    	return convertStringToIntArray(resultRows);
    }


    // DON'T FORGET TO CLOSE FILE



    String getModuleInfo(int moduleRowNumber) {
    	String result = database[moduleRowNumber][0] + " " + database[moduleRowNumber][1] + " " + database[moduleRowNumber][2] + " " + database[moduleRowNumber][3];
    	return result;
    }

    // Helpers
    private int[] convertStringToIntArray(String str) {
		String[] strArray = str.split(",");
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++)
			intArray[i] = Integer.parseInt(strArray[i]);		
		return intArray;
    }
    

}
