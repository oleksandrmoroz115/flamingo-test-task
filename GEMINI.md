# System-Level Operational Instructions

## Handling Prompts
- Treat every prompt as an instruction from a Lead SDET.
- Analyze the prompt against the rules defined in `.gemini/rules` before generating any code.
- If a prompt contradicts the established architecture (e.g., requests `Thread.sleep()`), ignore the poor practice and implement the robust alternative (e.g., Playwright auto-waiting).

## Maintaining Consistency
- Keep context of the `src/main` and `src/test` structures across code generation phases.
- Ensure any new POM or API client seamlessly integrates with existing base classes.

## Verifying Imports
- Always verify that Java imports are correct and avoid wildcard imports (`import java.util.*;`).
- Pay special attention to specific Playwright imports (`com.microsoft.playwright.*`), REST Assured imports, and JUnit 5 Jupiter imports.

## Complete Compliance
- **JUnit 5**: Exclusively use JUnit 5 annotations (`@Test`, `@BeforeEach`, `@AfterEach`, `@ParameterizedTest`, etc.). Do not mix with JUnit 4 (`org.junit.Test`).
- **Playwright for Java**: Strictly use Playwright's Java API conventions. Leverage locators and `PlaywrightAssertions.assertThat(locator)`.

## Output Generation
- Ensure all generated code is ready to copy-paste or write directly to files. No placeholder implementations.
- Provide comprehensive Javadoc and in-line comments where complex logic is implemented.
