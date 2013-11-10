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
    public void testColumnData() throws FileNotFoundException, IOException {
        String[] expectedModuleCodes = {"ECM1401", "ECM1402", "ECM1406"};
        String[] expectedModuleTitles = {"Programming", "Computer Systems", "Data Structures and Team Project"};
        String[] expectedModuleLeaders = {"Jonathan Fieldsend", "Zena Wood", "Zena Wood"};
        String[] expectedModuleLeadersEmails = {"J.E.Fieldsend@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk"};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleCodes[i], test.getDatabase()[i][0]);
        }
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleTitles[i], test.getDatabase()[i][1]);
        }
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleLeaders[i], test.getDatabase()[i][2]);
        }
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleLeadersEmails[i], test.getDatabase()[i][3]);
        }
    }

    @Test
    public void testSearchByModuleCode() throws FileNotFoundException, IOException {
        String[] testQueries = {"ECM1401", "ECM1406", "ECM1407", "ECM3412", "NSCM002", "Non-existent module"};
        int[] expectedSearchResults = { 0, 2, 3, 27, 59, -1};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");

        int i = 0;
        for (String moduleCodeQuery : testQueries) {
            int actualSearchResult = test.findModuleRowByCode(moduleCodeQuery);
            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            Assert.assertEquals(expectedSearchResults[i], actualSearchResult);
            i++;
        }
    }

    @Test
    public void testSearchByModuleYear() throws FileNotFoundException, IOException {
        String[] testQueries = {"1", "2", "3", "M"};
        int[][] expectedSearchResults = { {0,1,2}, {10,11,12}, {21,22,23}, {33,34,35}};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleYearQuery = testQueries[i];
            int[] expectedSearchResult = expectedSearchResults[i];
            int[] actualSearchResult = test.findModuleRowsByYear(moduleYearQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<3; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j]);  
            }
        }
    }

    @Test
    public void testSearchByModuleLeaderName() throws FileNotFoundException, IOException {
        String[] testQueries = {"Antony Galton", "Antony Galt", "Antony", "Ant"};
        int[][] expectedSearchResults = {{19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderNameQuery = testQueries[i];
            int[] expectedSearchResult = expectedSearchResults[i];
            int[] actualSearchResult = test.findModuleRowsByLeaderName(moduleLeaderNameQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j]);  
            }
        }
    }
}