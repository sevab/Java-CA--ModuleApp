package module_database;

/**
 *
 * @author sevabaskin
 */
public class DuplicateModuleException extends Exception {

    public DuplicateModuleException() {
        super();
    }

    /**
     *
     * @param message
     */
    public DuplicateModuleException(String message) {
        super(message);
    }
}
