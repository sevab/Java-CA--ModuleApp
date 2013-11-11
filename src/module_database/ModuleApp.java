/** TODO: currently the use of other methods requires calling loadCSV file; so, either
*         a) throw an error message if loadCSVFile() isn't called first or 
*         b) combine ModuleApp() with loadCSVFile()
*/

package module_database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
    File databaseFile;

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
    void loadCSVFile(String databaseFileDirectory) throws FileNotFoundException, IOException {
        this.databaseFile = new File(databaseFileDirectory);
        BufferedReader reader = new BufferedReader(new FileReader(databaseFile));
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



    String[] getModuleInfo(int moduleRow) {
    	return new String[]{database[moduleRow][0], database[moduleRow][1], database[moduleRow][2], database[moduleRow][3]};
    }
    String getCsvLine(String fileDirectory, int lineNumber) throws FileNotFoundException, IOException {
        // FIXME: make sure line's not empty
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if (i == lineNumber) {
                break;
            }
            i++;
        }
        return line;
    }





    // String[] getModuleInfoAsACsvLine(int moduleRow) {
    //     return new String;
    //     String = "\""+database[moduleRow][0]+"\",\""+database[moduleRow][1]+"\",\""+database[moduleRow][2]+"\",\""+database[moduleRow][3]+"\"";
    // }

    void updateModule(int moduleRow, String newModuleCode, String newModuleTitle, String newModuleLeader, String newModuleLeaderEmail) {
        // TODO: Validate first (format + if empty)
        // TODO: update, reload database
        // TODO: extract into to private methods updateDatabaseArray & updateDatabaseCSV, then call both in here after validating values

        String databaseFileName = this.databaseFile.getName();
        String tempDatabaseFileName = "temp_" + databaseFileName;
        // System.out.println("original file name: " + databaseFileName + " temp: " + tempDatabaseFileName);


        // update the database array
        // FIXME: shall we perform this after updating CSV file in case of errors? But errors should be caught.
        database[moduleRow][0] = newModuleCode;
        database[moduleRow][1] = newModuleTitle;
        database[moduleRow][2] = newModuleLeader;
        database[moduleRow][3] = newModuleLeaderEmail;

        // update the CSV file
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(this.databaseFile));
            bw = new BufferedWriter(new FileWriter(tempDatabaseFileName));
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                if (i == moduleRow) { // TODO: Extract into a separate method:
                    // System.out.println("before: " + line);
                    line = "\""+newModuleCode+"\",\""+ newModuleTitle +"\",\""+ newModuleLeader +"\",\""+newModuleLeaderEmail+"\"";
                    // System.out.println("after: " + line);
                }
                bw.write(line+"\n");
                i++;
            }
        } catch (Exception e) {
            return;
        } finally {
            try {
                if(br != null)
                   br.close();
            } catch (IOException e) {}

            try {
                if(bw != null)
                   bw.close();
            } catch (IOException e) {}
        }


        // TODO: delete() returns true, so continue only if deleted successfuly
        // Delete old database file
        if (this.databaseFile.delete()) {
            // Reasign with the new database file saved as a tempFile
            File newFile = new File(tempDatabaseFileName);
            // Rename new database file to the original name
            newFile.renameTo(this.databaseFile);
            this.databaseFile = newFile;    
        }
    }

    // To be used by the test suite to restore the database back to its original state after modification
    public static void restoreDatabaseFileFromBackUp(String backupFilePath, String destinationFilePath) throws IOException {
        File backupFile = new File(backupFilePath);
        File destinationFile = new File(destinationFilePath);
        // TODO: might want to do something else
        if(!destinationFile.exists()) {
            destinationFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(backupFile).getChannel();
            destination = new FileOutputStream(destinationFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
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
