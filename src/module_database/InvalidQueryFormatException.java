package module_database;

/**
 *
 * @author sevabaskin
 */
public class InvalidQueryFormatException extends Exception {
	public InvalidQueryFormatException() {
        super();
    }
    public InvalidQueryFormatException(String message) {
        super(message);
    }
}
