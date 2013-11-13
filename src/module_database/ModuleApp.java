/** TODO: currently the use of other methods requires calling loadCSV file; so, either
*         a) throw an error message if loadCSVFile() isn't called first or 
*         b) combine ModuleApp() with loadCSVFile()
*
*   TODO: when doing threading, warn user if he tries to quit the app while a tread hasn't finished writing to a file
*   TODO: decide on uniform variable names (e.g. newModuleTitle vs newTitle)
*   TODO: shall we return Module[] arrays instead of integer arrays? also, how about renaming findModuleRowByTitle to findByTitle, ModuleApp can then be renamed into ModulesDatabase
*/

package module_database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sevabaskin
 */
class ModuleApp {
    // make private?:
    // String[][] database;
    private Pattern csvRegex;
	private Pattern moduleYearRegex;
    private File databaseFile;
    private Module[] db;

    ModuleApp() {
    	// Move to the specific methods if not used in multiple places:
    	this.csvRegex = Pattern.compile("\"(.*?)\"");
		this.moduleYearRegex = Pattern.compile("(?<=^...)(1|2|3|M|m)");
    }

    // TODO: normalize all queries by upcasing; normalize results as well?
    // TODO: Expand database if reached the limit (keep on adding until an exception is thrown?)
    void loadCSVFile(String databaseFileDirectory) throws FileNotFoundException, IOException {
        this.db = new Module[ModuleAppHelper.linesInAFile(databaseFileDirectory)];
        this.databaseFile = new File(databaseFileDirectory);
        BufferedReader reader = new BufferedReader(new FileReader(databaseFile));
        String line;
        int i = 0;
		while ((line = reader.readLine()) != null) {
			// #OPTIMIZE
            Matcher csvMatcher = csvRegex.matcher(line);
            csvMatcher.find();
            String newCode = csvMatcher.group(1);
            csvMatcher.find();
            String newTitle = csvMatcher.group(1);
            csvMatcher.find();
            String newLeaderName = csvMatcher.group(1);
            csvMatcher.find();
            String newLeaderEmail = csvMatcher.group(1);

            this.db[i] = new Module(newCode, newTitle, newLeaderName, newLeaderEmail);
			i++;
		}
    }


    int findModuleRowByCode(String moduleCodeQuery) {
    	int resultRow = -1;
    	for (int i=0; i < this.db.length ; i++) {
    		if (getModule(i).getCode().equals(moduleCodeQuery)) {
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
    	for (int i=0; i < this.db.length ; i++) {
    		Matcher moduleYearMatcher = moduleYearRegex.matcher(getModule(i).getCode());
    		moduleYearMatcher.find();
		   	String candidateResult = moduleYearMatcher.group();
    		if (candidateResult.equals(moduleYearQuery))
    			resultRows = resultRows + i + ",";
    	}
    	return ModuleAppHelper.convertStringToIntArray(resultRows);
    }

    int[] findModuleRowsByLeaderName(String moduleLeaderNameQuery) {
    	// if (moduleLeaderNameQuery.length() == 0) return new int[]{-1};

    	String resultRows = ""; // if nothing's found, assign an empty array.
    	Pattern moduleLeaderNameRegex = Pattern.compile(moduleLeaderNameQuery);
    	for (int i=0; i < this.db.length ; i++) {
    		Matcher moduleLeaderNameMatcher = moduleLeaderNameRegex.matcher(getModule(i).getLeaderName());
    		if (moduleLeaderNameMatcher.lookingAt())
    			resultRows = resultRows + i + ",";
    	}
    	return ModuleAppHelper.convertStringToIntArray(resultRows);
    }

    int[] findModuleRowsByLeaderEmail(String moduleLeaderEmailQuery) {
    	// if (moduleLeaderEmailQuery.length() == 0) return new int[]{-1};

    	String resultRows = ""; // if nothing's found, assign an empty array.
    	Pattern moduleLeaderEmailRegex = Pattern.compile(moduleLeaderEmailQuery);
    	for (int i=0; i < this.db.length ; i++) {
    		Matcher moduleLeaderEmailMatcher = moduleLeaderEmailRegex.matcher(getModule(i).getLeaderEmail());
    		if (moduleLeaderEmailMatcher.lookingAt())
    			resultRows = resultRows + i + ",";
    	}
    	return ModuleAppHelper.convertStringToIntArray(resultRows);
    }

    void updateModule(int moduleRow, String newModuleCode, String newModuleTitle, String newModuleLeaderName, String newModuleLeaderEmail) {
        // TODO: Validate first (format + if empty)
        // TODO: update, reload database
        // TODO: extract into to private methods updateDatabaseArray & updateDatabaseCSV, then call both in here after validating values


        File tempDatabaseFile = new File("temp_" + this.databaseFile.getName());


        // update the database array
        // FIXME: shall we perform this after updating CSV file in case of errors? But errors should be caught.
        // Extract into Validator.isNotEmpty(String str)
        if (!newModuleCode.equals(""))
            getModule(moduleRow).setCode(newModuleCode);
        if (!newModuleTitle.equals(""))
            getModule(moduleRow).setTitle(newModuleTitle);
        if (!newModuleLeaderName.equals(""))
            getModule(moduleRow).setLeaderName(newModuleLeaderName);
        if (!newModuleLeaderEmail.equals(""))
            getModule(moduleRow).setLeaderEmail(newModuleLeaderEmail);

        // update the CSV file
        // TODO: Move to thread
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(this.databaseFile));
            bw = new BufferedWriter(new FileWriter(tempDatabaseFile));
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                if (i == moduleRow) { // TODO: Extract into a separate method:
                    line = "\""+newModuleCode+"\",\""+ newModuleTitle +"\",\""+ newModuleLeaderName +"\",\""+newModuleLeaderEmail+"\"";
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

        ModuleAppHelper.replaceFile(this.databaseFile, tempDatabaseFile);
    }






    void deleteModule(int moduleRow) {
        // delete from the database Array first
        Module[] newDB = new Module[this.db.length-1];
        int j = 0;
        for (int i=0; i< newDB.length; i++) {
            if (i == moduleRow) j++;
            newDB[i] = this.db[j]; 
            j++;
        }
        this.db = newDB;

       // delete from CSV
        File tempDatabaseFile = new File("temp_" + this.databaseFile.getName());
       // TODO: Move to thread
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(this.databaseFile));
            bw = new BufferedWriter(new FileWriter(tempDatabaseFile));
            
            String line;
            int i = -1;
            while ((line = br.readLine()) != null) {
                i++;
                if (i == moduleRow) continue;
                bw.write(line+"\n");
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

        ModuleAppHelper.replaceFile(this.databaseFile, tempDatabaseFile);
    }


    void createModule(String newModuleCode, String newModuleTitle, String newModuleLeaderName, String newModuleLeaderEmail) throws DuplicateModuleException {
        verifyNotDuplicate(newModuleCode);
        // TODO: Validate (format + nonEmptyness) & Check for duplicates
        Module[] newDB = new Module[this.db.length+1];         // add to databaseArray:
        for (int i=0; i<this.db.length; i++) {                 // Copy old database into the new
            newDB[i] = this.db[i];
        }
        newDB[newDB.length-1] = new Module(newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail);         // Add new module to the new database
        this.db = newDB;         // reasign new database to the global database

        String line = "\""+newModuleCode+"\",\""+ newModuleTitle +"\",\""+newModuleLeaderName+"\",\""+newModuleLeaderEmail+"\"";
        ModuleAppHelper.appendLineToFile(this.databaseFile, line); // append line to the databaseCSVfile
    }


    void verifyNotDuplicate(String moduleCode) throws DuplicateModuleException {
        for (Module module : this.db) {
            if (module.getCode().equals(moduleCode)){
                throw new DuplicateModuleException();
            }
        }
    }


    // Getters
    // private boolean notDuplicate(String moduleCode) {}
    Module[] getDb() { return this.db; }
    Module getModule(int moduleRow) { return this.db[moduleRow]; }
 
    

}
