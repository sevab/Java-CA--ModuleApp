/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author sevabaskin
 */
public class ModuleTest {
    Module testModule;

    String moduleCode;
    String moduleTitle;
    String moduleLeaderName;
    String moduleLeaderEmail;

    public ModuleTest() {
        this.moduleCode = "ECM1999";
        this.moduleTitle = "Test Title";
        this.moduleLeaderName = "Test Leader Name";
        this.moduleLeaderEmail = "test@ex.ac.uk";
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    // @Before
    void createTestModule() throws InvalidModuleFormatException, EmptyValueException {
        this.testModule = new Module(this.moduleCode, this.moduleTitle, this.moduleLeaderName, this.moduleLeaderEmail);
    }
    // @Before
    void clearTestModule() {
        // unnecessary
        this.testModule = null;
    }


    @Test
    public void testReadingModuleInfo() throws InvalidModuleFormatException, EmptyValueException {
        createTestModule();
        Assert.assertEquals(this.moduleCode, this.testModule.getCode());
        Assert.assertEquals(this.moduleTitle, this.testModule.getTitle());
        Assert.assertEquals(this.moduleLeaderName, this.testModule.getLeaderName());
        Assert.assertEquals(this.moduleLeaderEmail, this.testModule.getLeaderEmail());
    }

    @Test
    public void testUpdatingModuleInfo() throws InvalidModuleFormatException, EmptyValueException {
        createTestModule();
        String newCode = "FFF1234";
        String newTitle = "Updated Title";
        String newLeaderName = "Updated Leader Name";
        String newLeaderEmail = "test_email.123@exeter.ac.uk";

        this.testModule.setCode(newCode);
        this.testModule.setTitle(newTitle);
        this.testModule.setLeaderName(newLeaderName);
        this.testModule.setLeaderEmail(newLeaderEmail);

        Assert.assertEquals(newCode, this.testModule.getCode());
        Assert.assertEquals(newTitle, this.testModule.getTitle());
        Assert.assertEquals(newLeaderName, this.testModule.getLeaderName());
        Assert.assertEquals(newLeaderEmail, this.testModule.getLeaderEmail());
    }

    @Test
    public void testGettingFullModuleInfo() throws InvalidModuleFormatException, EmptyValueException {
        createTestModule();
        String[] expectedArray = {"ECM1999", "Test Title", "Test Leader Name", "test@ex.ac.uk"};
        Assert.assertEquals(expectedArray, this.testModule.getFullInfo());
    }

    @Test
    public void shouldThrowInvalidModuleFormatExceptionOnCreatingAnInvalidModule() throws InvalidModuleFormatException, EmptyValueException {
        try {                // invalid module code
            new Module("ECM140","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered invalid module code. Please enter a module code in the format ABC1234, ABC2234, ABC3234 or ABCM123."));
        }
        try {                // empty module code
            new Module("","Programming","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's code, please enter one."));
        }
        try {                // empty module title
            new Module("ECM1401","","Jonathan Fieldsend","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's title, please enter one."));
        }
        try {                // empty module leader name
            new Module("ECM1401","Programming","","J.E.Fieldsend@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's leader name, please enter one."));
        }
        try {                // empty email address
            new Module("ECM1401","Programming","Jonathan Fieldsend","");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's leader email, please enter one."));
        }
        try {                // empty domain part of email address
            new Module("ECM1401","Programming","Jonathan Fieldsend","J.E.Fieldsend@");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        try {                // empty local part of email address
            new Module("ECM1401","Programming","Jonathan Fieldsend","@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        try {                // invalid domain
            new Module("ECM1401","Programming","Jonathan Fieldsend","test@test.co.uk");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        // Test that modules with valid emails are created
        new Module("ECM1401","Programming","Jonathan Fieldsend","test@ex.ac.uk");
        new Module("ECM1401","Programming","Jonathan Fieldsend","test@exeter.ac.uk");
    }


    public void shouldThrowInvalidModuleFormatExceptionOnUpdatingModuleWithInvalidValues() throws InvalidModuleFormatException, EmptyValueException {
        Module testModule = new Module("ECM1401","Programming","Jonathan Fieldsend","test@ex.ac.uk");

        try {                // invalid module code
            testModule.setCode("ECM140");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered invalid module code. Please enter a module code in the format ABC1234, ABC2234, ABC3234 or ABCM123."));
        }
        try {                // empty module code
            testModule.setCode("");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's code, please enter one."));
        }
        try {                // empty module title
            testModule.setTitle("");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's title, please enter one."));
        }
        try {                // empty module leader name
            testModule.setLeaderName("");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's leader name, please enter one."));
        }
        try {                // empty email address
            testModule.setLeaderEmail("");
            fail( "Missing exception" );
        } catch (EmptyValueException e){
            assertTrue(e.getMessage().equals("You forgot to enter module's leader email, please enter one."));
        }
        try {                // empty domain part of email address
            testModule.setLeaderEmail("J.E.Fieldsend@");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        try {                // empty local part of email address
            testModule.setLeaderEmail("@exeter.ac.uk");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        try {                // invalid domain
            testModule.setLeaderEmail("test@test.co.uk");
            fail( "Missing exception" );
        } catch (InvalidModuleFormatException e){
            assertTrue(e.getMessage().equals("You've entered an invalid email. Please, make sure it's in the format xyz@exeter.ac.uk or xyz@ex.ac.uk."));
        }
        // Test that valid emails can be input
        testModule.setLeaderEmail("test@ex.ac.uk");
        testModule.setLeaderEmail("test@exeter.ac.uk");

    }

}

        