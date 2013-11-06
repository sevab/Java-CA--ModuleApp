/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author sevabaskin
 */
public class ModuleApp {
    String[] moduleCodes;
    
    ModuleApp() {
    	
    }
    
    
    // maybe no need to throw IOException
    public static String loadCSVFile(String fileDirectory) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
        return reader.readLine();
        
//        String[] lines = new String[100];
//        String line = null;
//        for (int j = 0; j < 100 && (line = reader.readLine()) != null; j++) {
//            lines[j] = line;
//        }    


       for (int i = 0; i < 100 && (line = reader.readLine()) != null; i++) {
           lines[i] = line;
       }    


    }
    
    
    public String[] getModulesCodes() {
        return null;
    }
}
