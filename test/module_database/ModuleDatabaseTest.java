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
public class ModuleDatabaseTest {
    String test_csv_file;
    String backup_csv_file;
    public ModuleDatabaseTest() {
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
//        String actual = ModulesDatabase.loadCSVFile(this.test_csv_file);
//        Assert.assertEquals(expected, actual);
//    }
    
    @Test
    public void testColumnData() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] expectedModuleCodes = {"ECM1401", "ECM1402", "ECM1406"};
        String[] expectedModuleTitles = {"Programming", "Computer Systems", "Data Structures and Team Project"};
        String[] expectedModuleLeaders = {"Jonathan Fieldsend", "Zena Wood", "Zena Wood"};
        String[] expectedModuleLeadersEmails = {"J.E.Fieldsend@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk", "Z.M.Wood@exeter.ac.uk"};

        ModulesDatabase test = new ModulesDatabase();
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
        String[] testQueries = {"ECM1401", "ECM1406", "ECM1407", "ECM3412", "NSCM002"};
        String[] expectedSearchResults = { "ECM1401", "ECM1406", "ECM1407", "ECM3412", "NSCM002"};

        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        for (int i=0; i < testQueries.length; i++) {
            
            Module[] actualSearchResult = test.findModuleRowByCode(testQueries[i]);
            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            Assert.assertEquals(expectedSearchResults[i], actualSearchResult[0].getCode());
        }

        // An empty array is returned when no results matched
        Assert.assertEquals(new Module[]{}, test.findModuleRowByCode("Non-existent module"));
    }

    @Test
    public void testSearchByModuleYear() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] testQueries = {"1", "2", "3", "M"};
        String[][] expectedSearchResults = { {"ECM1401","ECM1402","ECM1406"}, {"ECM2407","ECM2408","ECM2410"}, {"ECM3401","ECM3402","ECM3403"}, {"ECMM412","ECMM401","ECMM403"}};

        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {

            String moduleYearQuery = testQueries[i];
            String[] expectedSearchResult = expectedSearchResults[i];
            Module[] actualSearchResult = test.findModuleRowsByYear(moduleYearQuery);
            // System.out.println(test.findModuleRowsByYear("2")[1].getCode());

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<3; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j].getCode());  
            }
        }
    }

    @Test
    public void testSearchByModuleLeaderName() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] testQueries = {"Antony Galton", "Antony Galt", "Antony", "Ant"};
        String[][] expectedSearchResults = {{"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}};

        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderNameQuery = testQueries[i];
            String[] expectedSearchResult = expectedSearchResults[i];
            Module[] actualSearchResult = test.findModuleRowsByLeader("name", moduleLeaderNameQuery);
            

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j].getCode());  
            }
        }
    }

    @Test
    public void testSearchByModuleLeaderEmail() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException {
        String[] testQueries = {"A.P.Galton@ex.ac.uk", "A.P.Galton@ex.ac.", "A.P.Ga", "A.P"};
        String[][] expectedSearchResults = {{"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}, {"ECM2417","ECM3401","ECM3410","ECM3416","ECM3421"}};

        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        // Loop over test queries
        for (int i=0; i<4; i++) {
            String moduleLeaderEmailQuery = testQueries[i];
            String[] expectedSearchResult = expectedSearchResults[i];
            Module[] actualSearchResult = test.findModuleRowsByLeader("email", moduleLeaderEmailQuery);

            // System.out.println(actualSearchResult + " " + moduleCodeQuery + " " + expectedSearchResults[i]);
            for (int j=0; j<5; j++) {
                Assert.assertEquals(expectedSearchResult[j], actualSearchResult[j].getCode());  
            }
        }
    }

    @Test
    public void testGetCsvLine() throws FileNotFoundException, IOException {
        ModulesDatabase test = new ModulesDatabase();
        String expected = "\"ECM1402\",\"Computer Systems\",\"Zena Wood\",\"Z.M.Wood@exeter.ac.uk\"";
        String actual = ModuleAppHelper.getCsvLine(this.test_csv_file, 1);
        Assert.assertEquals(expected, actual);
    }

    // Next: Validate, Verify Duplicates
    @Test
    public void testModuleUpdate() throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException, DuplicateModuleException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModulesDatabase test = new ModulesDatabase();
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
    public void testModuleDelete()
    throws FileNotFoundException, IOException, InvalidModuleFormatException, EmptyValueException, InterruptedException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModulesDatabase test = new ModulesDatabase();
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
    public void testModuleCreate()
    throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException {
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        ModulesDatabase test = new ModulesDatabase();
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
    public void shouldThrowADuplicateExceptionWhenAddingADuplicate()
    throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException {

        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        int dbLengthBefore = test.getDb().length;
        try {
            // try adding a duplicate
            test.createModule("ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (DuplicateModuleException ex) {
             Assert.assertTrue(ex.getMessage().equals("A module with the same module code already exists, you cannot add a duplicate."));
        }

        int dbLengthAfter = test.getDb().length;

        Assert.assertEquals(dbLengthAfter, dbLengthAfter);      // assert database length before and after the attempt to add a new module stays the same
        String lastModuleCode = test.getModule(test.getDb().length-1).getCode();
        Assert.assertEquals("NSCM002", lastModuleCode);

        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
        // Verify the DB that it has not been added
    }

    @Test
    public void shouldNotUpdateModuleWithDuplicateValues()
    throws FileNotFoundException, IOException, DuplicateModuleException, InvalidModuleFormatException, EmptyValueException, DuplicateModuleException {
        ModulesDatabase test = new ModulesDatabase();
        test.loadCSVFile(this.test_csv_file);

        try {
            // it is allowed to update itself with the same values
            test.updateModule(1, "ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            // however, updating one module's code with that of another module's code should throw a DuplicateModuleException
            test.updateModule(2, "ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (DuplicateModuleException e) {
             Assert.assertTrue(e.getMessage().equals("A module with the same module code already exists, you cannot add a duplicate."));
        }
        ModuleAppHelper.restoreDatabaseFileFromBackUp(this.backup_csv_file, this.test_csv_file);
    }

    // !!! Test deleteing, updating and creating in one test to make sure the threading is synchronized and methods don't get into each other's way
    // may need to do the wait & notify thing to make sure one thread starts reading file before the other one stoped writing. Though shouldn't happen since synchronized

    // @Test
    // public void testSearchByModuleLeaderEmailCornerCases() throws FileNotFoundException, IOException {
    //     ModulesDatabase test = new ModulesDatabase();
    //     test.loadCSVFile("/Users/sevabaskin/Dropbox/2nd Year/Java/CW1/modules.csv");
    //         int[] expectedSearchResult = {-1};
    //         int[] actualSearchResult = test.findModuleRowsByLeaderEmail("");
    //         Assert.assertArrayEquals(expectedSearchResult, actualSearchResult);  
    // }

    // TODO: Test corener cases (e.g. if nothing found, exceptions, etc). Maybe should be handled by UI

    
}