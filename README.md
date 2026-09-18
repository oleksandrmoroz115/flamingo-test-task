# Enterprise Java 17 Automation Framework
**Playwright | REST Assured | GraphQL | JUnit 5 | Allure**

Welcome to the Flamingo QA test automation repository. This framework is built to test the **Restful Booker API**, a **GraphQL Ecommerce API**, and **DemoQA Web UI** seamlessly in a highly concurrent and scalable architecture.

Follow this guide step-by-step to quickly spin up the environment on your local machine.

---

## Step 1: System Prerequisites

Before starting, ensure your system has the required development dependencies installed:

1. **Java Development Kit (JDK) 17**:
   - Verify installation: Run `java -version` and `javac -version` in your terminal. They should output version `17.x`.
   - *If missing*: Download and install it from [Adoptium (Temurin 17)](https://adoptium.net/temurin/releases/?version=17).
2. **Apache Maven**:
   - Verify installation: Run `mvn -v`.
   - *If missing*: Install it via Homebrew on macOS (`brew install maven`), Chocolatey on Windows (`choco install maven`), or download it from the [Maven website](https://maven.apache.org/download.cgi).

---

## Step 2: Clone & Local Setup

1. **Clone the repository**:
   Open your terminal and run:
   ```bash
   git clone <repository-url>
   cd flamingo-test-task
   ```

2. **Setup Local Configuration**:
   The framework relies on a secure properties file for environmental variables (like browser type and headless toggles).
   Copy the provided template to active execution:
   ```bash
   cp src/main/resources/local-config.properties.template src/main/resources/local-config.properties
   ```
   *(Note: `local-config.properties` is ignored by Git, meaning you can safely override passwords or tweak `ui.headless=false` inside this file without pushing changes to the team).*

---

## Step 3: Install Playwright Browsers

Playwright requires specific native browser binaries to run UI tests effectively. Install the Chromium dependency using the included Maven exec command:

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium"
```

---

## Step 4: Running Tests

The test suite runs through Maven. You can trigger the entire suite or execute specific segments using JUnit 5 tags.

* **Run Everything**:
  ```bash
  mvn clean test
  ```

* **Run API Tests Only (Restful Booker & GraphQL)**:
  ```bash
  mvn test -Dgroups="api"
  ```

* **Run UI Tests Only (DemoQA Practice Form & Web Tables)**:
  ```bash
  mvn test -Dgroups="ui"
  ```

**Pro Tip (Debugging UI)**: By default, UI tests execute in the background (headless). If you want to visually watch the browser interact, open your `src/main/resources/local-config.properties` file and change `ui.headless=true` to `ui.headless=false`.

---

## Step 5: Generating and Viewing Allure Reports

The framework deeply integrates with **Allure** to generate rich, interactive HTML reports featuring execution step timelines, API payload request/response logs, and automatic failure screenshots.

To generate and serve the interactive report after running tests:
```bash
mvn allure:serve
```
This command automatically parses the raw results generated in `target/allure-results/` and opens a dashboard in your default web browser. Inside the dashboard, you can view execution times, attached UI screenshots (in case of failures or skips), and trace test steps directly.

---

## Step 6: Architecture & Engineering Highlights

This framework was engineered enforcing strict modern automation architecture:

* **Dependency Injection (Fixtures)**: Inheritance (e.g. `extends TestInit`) is completely eliminated. We leverage JUnit 5 `@ExtendWith(PlaywrightFixtureExtension.class)` and `@ExtendWith(ApiFixtureExtension.class)`. Tests simply request the Page Objects or API Clients they need right inside the method signature, drastically reducing boilerplate code and enabling true composition.
* **Dynamic Test Data Generation**: Statically hardcoded inputs have been replaced. Models like `StudentData` and `WebTableRecord` are completely immutable (via Lombok `@Builder`), built deterministically or dynamically at runtime using `UserDataGenerator` to ensure each parallel thread executes with completely unique emails, phone numbers, and names without collision. Tests no longer depend on seeded mock data!
* **Fluent POM & Strict Encapsulation**: Page Object Models utilize strictly private component fields, mapping locators securely via `String.format()` constants, completely eliminating inline spaghetti code and leaky abstractions.
* **Advanced Thread-Safety & Pipeline Security**: 
  - Leverages `ThreadLocal` storage directly within the Fixture extensions, ensuring concurrent execution threads absolutely never collide.
  - Test suites are strictly decoupled into single-responsibility actions (e.g., separating REST CRUD into 4 discrete tests).
  - The CI/CD GitHub Actions workflow strictly halts on failures without `continue-on-error`, backed natively by Maven Surefire's `<testFailureIgnore>false</testFailureIgnore>` and isolated XML generation pipelines (`<forkCount>1</forkCount>`).
* **Auto-Resiliency & Dynamic Waiting**: 
  - Hardcoded thread sleeps are completely banned. Playwright utilizes dynamic state polling (e.g., `waitForFunction()`) directly against the JS execution cycle.
  - Third-party ad trackers and bloat scripts that cause severe flakiness (e.g., `googlesyndication.com`) are securely aborted in real-time via `page.route()`.
  - Maven Surefire is configured to automatically instantly retry failing endpoints one time to elegantly recover from DemoQA's strict Cloudflare rate limiters.
* **Custom Event Logging**: A bespoke TestNG-style `TestExecutionLogger` tracks JUnit 5 lifecycles directly into the terminal stream for flawless CI/CD visibility.

---

## Writing Tests with Fixtures (Dependency Injection)

Writing new tests is effortless. There's no driver initialization, no extending base classes, and no manual instantiation of classes.

**Example: Writing a UI Test**
Simply add the `@ExtendWith(PlaywrightFixtureExtension.class)` annotation, and pass your Page Object directly into the test method signature!

```java
@ExtendWith(PlaywrightFixtureExtension.class)
public class PracticeFormTests {

    @Test
    public void testRegistration(PracticeFormPage page) {
        // The page object is completely instantiated and ready to use!
        page.open()
            .fillName("John", "Doe")
            .submit();
    }
}
```

**Example: Writing an API Test**
Use the `ApiFixtureExtension` to inject your API clients natively:

```java
@ExtendWith(ApiFixtureExtension.class)
public class RestfulBookerTests {

    @Test
    public void testGetBooking(BookerClient client) {
        // The API client is injected automatically
        Booking booking = client.getBooking(123);
        assertThat(booking.getFirstname()).isNotBlank();
    }
}
```
