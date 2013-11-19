package module_database;

/**
 *
 * @author sevabaskin
 */
public class EmptyValueException extends Exception {

    public EmptyValueException() {
        super();
    }

    /**
     *
     * @param message
     */
    public EmptyValueException(String message) {
        super(message);
    }
}
