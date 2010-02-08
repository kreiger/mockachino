package se.mockachino;

import se.mockachino.expectations.MethodExpectations;
import se.mockachino.listener.MethodListener;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.util.SafeIteratorList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockData<T> {
	public final static MethodCall NULL_METHOD = new MethodCall(MockachinoMethod.TOSTRING, new Object[]{}, 0, new StackTraceElement[]{});
	private final Class<T> iface;
	private final List<MethodCall> calls;
	private final List<MethodCall> readOnlyCalls;
	private final Map<MockachinoMethod, List<MethodListener>> listeners;
	private final Map<MockachinoMethod, MethodExpectations> expectations;

	public MockData(Class<T> iface) {
		this.iface = iface;
		calls = new SafeIteratorList<MethodCall>(new ArrayList<MethodCall>(), NULL_METHOD);
		readOnlyCalls = Collections.unmodifiableList(calls);
		expectations = new HashMap<MockachinoMethod,MethodExpectations>();
		listeners = new HashMap<MockachinoMethod,List<MethodListener>>();
		for (Method reflectMethod : iface.getMethods()) {
			MockachinoMethod method = new MockachinoMethod(reflectMethod);
			expectations.put(method, new MethodExpectations());
			listeners.put(method,
					new SafeIteratorList<MethodListener>(
							new ArrayList<MethodListener>(), null));
		}
	}

	public List<MethodCall> getCalls() {
		return readOnlyCalls;
	}

	public MethodExpectations getExpectations(MockachinoMethod method) {
		return expectations.get(method);
	}

	public List<MethodListener> getListeners(MockachinoMethod method) {
		return listeners.get(method);
	}

	public Class<T> getInterface() {
		return iface;
	}

	public synchronized MethodCall addCall(MockContext mockContext, MockachinoMethod method, Object[] objects, StackTraceElement[] stackTrace) {
		int callNumber = mockContext.incrementSequence();
		MethodCall call = new MethodCall(method, objects, callNumber, stackTrace);
		calls.add(call);
		return call;
	}

	public synchronized void reset() {
		calls.clear();
	}

}
