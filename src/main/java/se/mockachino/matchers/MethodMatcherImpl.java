package se.mockachino.matchers;

import se.mockachino.MethodCall;
import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.Formatting;
import se.mockachino.util.MockachinoMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodMatcherImpl implements MethodMatcher {
	private final MockachinoMethod method;
	private final List<Matcher> argumentMatchers;

	public MethodMatcherImpl(MockachinoMethod method, Object[] arguments) {
		this(method, convert(arguments, method.getMethod().isVarArgs()));
	}

	public MethodMatcherImpl(MockachinoMethod method, List<Matcher> argumentMatchers) {
		this.method = method;
		this.argumentMatchers = argumentMatchers;
	}

	private static List<Matcher> convert(Object[] arguments, boolean varArgs) {
		List<Matcher> argumentMatchers = new ArrayList<Matcher>();
		if (arguments != null) {
			if (MatcherThreadHandler.isClean()) {
				for (Object argument : arguments) {
					argumentMatchers.add(new EqualityMatcher(argument));
				}
			} else {
				for (Object argument : arguments) {
					argumentMatchers.add(MatcherThreadHandler.getMatcher(argument, varArgs));
				}
			}
		}
		return argumentMatchers;
	}

	public static MethodMatcherImpl matchAll(MockachinoMethod method) {
		MatcherThreadHandler.assertEmpty();
		List<Matcher> matchers = new ArrayList<Matcher>();
		for (Class aClass : method.getParameters()) {
			matchers.add(MatchersBase.mAny(aClass));
		}
		return new MethodMatcherImpl(method, matchers);
	}

	@Override
	public boolean matches(MethodCall methodCall) {
		if (!method.equals(methodCall.getMethod())) {
			return false;
		}

		Object[] arguments = methodCall.getArguments();
		if (arguments == null) {
			return argumentMatchers.size() == 0;
		}
		int n = arguments.length;
		if (argumentMatchers.size() != n) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			if (!argumentMatchers.get(i).matches(arguments[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return method.getName() + "(" + Formatting.list(argumentMatchers) + ")";
	}

	@Override
	public MockachinoMethod getMethod() {
		return method;
	}

	@Override
	public List<Matcher> getArgumentMatchers() {
		return argumentMatchers;
	}
}
