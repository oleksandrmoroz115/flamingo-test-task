# QA Automation Agent Manual

## Assignment Overview
This repository contains an Enterprise Java 17 QA automation framework covering:
1.  **Restful Booker API**: Full CRUD operations using REST Assured.
2.  **GraphQL Ecommerce API**: Querying using variables, pagination, and nested types.
3.  **DemoQA UI**: Automated interactions with Form Submission and Web Tables using Playwright for Java.

## Flat Project Structure
- `src/main/java`: Contains the core framework and automation logic safely decoupled.
  - `api/`: API Clients, SpecFactory configurations, and Lombok DTOs.
  - `config/`: Type-safe properties mapping using the Aeonbits Owner framework.
  - `ui/`: Base classes, Page Object Models with fluent `@Getter` locators, and Modals.
  - `ui/data/`: `UserDataGenerator` and Lombok immutable models (`StudentData`, `WebTableRecord`) for completely dynamic test data generation.
- `src/test/java`: 
  - `tests/api/`: Scalable API Tests containing 21 perfectly isolated executions (including 4 discrete CRUD tests for REST: `createBookingTest`, `getBookingByIdTest`, `updateBookingTest`, `deleteBookingTest`).
  - `tests/ui/`: Scalable UI Tests utilizing native Playwright parameterized fixtures and completely dynamic data generation.
  - `infrastructure/logging/`: JUnit 5 `TestExecutionLogger` handling TestNG-style CLI printing.
  - `infrastructure/fixtures/`: ParameterResolver extensions (`PlaywrightFixtureExtension`, `ApiFixtureExtension`) handling dependency injection for Pages and API Clients.

## Guidelines for Adding Tests
- **Consistency**: Follow the established POM with Lombok `@Getter` encapsulation for precise Playwright locators. Base components maintain strictly `private` locators.
- **Locators**: Eliminate inline HTML strings. Wrap XPath and CSS directly into static constants.
- **Dynamic Action**: Avoid static timeouts. Utilize dynamic Playwright JS evaluation (`waitForFunction()`) and auto-waiting logic.
- **Isolation**: Use thread-safe abstractions. Test execution must not depend on fragile pre-seeded UI state.

## Execution & Configuration
- **Configuration**: Managed natively via `Configuration.get()`. Do not embed secrets. Developer overrides belong exclusively in the untracked `local-config.properties`.
- **CI/CD Quality Gate**: The GitHub Actions pipeline strictly enforces block-on-failure without `continue-on-error`.
- **Maven Surefire Integration**: The framework securely runs concurrent forks via `<forkCount>1</forkCount>` while `<testFailureIgnore>false</testFailureIgnore>` ensures absolute pipeline halting on regression.
- **Running Tests**:
  - `mvn clean test` (Entire suite of 21 isolated unit tests)
  - `mvn test -Dgroups="api"` (API specific)
  - `mvn test -Dgroups="ui"` (UI specific)
