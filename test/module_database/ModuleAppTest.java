/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author sevabaskin
 */
public class ModuleAppTest {
    
    public ModuleAppTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
//    @Ignore @Test
//    public void testFirstLine() throws FileNotFoundException, IOException {
//        String expected = "\"ECM1401\",\"Programming\",\"Jonathan Fieldsend\",\"J.E.Fieldsend@exeter.ac.uk\"";
//        String actual = ModuleApp.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");
//        Assert.assertEquals(expected, actual);
//    }
    
    @Test
    public void testModuleCodeColumn() throws FileNotFoundException, IOException {
        String[] expectedModuleCodes = {"ECM1401", "ECM1402", "ECM1406"};
        ModuleApp test = new ModuleApp();
        test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleCodes[i], test.getDatabase()[i][0]);
        }
        
    }
}