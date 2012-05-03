package org.outerj.daisy.diff;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * This class is mostly copied from org.junit.runners.Parameterized,
 * which unfortunately does not provide for properly named tests.
 * I.e. tests would be simply named 1,2,3,... which is rather unhelpful.
 * This class uses the parameter as the test name instead (if available).
 * 
 * @version 05 Jul 2011
 */
public class BetterParameterized extends Parameterized {

	final List<Runner> myRunners = new ArrayList<Runner>();
	
	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public BetterParameterized(Class<?> klass) throws Throwable {
		super(klass);
		List<Object[]> parametersList= getParametersList(getTestClass());
		for (int i= 0; i < parametersList.size(); i++) {
			myRunners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(),
					parametersList, i));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(TestClass klass)
			throws Throwable {
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(
				null);
	}

	@Override
	protected List<Runner> getChildren() {
		// only return our own ones, not those from the superclass
		return myRunners;
	}
	
	private FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
		List<FrameworkMethod> methods= testClass
		.getAnnotatedMethods(Parameters.class);
		for (FrameworkMethod each : methods) {
			int modifiers= each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				return each;
			}
		}
		
		throw new Exception("No public static parameters method on class "
				+ testClass.getName());
	}

	public class TestClassRunnerForParameters extends
	BlockJUnit4ClassRunner {
		private final int fParameterSetNumber;

		private final List<Object[]> fParameterList;

		TestClassRunnerForParameters(Class<?> type,
				List<Object[]> parameterList, int i) throws InitializationError {
			super(type);
			fParameterList= parameterList;
			fParameterSetNumber= i;
		}

		@Override
		public Object createTest() throws Exception {
			return getTestClass().getOnlyConstructor().newInstance(
					computeParams());
		}

		private Object[] computeParams() throws Exception {
			try {
				return fParameterList.get(fParameterSetNumber);
			} catch (ClassCastException e) {
				throw new Exception(String.format(
						"%s.%s() must return a Collection of arrays.",
						getTestClass().getName(), getParametersMethod(
								getTestClass()).getName()));
			}
		}

		@Override
		protected String getName() {
			try {
				Object[] tempParams = computeParams();
				if (tempParams != null && tempParams.length > 0) {
					if (tempParams.length == 1) {
						return tempParams[0].toString();
					}
					return tempParams.toString();
				}
			} catch (Exception ex) {
				return ex.getMessage();
			}
			
			return String.format("[%s]", fParameterSetNumber);
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(),
					getName());
		}

		@Override
		protected void validateConstructor(List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(RunNotifier notifier) {
			return childrenInvoker(notifier);
		}

	}
}
