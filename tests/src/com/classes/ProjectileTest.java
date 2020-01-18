package com.classes;

import static com.config.Constants.SCREEN_HEIGHT;
import static com.config.Constants.SCREEN_WIDTH;
import static com.config.Constants.TILE_DIMS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

//Import Kroy game
import com.kroy.Kroy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.classes.Projectile;

// Import test runner
import com.testrunner.GdxTestRunner;

/**
 * @author Joshua
 *
 */
@RunWith(GdxTestRunner.class)
class ProjectileTest {

	/**
	 * Test method for {@link com.classes.Projectile#update(com.badlogic.gdx.graphics.g2d.Batch)}.
	 */
	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.Projectile#Projectile(com.badlogic.gdx.graphics.Texture, float, float)}.
	 */
	@Test
	void testProjectile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.Projectile#calculateTrajectory(com.badlogic.gdx.math.Polygon)}.
	 */
	@Test
	void testCalculateTrajectory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for testing a Projectile would be removed when out of view.
	 * {@link com.classes.Projectile#isOutOfView(com.badlogic.gdx.math.Vector3)}.
	 * 
	 * @author Joshua
	 */
	@Test
	void testProjectileIsOutOfView() {
		// Create camera position to test.
		Vector3 testCameraPosition = new Vector3(80 * TILE_DIMS, 30 * TILE_DIMS, 0);
		
		// Create test projectile.
		// TEXTURE NOT WORKING
		assertTrue(Gdx.files.internal("../core/assets/button.png").exists());
		Texture testTexture = new Texture("../core/assets/button.png");
		Projectile testProjectile = new Projectile(testTexture, 0, 0);
		
		// Test projectile is out of view.
		assertTrue(testProjectile.isOutOfView(testCameraPosition));
	}

	/**
	 * Test method for {@link com.classes.Projectile#isOutOfMap()}.
	 */
	@Test
	void testIsOutOfMap() {
		fail("Not yet implemented");
	}

}
