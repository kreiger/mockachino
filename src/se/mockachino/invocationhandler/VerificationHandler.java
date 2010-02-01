package se.mockachino.invocationhandler;

import se.mockachino.matchers.MethodMatcher;
import se.mockachino.expectations.DefaultMethodExpectations;

import java.lang.reflect.Method;

public abstract class VerificationHandler extends AbstractInvocationHandler {
	protected VerificationHandler(String kind) {
		super(kind);
	}

	public final Object doInvoke(Object o, Method method, Object[] objects) throws Throwable {
		MethodMatcher matcher = new MethodMatcher(method, objects);
		verify(o, matcher);
		return DefaultMethodExpectations.forType(method.getReturnType()).getValue();
	}

	public abstract void verify(Object o, MethodMatcher matcher);
}