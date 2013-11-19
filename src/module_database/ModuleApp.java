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

	public static void main(String[] args) throws IOException, InvalidQueryFormatException, NonexistentModuleException, EmptyValueException   {
		System.out.println("##################################### ");
		System.out.println("Welcome to the Modules Database app!\n");
		addFileProcedure();
		selectMainOption();
    }

    static void printMainOptions() {
    	System.out.println("Select one of the following options:");
    	System.out.println( "====================================");
    	System.out.println("(1) Add new module to a database");
    	System.out.println("(2) Search and modify existing modules");
    	System.out.println("(x) Exit Modules App");
    }
    static void selectMainOption() throws IOException, InvalidQueryFormatException, NonexistentModuleException, EmptyValueException  {
		printMainOptions();
		try {
			System.out.print("Enter an option: ");
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "1": newModuleProcedure(); break;
	    		case "2": selectSearchOption(); break;
	    		case "x": System.exit(0);
	    		case "X": System.exit(0);
	    		default : System.out.println("\nSorry, that's not an available option.\n");
	    				  selectMainOption();
	    	}
		} catch (IOException e) {
			quitWithAnError();
		}

    }



    // ########################      CRUD      ####################################

    static void printCRUDoptions() {
		System.out.println("Select one of the following options:");
    	System.out.println("====================================");
    	System.out.println("(1) Delete a module");
    	System.out.println("(2) Update a module");
    	System.out.println("(3) Return to search menu");
    	System.out.println("(4) Return to main menu");
    }

    static void selectCRUDoption() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException {
    	printCRUDoptions();
    	try {
			System.out.print("Enter an option: ");
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "1"	 	: deleteModule(); break;
	    		case "2"		: updateModuleProcedure(); break;
	    		case "3"		: selectSearchOption(); break;
	    		case "4"		: selectMainOption(); break;
	    		default			: System.out.println("\nSorry, that's not an available option.\n");
	    						  selectCRUDoption();
	    	}
		} catch (IOException e) {
			quitWithAnError();
		}
    }

// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv

    static void deleteModule() throws InvalidQueryFormatException, EmptyValueException {
    	try {
			System.out.print("Delete module with the code: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	modulesDB.deleteModule(userInput);
	    	// if found
			System.out.println("\nThe module " + userInput + " has been successfuly removed from the database\n");
			selectMainOption();
		} catch (NonexistentModuleException e) {
			System.out.println("\n(!) " + e.getMessage() + "\n");
			deleteModule();
		} catch (IOException e) {
			quitWithAnError();
		}
    }

    static void updateModuleProcedure() throws IOException, InvalidQueryFormatException, NonexistentModuleException, EmptyValueException {
    	try {
			System.out.print("Enter code of the module to update: ");
		    String moduleToUpdate = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	ModuleValidator.validateCode(moduleToUpdate);
	    	modulesDB.getModuleRow(moduleToUpdate); // just verify exists
	  		modifyModuleProcedure("update", moduleToUpdate);
		} catch (IOException e) {
			quitWithAnError();
		} catch (InvalidModuleFormatException | EmptyValueException | NonexistentModuleException e) {
			System.out.println("\n(!) " + e.getMessage() + "\n");
			updateModuleProcedure();
		}
    }
    static void newModuleProcedure() throws IOException, InvalidQueryFormatException, NonexistentModuleException, EmptyValueException  {
    	modifyModuleProcedure("create", null);
    }
    static void modifyModuleProcedure(String method, String moduleToUpdate) throws IOException, InvalidQueryFormatException, NonexistentModuleException, EmptyValueException  {
		String newModuleCode, newModuleTitle, newModuleLeaderName, newModuleLeaderEmail;
		newModuleCode = newModuleTitle = newModuleLeaderName = newModuleLeaderEmail = null;
		String[] moduleArgs = new String[4];

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
					
					// only validate user inputs when it is create or is non-blank
					// i.e. when updating a module, allow empty inputs to skip updating a specific field:
					if (!moduleArgs[i].equals("") || method.equals("create")) {
						switch (i) {
							case 0: ModuleValidator.validateCode(moduleArgs[i]); break;
							case 1: ModuleValidator.validateTitle(moduleArgs[i]); break;
							case 2: ModuleValidator.validateLeaderName(moduleArgs[i]); break;
							case 3: ModuleValidator.validateEmail(moduleArgs[i]); break;
						}	
					}
					

				} catch (InvalidModuleFormatException | EmptyValueException e) {
					moduleArgs[i] = null;
					System.out.println("\n(!) " + e.getMessage() + "\n");
				}
			}
		}
    	try {
    		switch(method) {
    			case "create" : modulesDB.createModule(moduleArgs[0], moduleArgs[1], moduleArgs[2], moduleArgs[3]);
    				    		System.out.println("\nSuccess! Module " + moduleArgs[0] + " has been added.\n");
								break;
    			case "update" : modulesDB.updateModule(moduleToUpdate, moduleArgs[0], moduleArgs[1], moduleArgs[2], moduleArgs[3]);
				    			System.out.println("\nSuccess! Module has been updated.\n");
								break;
    		}
    	} catch (InvalidModuleFormatException | EmptyValueException | DuplicateModuleException e) {
			System.out.println("\n(!) " + e.getMessage() + "\n");
				// No action following fail, since fails should not happen (all validations have been made)
    	}
		selectMainOption();
    }

		// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv






    // ########################      SEARCH      ####################################

    static void printSearchOptions() {
    	System.out.println("Select one of the following search options:");
    	System.out.println("===========================================");
    	System.out.println("(0) Show all records in the database");
    	System.out.println("(1) Find Module by Module Code");
    	System.out.println("(2) Find Module by Year Level");
    	System.out.println("(3) Find Module by Module Leader Name");
    	System.out.println("(4) Find Module by Module Leader Email");
    	System.out.println("(5) Return to main menu");
    }
    static void selectSearchOption() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException  {
    	printSearchOptions();
    	try {
			System.out.print("Enter an option: ");		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	switch (userInput) {
	    		case "0": printOutModules(modulesDB.getDb()); selectSearchOption(); break;
	    		case "1": searchByCode(); break;
	    		case "2": searchByYear(); break;
	    		case "3": searchByName(); break;
	    		case "4": searchByEmail(); break;
	    		case "5": selectMainOption(); break;
	    		case "x": System.exit(0);
	    		case "X": System.exit(0);
	    		default : System.out.println("\nSorry, that's not an available option.\n");
	    				  selectSearchOption(); //TODO: extract into a helper to use by other methods as well
	    	}
		} catch (IOException e) {
			quitWithAnError();
		}
    }
    static void searchByCode() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException 
	    { search("code", "Enter the exact module code: "); }
    static void searchByYear() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException 
	    { search("year", "Enter modules' year level: "); }
    static void searchByName() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException 
	    { search("name", "Enter modules' leader name: "); }
    static void searchByEmail() throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException 
	    { search("email", "Enter modules' leader email: "); }
    private static void search(String method, String promptMessage) throws InvalidQueryFormatException, NonexistentModuleException, EmptyValueException {
		try {
			System.out.print(promptMessage);		
		    String userInput = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	    	Module[] searchResults = null;
	    	try {
				switch(method) {
					case "code"  : searchResults = modulesDB.findByCode(userInput); break;
					case "year"  : searchResults = modulesDB.findByYear(userInput); break;
					case "name"  : searchResults = modulesDB.findByLeader("name",  userInput); break;
					case "email" : searchResults = modulesDB.findByLeader("email", userInput); break;
					default      : break;
				}
			} catch (InvalidQueryFormatException | InvalidModuleFormatException | EmptyValueException e) {
				System.out.println("\n(!) " + e.getMessage() + "\n");
				selectSearchOption();
			}
			if (searchResults.length == 0) {
				printNoResults();
				selectSearchOption();
			} else {
				printOutModules(searchResults);
				selectCRUDoption();
			}
			
		} catch (IOException e) {
			quitWithAnError();
		}
    }


    static void printOutModules(Module[] modulesArray) {
    	System.out.println( "================== Results ==================");
    	System.out.println("Module Code   Module Title        Leader Name     Leader Email");
    	for (int i=0; i<modulesArray.length; i++) {
    		System.out.println(modulesArray[i].toString("       "));
    	}
    	System.out.println("\n");
    	System.out.println( "================== A total of " + modulesArray.length +" records found ==================");
    	System.out.println("\n\n\n");
    }
    static void printNoResults() {
    	System.out.println("\n");
		System.out.println( "================== Your search did not match anything ==================");
    	System.out.println("\n");
    }




    // ########################      FILE      ####################################

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
			} catch (IOException e) {
				quitWithAnError();
			}
   		}
		// File received
		System.out.println("We're loading the file in...");
		modulesDB = new ModulesDatabase();
		try {
			modulesDB.loadCSVFile(fileName);
			System.out.println("Success! The database has been loaded.\n");
		} catch(FileIntegrityException e){
			System.out.println("\n" + e.getMessage() + "\n");
			addFileProcedure();
		} catch (Exception e){
			System.out.println("\n" + e.getMessage() + "\n");
			quitWithAnError();
		}
    }

    static void quitWithAnError() {
    	System.out.println("Oops..somethign went wrong.");
		System.exit(1);
    }



	// 			/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/Coursework 1/test/module_database/test_modules.csv




}
// TODO: before quiting wait till all threads finish executing
