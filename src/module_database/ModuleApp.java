// TODO: make sure a line is added to the file
// TODO: before quiting wait till all threads finish executing
// TODO: change default action to repeat itself rather than delete an application

package module_database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author sevabaskin
 */
public class ModuleApp {
	private static ModulesDatabase modulesDB;
// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv

	public static void main(String[] args) throws IOException, InvalidQueryFormatException  {
		System.out.println("##################################### ");
		System.out.println("Welcome to the Modules Database app!\n");
		addFileProcedure();
		// Select option
		selectMainOption();
    }

	// Default: print an error, reprint self
    static void selectMainOption() throws IOException, InvalidQueryFormatException {
		printMainOptions();
		try {
			System.out.print("Enter an option: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "1": addNewModuleProcedure(); break;
	    		case "2": selectSearchOption(); break;
	    		case "x": System.exit(0);
	    		case "X": System.exit(0);
	    		default: break;
	    	}
		} catch (IOException ioe) {
			System.out.println("Oops..somethign went wrong.");
			System.exit(1);
		}

    }
    // static void print(String str) {
    // 	System.out.println(str);
    // }

    // catch ModuleNotFound & blank exceptions
    static void updateModule() {
  //   	System.out.print("Update module with the code: ");		
	 //    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
  //   	modulesDB.updateModuleByModuleCode(userInput);
  //   	// if found
		// System.out.print("The module " + userInput + " has been successfuly removed from the database");
    }

    static void addNewModuleProcedure() throws IOException, InvalidQueryFormatException  {
		String newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail;
		newModuleCode = newModuleTitle = newModuleLeaderName = newModuleLeaderEmail = null;
		String[] moduleArgs = {newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail};

		String codeMessage = "Enter new module code: ";
		String titleMessage = "Enter new module title: ";
		String leaderNameMessage = "Enter new module leader's name: ";
		String leaderEmailMessage = "Enter new module leader's email: ";
		String[] promptMessages = {codeMessage, titleMessage, leaderNameMessage, leaderEmailMessage};
		
		for (byte i=0; i<4; i++) {
			while(moduleArgs[i] == null) {
				try {
					System.out.print(promptMessages[i]);
					moduleArgs[i] = (new BufferedReader(new InputStreamReader(System.in))).readLine();

					switch (i) {
						case 0: ModuleValidator.validateCode(moduleArgs[i]); break;
						case 1: ModuleValidator.validateTitle(moduleArgs[i]); break;
						case 2: ModuleValidator.validateLeaderName(moduleArgs[i]); break;
						case 3: ModuleValidator.validateEmail(moduleArgs[i]); break;
					}

				} catch (Exception e) {
					if (e instanceof InvalidModuleFormatException || e instanceof EmptyValueException) {
						moduleArgs[i] = null;
						System.out.println("\n(!) " + e.getMessage() + "\n");
					} else { throw new RuntimeException(e); }
				}
			}
		}
    	try {
    		modulesDB.createModule(newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail);
	    	System.out.println("Success! The module has been added.");
    	} catch  (Exception e) {
			if (e instanceof InvalidModuleFormatException || e instanceof EmptyValueException || e instanceof DuplicateModuleException) {
				System.out.println("\n(!) " + e.getMessage() + "\n");
				// No action following fail, since fails should not happen (all validations have been made)
			}
    	}
		// TODO: printModule? e.g. => added Module Code: "ECM1234" Module Title: "sdfsd"...
		selectMainOption();
    }

		// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv


    static void printSearchOptions() {
    	System.out.println("Select one of the following search options:");
    	System.out.println("===========================================");
    	System.out.println("(1) Find Module by Module Code");
    	System.out.println("(2) Find Module by Year Level");
    	System.out.println("(3) Find Module by Module Leader Name");
    	System.out.println("(4) Find Module by Module Leader Email");
    	System.out.println("(5) Return to main menu");
    }
    static void selectSearchOption()  throws InvalidQueryFormatException {
    	printSearchOptions();
    	try {
			System.out.print("Enter an option: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "1": searchByCode(); break;
	    		case "2": searchByYear(); break;
	    		case "3": searchByName(); break;
	    		case "4": searchByEmail(); break;
	    		case "5": selectMainOption(); break;
	    		case "x": System.exit(0);
	    		case "X": System.exit(0);
	    		default:  System.out.println("\nSorry, that's not an available option.\n");
	    				  selectSearchOption(); //TODO: extract into a helper to use by other methods as well
	    	}
	    	// selectCRUDoption();
		} catch (IOException ioe) {
			System.out.println("Oops..somethign went wrong.");
			System.exit(1);
		}
    }

    static void printCRUDoptions() {
		System.out.println("Select one of the following options:");
    	System.out.println("====================================");
    	System.out.println("(1) Delete a module");
    	System.out.println("(2) Update a module");
    	System.out.println("(3) Return to search menu");
    	System.out.println("(4) Return to main menu");
    }
    // TODO: don't quit if else is written, simply reprint itself
    static void selectCRUDoption() throws InvalidQueryFormatException {
    	printCRUDoptions();
    	try {
			System.out.print("Make an action: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "1"	 	: deleteModule(); break;
	    		case "2"		: updateModule(); break;
	    		case "3"		: selectSearchOption(); break;
	    		case "4"		: selectMainOption(); break;
	    		default			: System.out.println("\nSorry, that's not an available option.\n");
	    						  selectCRUDoption();
	    	}
		} catch (IOException ioe) {
			System.out.println("Oops..somethign went wrong.");
			System.exit(1);
		}
    }

    // catch ModuleNotFound & blank exceptions
    static void deleteModule() {
    	try {
			System.out.print("Delete module with the code: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	modulesDB.deleteModuleByModuleCode(userInput);
	    	// if found
			System.out.print("The module " + userInput + " has been successfuly removed from the database");
		} catch (IOException ioe) {
			System.out.println("Oops..somethign went wrong.");
			System.exit(1);
		}
    }


    // TODO: Print out nothing's found if nothing's found
    // FIXME: Fix empty search by leadername results bug
    static void searchByCode()  throws InvalidQueryFormatException {  search("code", "Enter the exact module code: "); }
    static void searchByYear()  throws InvalidQueryFormatException {  search("year", "Enter modules' year level: "); }
    static void searchByName()  throws InvalidQueryFormatException {  search("name", "Enter modules' leader name: "); }
    static void searchByEmail()  throws InvalidQueryFormatException { search("email", "Enter modules' leader email: "); }
    private static void search(String method, String promptMessage) throws InvalidQueryFormatException {
		try {
			System.out.print(promptMessage);		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	Module[] searchResults = null;
	    	System.out.print("user input: " + userInput);
	    	try {
				switch(method) {
					case "code"  : searchResults = modulesDB.findModuleRowByCode(userInput); break;
					case "year"  : searchResults = modulesDB.findModuleRowsByYear(userInput); break;
					case "name"  : searchResults = modulesDB.findModuleRowsByLeader("name",  userInput); break;
					case "email" : searchResults = modulesDB.findModuleRowsByLeader("email", userInput); break;
					default      : break;
				}
			} catch (InvalidQueryFormatException e) {
				System.out.println("\n(!) " + e.getMessage() + "\n");
				selectSearchOption();
			}
			if (searchResults.length == 0) {
				printNoResults();
			} else {
				printOutModules(searchResults);
			}
			selectSearchOption();
		} catch (IOException ioe) {
			System.out.println("Oops..somethign went wrong.");
			System.exit(1);
		}
    }


    static void printOutModules(Module[] modulesArray) {
    	System.out.println( "================== Results ==================");
    	System.out.println("Module Code   Module Title        Leader Name     Leader Email");
    	for (int i=0; i<modulesArray.length; i++) {
    		System.out.println(modulesArray[i].toString("       "));
    	}
    	System.out.println("\n\n\n");
    }
    static void printNoResults() {
    	System.out.println("\n");
		System.out.println( "================== Your search did not match anything ==================");
    	System.out.println("\n");
    }





    static void addFileProcedure() {
		// Getting file path
		String fileName = null;
		File f = new File("fake/path");
   		
   		while (!f.exists()) {	
			System.out.print("Please enter the path to the csv file containing modules data: ");
			try {
				fileName = (new BufferedReader(new InputStreamReader(System.in))).readLine();
				f = new File(fileName);
				if(!f.exists()) {
					System.out.println("It doesn't look like this file exists..");
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name :(");
					System.exit(1);
			}
   		}
		// File received
		System.out.println("We're loading the file in...");
		modulesDB = new ModulesDatabase();
		try {
			modulesDB.loadCSVFile(fileName);
			System.out.println("Success! The database has been loaded.\n");
		} catch (Exception e){
			System.out.println("Oops..somethign went wrong.");
		}
    }

	// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv

    static void printMainOptions() {
    	System.out.println("Select one of the following options:");
    	System.out.println( "====================================");
    	System.out.println("(1) Add new module to a database");
    	System.out.println("(2) Search and modify existing modules");
    	System.out.println("(x) Exit Modules App");
    }


}
