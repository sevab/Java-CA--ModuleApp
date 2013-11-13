/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

/**
 *
 * @author sevabaskin
 */
public class EmptyValueException extends Exception {
	public EmptyValueException() {
        super();
    }
    public EmptyValueException(String message) {
        super(message);
    }
}
