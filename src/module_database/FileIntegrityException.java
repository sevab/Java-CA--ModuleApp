/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

/**
 *
 * @author sevabaskin
 */
public class FileIntegrityException extends Exception {
	public FileIntegrityException() {
        super();
    }
    public FileIntegrityException(String message) {
        super(message);
    }
}
