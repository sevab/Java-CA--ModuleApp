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
class ModulesDatabase {
    private File databaseFile;
    private Module[] db;

    ModulesDatabase() {    }

    // JavaDoc: loads CSV file from its file path on the filesystem
    // e.g. on Mac OS: /Users/BillGates/Documents/modules_database.csv

    synchronized void loadCSVFile(String databaseFileDirectory)
        throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException, FileIntegrityException {
        // Create an array as large as there're numbers in a csv file:
        this.db = new Module[ModuleAppHelper.linesInAFile(databaseFileDirectory)];
        this.databaseFile = new File(databaseFileDirectory);
        BufferedReader reader = new BufferedReader(new FileReader(databaseFile));
        String line;
        int i = 0;
        Pattern csvRegex = Pattern.compile("\"(.*?)\"");
        while ((line = reader.readLine()) != null) {
            Matcher csvMatcher = csvRegex.matcher(line);
            csvMatcher.find();
            String newCode = csvMatcher.group(1).toUpperCase();
            csvMatcher.find();
            String newTitle = csvMatcher.group(1);
            csvMatcher.find();
            String newLeaderName = csvMatcher.group(1);
            csvMatcher.find();
            String newLeaderEmail = csvMatcher.group(1).toLowerCase();
            try {
                this.db[i] = new Module(newCode, newTitle, newLeaderName, newLeaderEmail);
            } catch (InvalidModuleFormatException | EmptyValueException e) {
                throw new FileIntegrityException(
                    "Error while reading the CSV file. A module record on line " + i + " has the following integrity error:" +
                    "\n(!) " + e.getMessage() + "\n"
                    );
            }
            
            i++;
        }
    }

    Module[] findByCode(String query) throws InvalidModuleFormatException, EmptyValueException {
        ModuleValidator.validateCode(query);
        Module[] resultRow = {};
        for (int i=0; i < this.db.length ; i++) {
            if (getModuleByRow(i).getCode().equals(query)) {
                resultRow = new Module[]{getModuleByRow(i)};
                break;
            }
        }
        return resultRow;
    }



    // JavaDoc: Describe how strings are used to generte dynamyc-length arrays
    Module[] findByYear(String query) throws InvalidQueryFormatException {
        if (query.length() == 0) return new Module[]{};
        ModuleAppHelper.validateModuleYearQuery(query);
        Pattern moduleYearRegex = Pattern.compile("(?<=^...)(1|2|3|M)");
        String resultRows = ""; // if nothing's found, assign an empty array.
        for (int i=0; i < this.db.length ; i++) {
            Matcher moduleYearMatcher = moduleYearRegex.matcher(getModuleByRow(i).getCode());
            moduleYearMatcher.find();
            String candidateResult = moduleYearMatcher.group();
            if (candidateResult.equals(query.toUpperCase()))
                resultRows = resultRows + i + ",";
        }
        switch (resultRows) {
            case "" : return new Module[]{};
            default : return getModulesByRow(ModuleAppHelper.convertStringToIntArray(resultRows));
        }
    }

    // JavaDoc returns empty array if nothing's found or the query's empty
    Module[] findByLeader(String method, String query) {
        if (query.length() == 0) return new Module[]{};
        // if method is smth else, throw some sort of exception? or turn methods into enums
        String resultRows = "";
        Pattern pattern = null;
        switch(method) {
            case "name"  : pattern = Pattern.compile(query); break;
            case "email" : pattern = Pattern.compile(query.toLowerCase()); break; // downcase email queries since all emails are stored in lowercase
        }
        Matcher moduleLeaderMatcher = null;
        for (int i=0; i < this.db.length ; i++) {
            switch(method) {
                case "name"  : moduleLeaderMatcher = pattern.matcher(getModuleByRow(i).getLeaderName()); break;
                case "email" : moduleLeaderMatcher = pattern.matcher(getModuleByRow(i).getLeaderEmail()); break;
            }
            if (moduleLeaderMatcher.lookingAt())
                resultRows = resultRows + i + ",";
        }
        switch (resultRows) {
            case "" : return new Module[]{};
            default : return getModulesByRow(ModuleAppHelper.convertStringToIntArray(resultRows));
        }
    }





    synchronized void updateModule(String moduleCode, String newModuleCode, String newModuleTitle, String newModuleLeaderName, String newModuleLeaderEmail)
        throws InvalidModuleFormatException, EmptyValueException, DuplicateModuleException, NonexistentModuleException {
        // Update this.db array array:
        // get module row and verify exists
        final int moduleRow = getModuleRow(moduleCode);
        File tempDatabaseFile = new File("temp_" + this.databaseFile.getName());


        Module moduleToUpdate = getModuleByRow(moduleRow);
        // If not empty & not the same as existing value:
        if (!newModuleCode.equals("") && !newModuleCode.equals(moduleToUpdate.getCode())){
            verifyNotDuplicate(newModuleCode);
            moduleToUpdate.setCode(newModuleCode);
        }
        if (!newModuleTitle.equals(""))
            moduleToUpdate.setTitle(newModuleTitle);
        if (!newModuleLeaderName.equals(""))
            moduleToUpdate.setLeaderName(newModuleLeaderName);
        if (!newModuleLeaderEmail.equals(""))
            moduleToUpdate.setLeaderEmail(newModuleLeaderEmail);

        // Update the CSV file:
        final String substituteLine = "\""+ moduleToUpdate.getCode() +
                                      "\",\"" + moduleToUpdate.getTitle()  +
                                      "\",\"" + moduleToUpdate.getLeaderName()  +
                                      "\",\"" + moduleToUpdate.getLeaderEmail() +"\"";

        new Thread() {
            public void run() {
                ModuleAppHelper.modifyLineInAFile(databaseFile, moduleRow, "update", substituteLine);
                // TODO: Catch InterruptedException ? "File-writing has been inturrupted. The database csv file may be corrupt."
            }
        }.start();
    }

    synchronized void deleteModule(String moduleCode) throws NonexistentModuleException{

        // get module row and verify exists
        final int moduleRow = getModuleRow(moduleCode);
        Module[] newDB = new Module[this.db.length-1];
        int j = 0;
        for (int i=0; i< newDB.length; i++) {
            if (i == moduleRow) j++;
            newDB[i] = this.db[j]; 
            j++;
        }
        this.db = newDB;

       // Delete from CSV:
        new Thread() {
            public void run() {
                ModuleAppHelper.modifyLineInAFile(databaseFile, moduleRow, "delete", null);
            }
        }.start();

    }

    synchronized void createModule(String newModuleCode, String newModuleTitle, String newModuleLeaderName, String newModuleLeaderEmail) throws DuplicateModuleException, InvalidModuleFormatException, EmptyValueException {
        verifyNotDuplicate(newModuleCode);
        // Expand database:
        Module[] newDB = new Module[this.db.length+1];
        for (int i=0; i<this.db.length; i++) {
            newDB[i] = this.db[i];
        }
        newDB[newDB.length-1] = new Module(newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail);         // Add new module to the new database
        this.db = newDB;

        final String line = "\""+newModuleCode+"\",\""+ newModuleTitle +"\",\""+newModuleLeaderName+"\",\""+newModuleLeaderEmail+"\"";
        
        new Thread() {
            public void run() {
                ModuleAppHelper.appendLineToFile(databaseFile, line); // append line to the databaseCSVfile
            }
        }.start();
    }


    // Getters
    Module[] getDb() {
        return this.db;
    }
    // get Module by row is a better name
    Module getModuleByRow(int moduleRow) {
        return this.db[moduleRow];
    }
    Module[] getModulesByRow(int[] modulesIDs) {
        Module[] modulesArray = new Module[modulesIDs.length];
        for (int i=0; i<modulesIDs.length; i++) {
            modulesArray[i] = getModuleByRow(modulesIDs[i]);
        }
        return modulesArray;
    }
     // returns row of the module in the db array. throws NonexistentModuleException module exception if nothing's found
    int getModuleRow(String moduleCode) throws NonexistentModuleException {
        int result = -1;
        for (int i=0; i< this.db.length; i++) {
            if (this.db[i].getCode().equals(moduleCode)) {
                result = i;
                break;
            }
        }
        if (result == -1)
            throw new NonexistentModuleException("The module you have entered does not exist. Try again.");
        return result;
    }
    // Helpers
    void verifyNotDuplicate(String moduleCode) throws DuplicateModuleException {
        for (Module module : this.db) {
            if (module.getCode().equals(moduleCode)){
                throw new DuplicateModuleException("A module with the same module code already exists, you cannot add a duplicate.");
            }
        }
    }

}
/** TODO: currently the use of other methods requires calling loadCSV file; so, either
*         a) throw an error message when querying the db without a registered CSV file
*         b) combine ModulesDatabase() with loadCSVFile()
*
*   TODO: when doing threading, warn user if he tries to quit the app while a tread hasn't finished writing to a file
*/
