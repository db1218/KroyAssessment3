package com.classes;

import static org.junit.jupiter.api.Assertions.fail;

// Import Java File type
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
//LibGDX imports
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.testrunner.GdxTestRunner;

@RunWith(GdxTestRunner.class)
class FiretruckTest {

    private static Firetruck TestClass;
	
	@BeforeEach
	void setUp() throws Exception {
		// Create classes to test
		Texture texture;
		SpriteBatch batch;
		// create sprite
		batch = new SpriteBatch();
        texture = new Texture("badlogic.jpg");
        TiledMap map = new TmxMapLoader().load("MapAssets/KroyMap.tmx");
        TestClass = new Firetruck((ArrayList) texture, (ArrayList) texture 1000, 500, (TiledMapTileLayer) map.getLayers().get("River"), 1);
    }

    /**
	 * Test all assets the class uses exist.
	 */
	@Test
	void testHasAssets() {
		// All assets used by the class
        String[] correctAssets = {"badlogic.jpg, MapAssets/KroyMap.tmx"};

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