package module_database;

/**
 *
 * @author sevabaskin
 */
public class NonexistentModuleException extends Exception {

    public NonexistentModuleException() {
        super();
    }

    /**
     *
     * @param message
     */
    public NonexistentModuleException(String message) {
        super(message);
    }
}
