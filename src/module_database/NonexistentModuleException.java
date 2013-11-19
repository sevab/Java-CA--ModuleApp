package module_database;

/**
 *
 * @author sevabaskin
 */
public class NonexistentModuleException extends Exception {
	public NonexistentModuleException() {
        super();
    }
    public NonexistentModuleException(String message) {
        super(message);
    }
}
