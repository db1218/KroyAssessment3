package tests;

// Java File type import
import java.io.File;

// JUnit imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

// Class imports
import com.kroy.Kroy;

class KroyTests {

    // Allows test class to be acessed in all methods
    private static Kroy TestClass;

    // Runs before all tests
    @BeforeAll
    public static void setUp() {
        // Create a new Kroy object to test
        TestClass = new Kroy();
    }
     
    // Checks that the class contains all the required methods
    @Test
    public void hasCorrectMethods() {

        // All methods that should be in the class
        String[] correctMethods = {"create", "render", "dispose", "init", "drawFont"};

        for (int i = 0; i < correctMethods.length; i++) {
            try {
                // JUnit test to see if it exists
                assertEquals(correctMethods[i], TestClass.getClass().getMethod(correctMethods[i]).getName());
            } catch (NoSuchMethodException | SecurityException e) {
                fail("Class does not contain " + correctMethods[i] + " method");
            }
        }
    }

    // Checks that all the assets the class uses exist
    @Test
    public void hasAssets() {

        // All methods that chould be in the class
        String[] correctAssets = {"badlogic.jpg"};

        for (int i = 0; i < correctAssets.length; i++) {

            // Create file object from filepath
            File file = new File("assets/" + correctAssets[i]);

            // Check if file exists
            if (!file.exists() || !file.getAbsolutePath().contains("assets/" + correctAssets[i])){
                fail("Missing asset " + correctAssets[i]);
            }
        }   
    }

    // Runs after all tests, perform any clean up here
    @AfterAll
    public static void cleanUp() {
        // Add here if neccessary
    }
}