package se.mockachino.stub;

import se.mockachino.Answer;
import se.mockachino.MockContext;
import se.mockachino.MockData;

public class StubAnswer {
	private final MockContext mockContext;
	private final Answer answer;

	public StubAnswer(MockContext mockContext, Answer answer) {
		this.mockContext = mockContext;
		this.answer = answer;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new StubAnswerHandler(answer, mock, data));
	}
}
