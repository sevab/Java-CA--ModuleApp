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

    /**
     *
     * @param code
     * @param title
     * @param leaderName
     * @param leaderEmail
     * @throws InvalidModuleFormatException
     * @throws EmptyValueException
     */
    public static void validateModuleValues(String code, String title, String leaderName, String leaderEmail) throws InvalidModuleFormatException, EmptyValueException {
		validateCode(code);
		validateTitle(title);
		validateLeaderName(leaderName);
		validateEmail(leaderEmail);
	}

    /**
     *
     * @param moduleCode value to validate
     * @throws InvalidModuleFormatException
     * @throws EmptyValueException
     */
    public static void validateCode(String moduleCode) throws InvalidModuleFormatException, EmptyValueException {
	    validadeEmptyness(moduleCode, emptyModuleCodeErrorMessage);
	    validatePattern(moduleCode, "[A-Z]{3}(1|2|3|M)\\d{3}", invalidModuleCodeFormatErrorMessage);
	}

    /**
     *
     * @param email value to validate
     * @throws InvalidModuleFormatException
     * @throws EmptyValueException
     */
    public static void validateEmail(String email) throws InvalidModuleFormatException, EmptyValueException  {
	    validadeEmptyness(email, emptyModuleLeaderEmailErrorMessage);
	    validatePattern(email, ".+\\@(exeter.ac.uk|ex.ac.uk)", invalidEmailFormatErrorMessage);
	}

    /**
     *
     * @param moduleTitle value to validate
     * @throws EmptyValueException
     */
    public static void validateTitle(String moduleTitle) throws EmptyValueException {
    	validadeEmptyness(moduleTitle, emptyModuleTitleErrorMessage);
    }

    /**
     *
     * @param leaderName value to validate
     * @throws EmptyValueException
     */
    public static void validateLeaderName(String leaderName) throws EmptyValueException {
		validadeEmptyness(leaderName, emptyModuleLeaderNameErrorMessage);
    }
    private static void validadeEmptyness(String str, String message) throws EmptyValueException {
    	if (str.equals("")) {
    		throw new EmptyValueException(message);
    	}
    }
    private static void validatePattern(String userInput, String pattern, String errorMessage)  throws InvalidModuleFormatException, EmptyValueException {
	    Pattern regex = Pattern.compile(pattern);
	    Matcher match = regex.matcher(userInput);
	    if (!match.matches())
	        throw new InvalidModuleFormatException(errorMessage);
	}
}
