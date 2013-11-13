/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

/**
 *
 * @author sevabaskin
 */
class Module {
	String code;
	String title;
	String leaderName;
	String leaderEmail;

	
	Module(String code, String title, String leaderName, String leaderEmail) {
		this.code = code;
		this.title = title;
		this.leaderName = leaderName;
		this.leaderEmail = leaderEmail;
	}

	// Getters:
	String getCode() { return this.code; }
	String getTitle() { return this.title; }
	String getLeaderName() { return this.leaderName; }
	String getLeaderEmail() { return this.leaderEmail; }
	
	// Setters
	void setCode(String newCode) { this.code = newCode; }
	void setTitle(String newTitle) { this.title = newTitle; }
	void setLeaderName(String newLeaderName) { this.leaderName = newLeaderName; }
	void setLeaderEmail(String newLeaderEmail) { this.leaderEmail = newLeaderEmail; }

	// Helpers
	// rename toStringArray
	String[] getFullInfo() {
    	return new String[]{ this.code, this.title, this.leaderName, this.leaderEmail };
    }
    // String toString() {}

}
