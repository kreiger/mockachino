package se.mockachino.matchers.matcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.matchers.MatchersBase.*;

public class AndOrMatcherTest {
	@Test
	public void testSimple() {
		OrMatcher<Object> orMatcher = or();
		assertEquals("false", orMatcher.asString());

		AndMatcher<Object> andMatcher = and();
		assertEquals("true", andMatcher.asString());

	}

	@Test
	public void testOr() {
		Matcher<String> orMatcher = or(contains("Foo"), eq("Hello"));
		assertEquals(false, orMatcher.matches("X"));
		assertEquals(true, orMatcher.matches("Hello"));
		assertEquals(true, orMatcher.matches("AFooA"));
		assertEquals("(regexp(\".*Foo.*\") | \"Hello\")", orMatcher.toString());
	}

	@Test
	public void testAnd() {
		Matcher<String> andMatcher = and(contains("lo"), contains("Hell"));
		assertEquals(false, andMatcher.matches("aloa"));
		assertEquals(false, andMatcher.matches("Hellas"));
		assertEquals(true, andMatcher.matches("Hello"));
		assertEquals("(regexp(\".*lo.*\") & regexp(\".*Hell.*\"))", andMatcher.toString());
	}


}
