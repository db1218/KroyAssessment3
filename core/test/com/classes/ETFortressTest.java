package com.classes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Import Java File type
import java.io.File;

//LibGDX imports
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

class ETFortressTest {

	private static MovementSprite TestClass;
	
	@BeforeEach
	void setUp() throws Exception {
		// Create classes to test
		Texture texture;
		SpriteBatch batch;
		// create sprite
		batch = new SpriteBatch();
        texture = new Texture("badlogic.jpg");
        TestClass = new ETFortress(batch, texture, 1500, 500);
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
}