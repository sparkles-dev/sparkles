package sparkles.support.javalin.testing;

import java.util.List;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import sparkles.support.javalin.testing.rules.TestAppResource;
import sparkles.support.javalin.testing.rules.TestClientResource;

public class JavalinTestRunner extends BlockJUnit4ClassRunner {
  private Class<?> testClass;
  private TestAppResource testAppResource;
  private TestClientResource testClientResource;

	public JavalinTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
    this.testClass = klass;
	}

	@Override
	protected List<TestRule> classRules() {
		List<TestRule> classRules = super.classRules();

		testAppResource = new TestAppResource(testClass);
		classRules.add(testAppResource);

		return classRules;
	}

  @Override
	protected List<TestRule> getTestRules(Object target) {
		List<TestRule> testRules = super.getTestRules(target);

    testClientResource = new TestClientResource(target, testAppResource.serverUrl());
    testRules.add(testClientResource);

    return testRules;
  }

}
