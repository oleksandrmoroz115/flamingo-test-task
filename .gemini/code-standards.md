# Code Standards

## 1. Naming Conventions
- **Classes**: PascalCase (e.g., `PracticeFormPage`, `RestfulBookerTests`).
- **Test Methods**: camelCase prefixed with `test` (e.g., `testSuccessfulRegistration`).
- **Locators (Constants)**: UPPER_SNAKE_CASE (e.g., `GENDER_RADIO_XPATH`).
- **Page Fields**: camelCase matching the element purpose (e.g., `firstNameInput`).

## 2. Strict Security & Configuration
- **No Hardcoded Secrets**: Passwords, tokens, or sensitive API keys must NEVER be hardcoded.
- **Type-Safe Configuration**: Employ the Aeonbits `Owner` library (`Configuration.get()`) mapped to `src/main/resources/config.properties`.
- **Local Overrides**: Developers must use `src/main/resources/local-config.properties` (which is `.gitignore`d) for testing personal credentials or toggling headless mode.

## 3. Playwright Best Practices
- **Native Waiting**: Rely exclusively on Playwright's auto-waiting (e.g., `locator.waitFor(...)` or `.isVisible()`).
- **Prohibited**: NEVER use `Thread.sleep()` under any circumstances.
- **Route Interception**: Implement aggressive ad network blocking using `page.route("**/*", route -> ...)` inside `TestInit` to prevent third-party scripts (e.g., Google Syndication) from causing flaky tests or stealing focus.

## 4. Allure Reporting Standards
- **Documentation Annotations**: Apply `@Feature`, `@Story`, and `@Description` heavily across all test classes and methods.
- **Step Logging**: Wrap complex Page Object and API Client actions with `@Step("Action Description: {param}")`.
- **Screenshot Hooks**: The global JUnit 5 Extension (`TestExecutionLogger` & `ScreenshotWatcher`) natively handles attaching UI screenshots directly to Allure reports automatically on test failures, aborts, or skips.
