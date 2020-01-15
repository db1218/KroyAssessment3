package com.classes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

//Import Kroy game
import com.kroy.Kroy;
import com.badlogic.gdx.graphics.Color;
import com.classes.ResourceBar;

/**
 * @author Joshua
 *
 */
class ResourceBarTest {

	/** 
	 * Test ResourceBar is created.
	 * For a current resource amount to be returned, bar must have been created.
	 */
	@Test
	void testResourceBarIsCreated() {
		// Create resource bar
		ResourceBar barToTest = new ResourceBar(50, 100);
		barToTest.setMaxResource((int) 2);
		// Test there is an amount.
		assertEquals(barToTest.getCurrentAmount(), 2);
	}
	
	/**
	 * Test method for {@link com.classes.ResourceBar#ResourceBar(float, float)}.
	 */
	@Test
	void testResourceBar() {
		
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#update(com.badlogic.gdx.graphics.g2d.Batch)}.
	 */
	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#setPosition(float, float)}.
	 */
	@Test
	void testSetPosition() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#setFade(boolean, boolean)}.
	 */
	@Test
	void testSetFade() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#getFade()}.
	 */
	@Test
	void testGetFade() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#setColourRange(com.badlogic.gdx.graphics.Color[])}.
	 */
	@Test
	void testSetColourRange() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#getCurrentAmount()}.
	 */
	@Test
	void testGetCurrentAmount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#getMaxAmount()}.
	 */
	@Test
	void testGetMaxAmount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#setMaxResource(int)}.
	 */
	@Test
	void testSetMaxResource() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#setResourcePercentage(int)}.
	 */
	@Test
	void testSetResourcePercentage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#addResourceAmount(int)}.
	 */
	@Test
	void testAddResourceAmount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.classes.ResourceBar#subtractResourceAmount(int)}.
	 */
	@Test
	void testSubtractResourceAmount() {
		fail("Not yet implemented");
	}

}
