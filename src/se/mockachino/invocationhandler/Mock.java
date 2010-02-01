package se.mockachino.invocationhandler;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.expectations.MethodExpectations;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.cleaner.StacktraceCleaner;

import java.lang.reflect.Method;
import java.util.List;

public class Mock extends AbstractInvocationHandler {
	private final MockContext context;
	private final Object impl;

	public Mock(MockContext context, Object impl, String kind) {
		super(kind);
		this.context = context;
		this.impl = impl;
	}

	public Object doInvoke(Object o, Method method, Object[] objects) throws Throwable {
		int callNumber = context.incrementSequence();
		MockData data = context.getData(o);
		List<MethodCall> list = data.getCalls();
		MethodCall methodCall = new MethodCall(method, objects, callNumber, StacktraceCleaner.cleanError(new Throwable()).getStackTrace());
		list.add(methodCall);
		MethodExpectations methodExpectations = data.getExpectations(method);
		MethodExpectation expectation = methodExpectations.findMatch(methodCall);
		if (expectation != null) {
			return expectation.getValue();
		}
		method.setAccessible(true);
		return method.invoke(impl, objects);
	}
}