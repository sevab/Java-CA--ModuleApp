/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sevabaskin
 */
public class ModuleValidator {

	private static String emptyModuleCodeErrorMessage = "You forgot to enter module's code, please enter one.";
	private static String emptyModuleTitleErrorMessage = "You forgot to enter module's title, please enter one.";
	private static String emptyModuleLeaderNameErrorMessage = "You forgot to enter module's leader name, please enter one.";
	private static String emptyModuleLeaderEmailErrorMessage = "You forgot to enter module's leader email, please enter one.";
	private static String invalidModuleCodeFormatErrorMessage = "You've entered invalid module code. Please enter a module code in the format ABC1234, ABC2234, ABC3234 or ABCM123.";
	private static String invalidEmailFormatErrorMessage = "You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk.";


	public static void validateModuleValues(String code, String title, String leaderName, String leaderEmail) throws InvalidModuleFormatException, EmptyValueException {
		validateCode(code);
		validateTitle(title);
		validateLeaderName(leaderName);
		validateEmail(leaderEmail);
	}
    public static void validateCode(String moduleCode) throws InvalidModuleFormatException, EmptyValueException  {
    	validadeEmptyness(moduleCode, emptyModuleCodeErrorMessage);
    	Pattern moduleCodeRegex = Pattern.compile("[A-Z]{3}(1|2|3|M)\\d{3}");
    	Matcher moduleCodeMatch = moduleCodeRegex.matcher(moduleCode);

    	if (!moduleCodeMatch.matches()) { // rename to value-specific exception?
    		throw new InvalidModuleFormatException(invalidModuleCodeFormatErrorMessage);
    	}
    }
    public static void validateEmail(String email) throws InvalidModuleFormatException, EmptyValueException  {
    	validadeEmptyness(email, emptyModuleLeaderEmailErrorMessage);
    	Pattern emailRegex = Pattern.compile(".+\\@(exeter.ac.uk|ex.ac.uk)");
    	Matcher emailMatch = emailRegex.matcher(email);

    	if (!emailMatch.matches()) {
    		throw new InvalidModuleFormatException(invalidEmailFormatErrorMessage);
    	}
    }
    public static void validateTitle(String moduleTitle) throws EmptyValueException {
    	validadeEmptyness(moduleTitle, emptyModuleTitleErrorMessage);
    }
    public static void validateLeaderName(String leaderName) throws EmptyValueException {
		validadeEmptyness(leaderName, emptyModuleLeaderNameErrorMessage);
    }
    private static void validadeEmptyness(String str, String message) throws EmptyValueException {
    	if (str.equals("")) {
    		throw new EmptyValueException(message);
    	}
    }
}
