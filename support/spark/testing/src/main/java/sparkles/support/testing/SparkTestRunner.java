package sparkles.support.testing;

import java.util.ArrayList;
import java.util.List;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import sparkles.support.testing.rules.SparkTestAppResource;
import sparkles.support.testing.rules.SparkTestClientResource;

public class SparkTestRunner extends BlockJUnit4ClassRunner {
  private Class<?> testClass;
  private String serverUrl;

	public SparkTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
    this.testClass = klass;
	}

	@Override
	protected List<TestRule> classRules() {
		List<TestRule> classRules = new ArrayList<>(super.classRules());

		SparkTestAppResource classRule = new SparkTestAppResource(testClass, 4567);
		classRules.add(classRule);
    serverUrl = classRule.serverUrl();

		return classRules;
	}

  @Override
	protected List<TestRule> getTestRules(Object target) {
		List<TestRule> testRules = super.getTestRules(target);

    SparkTestClientResource testRule = new SparkTestClientResource(target, serverUrl);
    testRules.add(testRule);

    return testRules;
  }

}
