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
    String test_csv_file;
    String backup_csv_file;
    public ModuleAppTest() {
        this.test_csv_file = "test/module_database/test_modules.csv";
        this.backup_csv_file = "test/module_database/backup_modules.csv";
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
//        String actual = ModuleApp.loadCSVFile(this.test_csv_file);
//        Assert.assertEquals(expected, actual);
//    }
    
    @Test
    public void testColumnData() throws FileNotFoundException, IOException {
        String[] expectedModuleCodes = {"ECM1401", "ECM1402", "ECM1406"};
        String[] expectedModuleTitles = {"Programming", "Computer Systems", "Data Structures and Team Project"};
        String[] expectedModuleLeaders = {"Jonathan Fieldsend", "Zena Wood", "Zena Wood"};
        String[] expectedModuleLeadersEmails = {"J.E.Fieldsend@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk"};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);
        for (int i = 0; i < 3 ; i++) {
            Assert.assertEquals(expectedModuleCodes[i], test.getModule(i).getCode());
            Assert.assertEquals(expectedModuleTitles[i], test.getModule(i).getTitle());
            Assert.assertEquals(expectedModuleLeaders[i], test.getModule(i).getLeaderName());
            Assert.assertEquals(expectedModuleLeadersEmails[i], test.getModule(i).getLeaderEmail());
        }
    }

    @Test
    public void testSearchByModuleCode() throws FileNotFoundException, IOException {
        String[] testQueries = {"ECM1401", "ECM1406", "ECM1407", "ECM3412", "NSCM002", "Non-existent module"};
        int[] expectedSearchResults = { 0, 2, 3, 27, 59, -1};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

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
        test.loadCSVFile(this.test_csv_file);

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
        test.loadCSVFile(this.test_csv_file);

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

    @Test
    public void testSearchByModuleLeaderEmail() throws FileNotFoundException, IOException {
        String[] testQueries = {"A.P.Galton@ex.ac.uk", "A.P.Galton@ex.ac.", "A.P.Ga", "A.P"};
        int[][] expectedSearchResults = {{19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderEmailQuery = testQueries[i];
            int[] expectedSearchResult = expectedSearchResults[i];
            int[] actualSearchResult = test.findModuleRowsByLeaderEmail(moduleLeaderEmailQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j]);  
            }
        }
    }
    
    @Test
    public void testGettingFullModuleInfoAsString() throws FileNotFoundException, IOException {
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);
        String[] expected = {"ECM1401", "Programming", "Jonathan Fieldsend", "J.E.Fieldsend@exeter.ac.uk"};
        String[] actual = test.getModuleInfo(0);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCsvLine() throws FileNotFoundException, IOException {
        ModuleApp test = new ModuleApp();
        String expected = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actual = test.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expected, actual);
    }

    // Next: Validate, Verify Duplicates
    @Test
    public void testModuleUpdate() throws FileNotFoundException, IOException {
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Update:
        String expectedLine = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actualLine = test.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArray = {"ECM1402","Computer Systems","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArray = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArray, actualArray);

        test.updateModule(1, "ECM9999", "Test Name", "Test Name", "test@email.co.uk");
        
        // After Update:
        String[] expectedArrayTwo = {"ECM9999", "Test Name", "Test Name", "test@email.co.uk"};
        String[] actualArrayTwo = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        expectedLine = "\"ECM9999\",\"Test Name\",\"Test Name\",\"test@email.co.uk\"";
        actualLine = test.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        // Restore database file back to the original, pre-test state
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }

    @Test
    public void testModuleDelete() throws FileNotFoundException, IOException {
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Update:
        String expectedLine = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actualLine = test.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArray = {"ECM1402","Computer Systems","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArray = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArray, actualArray);

        test.deleteModule(1);

        // After Update:
        expectedLine = "\"ECM1406\",\"Data Structures and Team Project\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        actualLine = test.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArrayTwo = {"ECM1406","Data Structures and Team Project","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArrayTwo = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        

        // Restore database file back to the original, pre-test state
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }


   @Test
    public void testModuleCreate() throws FileNotFoundException, IOException {
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Create:
        String expectedLine = null;
        String actualLine = test.getCsvLine(this.test_csv_file, 60);
        Assert.assertEquals(expectedLine, actualLine);
        try {
            String[] expectedArray = null;
            String[] actualArray = test.getModule(60).getFullInfo();
            Assert.assertArrayEquals(expectedArray, actualArray);
            fail( "Missing exception" );
        } catch (ArrayIndexOutOfBoundsException e) {}

        test.createModule("ECM9999", "Test Name", "Test Name", "test@email.co.uk");

        // After Create:
        String[] expectedArrayTwo = {"ECM9999", "Test Name", "Test Name", "test@email.co.uk"};
        String[] actualArrayTwo = test.getModule(60).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        expectedLine = "\"ECM9999\",\"Test Name\",\"Test Name\",\"test@email.co.uk\"";
        actualLine = test.getCsvLine(this.test_csv_file, 60);
        Assert.assertEquals(expectedLine, actualLine);
        
        

        // // Restore database file back to the original, pre-test state
        ModuleApp.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }




    // @Test
    // public void testCannotAddDuplicates() throws FileNotFoundException, IOException {}


    // @Test
    // public void testSearchByModuleLeaderEmailCornerCases() throws FileNotFoundException, IOException {
    //     ModuleApp test = new ModuleApp();
    //     test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");
    //         int[] expectedSearchResult = {-1};
    //         int[] actualSearchResult = test.findModuleRowsByLeaderEmail("");
    //         Assert.assertArrayEquals(expectedSearchResult, actualSearchResult);  
    // }

    // TODO: Test corener cases (e.g. if nothing found, exceptions, etc). Maybe should be handled by UI

    
}