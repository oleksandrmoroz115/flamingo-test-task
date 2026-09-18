package infrastructure.logging;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TestExecutionLogger implements BeforeAllCallback, AfterAllCallback, TestWatcher, BeforeTestExecutionCallback {

    private static final Logger logger = LoggerFactory.getLogger(TestExecutionLogger.class);

    @Override
    public void beforeAll(ExtensionContext context) {
        String className = context.getRequiredTestClass().getSimpleName();
        logger.info("\n==================================================\n       On Start :- {}\n==================================================", className);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        String className = context.getRequiredTestClass().getSimpleName();
        logger.info("\n==================================================\n       On Finish :- {}\n==================================================", className);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String methodName = context.getRequiredTestMethod().getName();
        logger.info("\n--------------------------------------------------\n{} Started\n--------------------------------------------------", methodName);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String methodName = context.getRequiredTestMethod().getName();
        logger.info("\n--------------------------------------------------\n{} Passed\n--------------------------------------------------", methodName);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error("\n--------------------------------------------------\nFailed because of - {}\n--------------------------------------------------", cause.getMessage());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String methodName = context.getRequiredTestMethod().getName();
        logger.warn("\n--------------------------------------------------\nTest skipped: {}\n--------------------------------------------------", methodName);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String methodName = context.getRequiredTestMethod().getName();
        logger.warn("\n--------------------------------------------------\nTest skipped: {}\n--------------------------------------------------", methodName);
    }
}
