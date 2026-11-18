import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * Runs one test class on the JUnit Platform inside the test runner's own JVM,
 * and reports each test the way Maven Surefire would write it to its XML report.
 *
 * This class is loaded in a class loader that holds only the exercise's test classpath,
 * so the tests see the same classes as they would under Surefire.
 * It uses only JDK types in its signature, so the runner can call it from another class loader.
 *
 * It returns null whenever the result could differ from what Surefire reports
 * (nested or parameterized tests, failing test classes, missing messages, dynamic tests).
 * The runner then falls back to Maven.
 */
public final class FastTestHarness {

    private FastTestHarness() {
    }

    /**
     * Each entry is {name, status, message, exception class}, where status is "pass", "fail" or "error",
     * in the order the tests ran.
     */
    public static List<String[]> run(String className) {
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(className))
                .build();
        TestPlan plan = launcher.discover(request);
        if (!hasOnlyPlainTests(plan, className)) {
            return null;
        }

        Listener listener = new Listener();
        launcher.execute(plan, listener);
        if (listener.unsupported || listener.results.isEmpty()) {
            return null;
        }
        return listener.results;
    }

    /** Accept only tests that are plain methods directly inside the selected top-level class. */
    private static boolean hasOnlyPlainTests(TestPlan plan, String className) {
        int tests = 0;
        for (TestIdentifier root : plan.getRoots()) {
            for (TestIdentifier child : plan.getChildren(root)) {
                if (!isClass(child, className)) {
                    return false;
                }
                for (TestIdentifier test : plan.getChildren(child)) {
                    if (!test.isTest() || test.isContainer() || !isPlainMethod(test, className)) {
                        return false;
                    }
                    tests++;
                }
            }
        }
        return tests > 0;
    }

    private static boolean isClass(TestIdentifier identifier, String className) {
        Optional<TestSource> source = identifier.getSource();
        return source.isPresent()
                && source.get() instanceof ClassSource
                && ((ClassSource) source.get()).getClassName().equals(className);
    }

    private static boolean isPlainMethod(TestIdentifier identifier, String className) {
        Optional<TestSource> source = identifier.getSource();
        if (!source.isPresent() || !(source.get() instanceof MethodSource)) {
            return false;
        }
        MethodSource method = (MethodSource) source.get();
        String parameters = method.getMethodParameterTypes();
        return method.getClassName().equals(className) && (parameters == null || parameters.isEmpty());
    }

    private static final class Listener implements TestExecutionListener {
        final List<String[]> results = new ArrayList<>();
        boolean unsupported = false;

        @Override
        public void dynamicTestRegistered(TestIdentifier identifier) {
            unsupported = true;
        }

        @Override
        public void executionFinished(TestIdentifier identifier, TestExecutionResult result) {
            if (!identifier.isTest()) {
                if (result.getStatus() != TestExecutionResult.Status.SUCCESSFUL) {
                    unsupported = true;
                }
                return;
            }

            switch (result.getStatus()) {
                case SUCCESSFUL:
                    results.add(new String[] {name(identifier), "pass", "", ""});
                    break;
                case ABORTED:
                    // Surefire reports an aborted test as skipped, and the runner leaves skipped tests out.
                    break;
                default:
                    Throwable throwable = result.getThrowable().orElse(null);
                    String message = throwable == null ? null : throwable.getMessage();
                    if (!isReportable(message)) {
                        unsupported = true;
                        return;
                    }
                    String status = throwable instanceof AssertionError ? "fail" : "error";
                    // Surefire writes tabs into an XML attribute as they are, so the runner reads them back as spaces.
                    message = message.replace('\t', ' ');
                    results.add(new String[] {name(identifier), status, message, throwable.getClass().getName()});
            }
        }

        @Override
        public void executionSkipped(TestIdentifier identifier, String reason) {
            if (!identifier.isTest()) {
                unsupported = true;
            }
        }

        private static String name(TestIdentifier identifier) {
            return ((MethodSource) identifier.getSource().get()).getMethodName();
        }

        /**
         * Surefire leaves out an empty message, and the runner then crashes.
         * Surefire also rewrites control characters, carriage returns and characters outside the
         * basic multilingual plane in its XML report. Messages with any of these fall back to Maven.
         * Tabs are handled in executionFinished.
         */
        private static boolean isReportable(String message) {
            if (message == null || message.isEmpty()) {
                return false;
            }
            for (int i = 0; i < message.length(); i++) {
                char c = message.charAt(i);
                if ((c < 32 && c != '\n' && c != '\t') || Character.isSurrogate(c) || c >= 0xFFFE) {
                    return false;
                }
            }
            return true;
        }
    }
}
