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
        this.moduleCode = "ECM9999";
        this.moduleTitle = "Test Title";
        this.moduleLeaderName = "Test Leader Name";
        this.moduleLeaderEmail = "test@email.co.uk";
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    // @Before
    void createTestModule() {
        this.testModule = new Module(this.moduleCode, this.moduleTitle, this.moduleLeaderName, this.moduleLeaderEmail);
    }
    // @Before
    void clearTestModule() {
        // unnecessary
        this.testModule = null;
    }


    @Test
    public void testReadingModuleInfo() {
        createTestModule();
        Assert.assertEquals(this.moduleCode, this.testModule.getCode());
        Assert.assertEquals(this.moduleTitle, this.testModule.getTitle());
        Assert.assertEquals(this.moduleLeaderName, this.testModule.getLeaderName());
        Assert.assertEquals(this.moduleLeaderEmail, this.testModule.getLeaderEmail());
    }

    @Test
    public void testUpdatingModuleInfo() {
        createTestModule();
        String newCode = "FFF1234";
        String newTitle = "Updated Title";
        String newLeaderName = "Updated Leader Name";
        String newLeaderEmail = "Updated Leader Email";

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
    public void testGettingFullModuleInfo() {
        createTestModule();
        String[] expectedArray = {"ECM9999", "Test Title", "Test Leader Name", "test@email.co.uk"};
        Assert.assertEquals(expectedArray, this.testModule.getFullInfo());
    }
}
