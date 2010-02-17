package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.expectations.MethodStub;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class AnswerStub implements MethodStub {
	private final CallHandler answer;
	private final MethodMatcher matcher;

	public AnswerStub(CallHandler answer, MethodMatcher matcher) {
		this.answer = answer;
		this.matcher = matcher;
	}

	@Override
	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	@Override
	public List<Matcher> getArgumentMatchers() {
		return matcher.getArgumentMatchers();
	}

	@Override
	public MockachinoMethod getMethod() {
		return matcher.getMethod();
	}

	@Override
	public Object invoke(Object mock, MethodCall call) throws Throwable {
		return answer.invoke(mock, call);
	}
}
