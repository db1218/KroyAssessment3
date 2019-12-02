/**
 * 
 */
package com.kroy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Import Java File type
import java.io.File;

class KroyTest {
	
	// Allows test class to be accessed in all methods
	private static Kroy TestClass;

	/**
	 * @throws java.lang.Exception
	 */
	// Run before each test
	@BeforeEach
	void setUp() throws Exception {
		// Creates a new Kroy object to test
		TestClass = new Kroy();
	}
	
	/**
	 * Test all assets the class uses exist.
	 */
	@Test
	void testHasAssets() {
		// All assets used by the class
        String[] correctAssets = {"badlogic.jpg"};

        for (int i = 0; i < correctAssets.length; i++) {

            // Create file object from assets file path
            File file = new File("assets/" + correctAssets[i]);

            // Check if file exists
            if (!file.exists()) {
                fail("Missing asset " + correctAssets[i] + " at " + file.getAbsolutePath());
            }
        }   
	}

	/**
	 * Test method for {@link com.kroy.Kroy#dispose()}.
	 */
	@Test
	void testDispose() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.kroy.Kroy#render()}.
	 */
	@Test
	void testRender() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.kroy.Kroy#create()}.
	 */
	@Test
	void testCreate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.kroy.Kroy#init(com.badlogic.gdx.graphics.OrthographicCamera)}.
	 */
	@Test
	void testInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.kroy.Kroy#drawFont(java.lang.String, java.lang.Double, java.lang.Double)}.
	 */
	@Test
	void testDrawFontStringDoubleDouble() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.kroy.Kroy#drawFont(java.lang.String[], java.lang.Double[], java.lang.Double[])}.
	 */
	@Test
	void testDrawFontStringArrayDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

}
