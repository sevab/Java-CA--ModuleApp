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
    public void testColumnData() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
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
    public void testSearchByModuleCode() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
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
    public void testSearchByModuleYear() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
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
    public void testSearchByModuleLeaderName() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] testQueries = {"Antony Galton", "Antony Galt", "Antony", "Ant"};
        int[][] expectedSearchResults = {{19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderNameQuery = testQueries[i];
            int[] expectedSearchResult = expectedSearchResults[i];
            int[] actualSearchResult = test.findModuleRowsByLeader("name", moduleLeaderNameQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j]);  
            }
        }
    }

    @Test
    public void testSearchByModuleLeaderEmail() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] testQueries = {"A.P.Galton@ex.ac.uk", "A.P.Galton@ex.ac.", "A.P.Ga", "A.P"};
        int[][] expectedSearchResults = {{19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}, {19,21,25,28,31}};

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderEmailQuery = testQueries[i];
            int[] expectedSearchResult = expectedSearchResults[i];
            int[] actualSearchResult = test.findModuleRowsByLeader("email", moduleLeaderEmailQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j]);  
            }
        }
    }

    @Test
    public void testGetCsvLine() throws FileNotFoundException, IOException {
        ModuleApp test = new ModuleApp();
        String expected = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actual = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expected, actual);
    }

    // Next: Validate, Verify Duplicates
    @Test
    public void testModuleUpdate() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException, DuplicateModuleException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Update:
        String expectedLine = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArray = {"ECM1402","Computer Systems","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArray = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArray, actualArray);

        test.updateModule(1, "ECMM999", "Test Name", "Test Name", "test@exeter.ac.uk");
        
        try { // wait 0.1sec before checking that the line in the file has been updated
            Thread.sleep(100);
        } catch( InterruptedException e) {
            fail("Exception should not be thrown");
        }
        // After Update:
        String[] expectedArrayTwo = {"ECMM999", "Test Name", "Test Name", "test@exeter.ac.uk"};
        String[] actualArrayTwo = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        expectedLine = "\"ECMM999\",\"Test Name\",\"Test Name\",\"test@exeter.ac.uk\"";
        actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        // Restore database file back to the original, pre-test state
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }

    @Test
    public void testModuleDelete() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException, InterruptedException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Update:
        String expectedLine = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArray = {"ECM1402","Computer Systems","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArray = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArray, actualArray);
        
        test.deleteModule(1);        
        try { // wait 0.1sec before checking that the line in the file has been deleted
            Thread.sleep(100);
        } catch( InterruptedException e) {
            fail("Exception should not be thrown");
        }
        // After Update:
        expectedLine = "\"ECM1406\",\"Data Structures and Team Project\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expectedLine, actualLine);
        String[] expectedArrayTwo = {"ECM1406","Data Structures and Team Project","Zena Wood","Z.M.Wood@exeter.ac.uk"};
        String[] actualArrayTwo = test.getModule(1).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        

        // Restore database file back to the original, pre-test state
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }


   @Test
    public void testModuleCreate() throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        // Before Create:
        String expectedLine = null;
        String actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 60);
        Assert.assertEquals(expectedLine, actualLine);
        try {
            String[] expectedArray = null;
            String[] actualArray = test.getModule(60).getFullInfo();
            Assert.assertArrayEquals(expectedArray, actualArray);
            fail( "Missing exception" );
        } catch (ArrayIndexOutOfBoundsException e) {}

        test.createModule("ECM3999", "Test Name", "Test Name", "test@ex.ac.uk");
        try { // wait 1sec before checking that the line has been added to the file
            Thread.sleep(100);
        } catch( InterruptedException e) {
            fail("Exception should not be thrown");
        }
        // After Create:
        String[] expectedArrayTwo = {"ECM3999", "Test Name", "Test Name", "test@ex.ac.uk"};
        String[] actualArrayTwo = test.getModule(60).getFullInfo();
        Assert.assertArrayEquals(expectedArrayTwo, actualArrayTwo);
        expectedLine = "\"ECM3999\",\"Test Name\",\"Test Name\",\"test@ex.ac.uk\"";
        actualLine = ModuleAppHelper.getCsvLine(this.test_csv_file, 60);
        Assert.assertEquals(expectedLine, actualLine);
        
        

        // // Restore database file back to the original, pre-test state
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }
    // (expected=UnknownUserException.class)
    @Test
    public void shouldThrowADuplicateExceptionWhenAddingADuplicate() throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException {

        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);

        int dbLengthBefore = test.getDb().length;
        try {
            // try adding a duplicate
            test.createModule("ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (DuplicateModuleException e) {}

        int dbLengthAfter = test.getDb().length;

        Assert.assertEquals(dbLengthAfter, dbLengthAfter);      // assert database length before and after the attempt to add a new module stays the same
        String lastModuleCode = test.getModule(test.getDb().length-1).getCode();
        Assert.assertEquals("NSCM002", lastModuleCode);

        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        // Verify the DB that it has not been added
    }

    @Test
    public void shouldNotUpdateModuleWithDuplicateValues() throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException, DuplicateModuleException {
        ModuleApp test = new ModuleApp();
        test.loadCSVFile(this.test_csv_file);


        try {
            // it is allowed to update itself with the same values
            test.updateModule(1, "ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            // however, updating one module's code with that of another module's code should throw a DuplicateModuleException
            test.updateModule(2, "ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (DuplicateModuleException e) {}
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }

    // !!! Test deleteing, updating and creating in one test to make sure the threading is synchronized and methods don't get into each other's way
    // may need to do the wait & notify thing to make sure one thread starts reading file before the other one stoped writing. Though shouldn't happen since synchronized

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