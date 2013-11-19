package module_database;

/**
 *
 * @author sevabaskin
 */
public class InvalidModuleFormatException extends Exception {


    public InvalidModuleFormatException() {
        super();
    }

    /**
     *
     * @param message
     */
    public InvalidModuleFormatException(String message) {
        super(message);
    }
}