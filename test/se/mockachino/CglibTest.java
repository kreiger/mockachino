package se.mockachino;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class CglibTest {
	@Test
	public void testSimple() {
		ArrayList mock = Mockachino.mock(ArrayList.class);
		mock.add("Hello");

		Mockachino.verify(mock).add("Hello");
	}

	@Test
	public void testFinal() {
		try {
			String mock = Mockachino.mock(String.class);
			mock.concat("Hello");

			Mockachino.verify(mock).concat("Hello");
			fail("Should not work");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}