package module_database;

/**
 *
 * @author sevabaskin
 */
public class DuplicateModuleException extends Exception {
	public DuplicateModuleException() {
        super();
    }
    public DuplicateModuleException(String message) {
        super(message);
    }
}
