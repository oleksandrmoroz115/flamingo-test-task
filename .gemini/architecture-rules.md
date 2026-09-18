# Architecture Rules

## 1. Page Object Model (POM) Standards
- **Inheritance**: UI classes must inherit from `AbstractBasePage`, `BaseTable`, or `AbstractBaseModal`.
- **Encapsulation**: Raw locators (CSS/XPath) must be encapsulated as `private static final String` constants at the top of the class. Base component fields (e.g., `tableBody` in `BaseTable`) must be strictly `private` and manipulated only via structured accessors.
- **Fluent Interface**: Action methods should return `this` or the next logical Page/Modal object.
- **Dynamic Waiting**: Never use fixed delays (e.g., `Thread.sleep` or `page.waitForTimeout`). Utilize Playwright's dynamic state waiting (`waitFor()`, `waitForFunction()`, auto-waiting locators).
- **Lombok Integration**: Use `@Getter` and `@Accessors(fluent = true)` for UI element locators initialized natively in constructors.

## 2. Dependency Injection (Fixtures) Lifecycle
- **Composition over Inheritance**: Do not use `extends TestInit`. Rely strictly on JUnit 5 Extensions annotated via `@ExtendWith` (e.g., `PlaywrightFixtureExtension`, `ApiFixtureExtension`).
- **ParameterResolvers**: Tests dynamically receive instantiated Page Objects, Playwright `Page` instances, or API Clients natively through test method parameters.
- **Concurrency & ThreadLocal**: `BeforeEachCallback`, `ParameterResolver`, and `AfterEachCallback` guarantee sequential execution on the ForkJoinPool thread. The extensions safely manage `ThreadLocal` containers ensuring absolute parallel thread-safety.
- **Cleanup**: Resources must be rigorously closed and `.remove()` called from ThreadLocals during `@AfterEach`.

## 3. Layered API Design
- **Data Transfer Objects (DTOs)**: Place all models under `api/models/`. Rely on Lombok for concise POJO generation.
- **Specifications**: Configure base REST Assured request specs centrally via `api/specs/SpecFactory.java`.
- **Client Wrappers**: Isolate actual HTTP execution methods into dedicated clients.

## 4. Test Structure & Data Decoupling
- **No Monolithic Tests**: Avoid "God Tests". Break down lifecycle flows into discrete, isolated CRUD test cases (e.g., `createBookingTest`, `getBookingByIdTest`, etc.) to pinpoint specific API failures.
- **Dynamic Generation Engine**: Do not use hardcoded static data. User models must be instantiated dynamically via `src/main/java/ui/data/UserDataGenerator.java`. Tests must not rely on fragile default UI data.
- **Immutable Models**: Utilize Lombok's `@Builder` and `@Getter` for all data holders housed within `ui/data/`.
- **No Hardcoding**: Tests should contain zero hardcoded magic strings for inputs or assertions; retrieve dynamic data exclusively through getters or strict enums (`WebTableKeywords`).

## 5. Infrastructure Isolation & CI/CD
- **Separation of Concerns**: Test logic must be strictly separated from framework execution tooling. `src/test/java/tests/` solely contains business logic tests. Core runners and logging belong in `src/test/java/infrastructure/`.
- **Pipeline Enforcement**: The CI/CD workflow strictly disallows `continue-on-error`.
- **Surefire Concurrency Security**: The `maven-surefire-plugin` enforce `<testFailureIgnore>false</testFailureIgnore>` and isolates execution XML reports securely using `<forkCount>1</forkCount>` and `<reuseForks>true</reuseForks>`.
