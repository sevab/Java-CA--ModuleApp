package module_database;

/**
 *
 * @author sevabaskin
 */
public class FileIntegrityException extends Exception {

    public FileIntegrityException() {
        super();
    }

    /**
     *
     * @param message
     */
    public FileIntegrityException(String message) {
        super(message);
    }
}
