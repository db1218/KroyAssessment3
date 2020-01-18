package com.screens;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Import Java File type
import java.io.File;

// Import Kroy game
import com.kroy.Kroy;

class GameScreenTest {
	
	// Allows test class to be accessed in all methods.
	private static GameScreen TestClass;

	@BeforeEach
	void setUp() throws Exception {
		// Create classes to test
		Kroy game = new Kroy();
		TestClass = new GameScreen(game);
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

	@Test
	void testGameScreen() {
		fail("Not yet implemented");
	}

	@Test
	void testRender() {
		fail("Not yet implemented");
	}

	@Test
	void testResize() {
		fail("Not yet implemented");
	}

	@Test
	void testShow() {
		fail("Not yet implemented");
	}

	@Test
	void testHide() {
		fail("Not yet implemented");
	}

	@Test
	void testPause() {
		fail("Not yet implemented");
	}

	@Test
	void testResume() {
		fail("Not yet implemented");
	}

	@Test
	void testDispose() {
		fail("Not yet implemented");
	}

}
