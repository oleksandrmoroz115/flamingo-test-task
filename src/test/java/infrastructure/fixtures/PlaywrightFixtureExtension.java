package infrastructure.fixtures;

import config.Configuration;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import ui.pages.AbstractBasePage;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class PlaywrightFixtureExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback, TestWatcher {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    @Override
    public void beforeEach(ExtensionContext context) {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(Configuration.get().headless());

        Browser browser;
        String browserName = Configuration.get().browser().toLowerCase();
        if ("firefox".equals(browserName)) {
            browser = playwright.firefox().launch(launchOptions);
        } else if ("webkit".equals(browserName)) {
            browser = playwright.webkit().launch(launchOptions);
        } else {
            browser = playwright.chromium().launch(launchOptions);
        }
        browserThreadLocal.set(browser);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080);
        BrowserContext browserContext = browser.newContext(contextOptions);
        browserContext.setDefaultTimeout(Configuration.get().timeout());

        browserContext.route("**/*", route -> {
            String url = route.request().url();
            if (url.contains("googlesyndication.com") || url.contains("googletagservices.com") || 
                url.contains("googletagmanager.com") || url.contains("doubleclick.net") || 
                url.contains("amazon-adsystem.com")) {
                route.abort();
            } else {
                route.resume();
            }
        });

        contextThreadLocal.set(browserContext);
        Page page = browserContext.newPage();
        pageThreadLocal.set(page);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (pageThreadLocal.get() != null) {
            pageThreadLocal.get().close();
            pageThreadLocal.remove();
        }
        if (contextThreadLocal.get() != null) {
            contextThreadLocal.get().close();
            contextThreadLocal.remove();
        }
        if (browserThreadLocal.get() != null) {
            browserThreadLocal.get().close();
            browserThreadLocal.remove();
        }
        if (playwrightThreadLocal.get() != null) {
            playwrightThreadLocal.get().close();
            playwrightThreadLocal.remove();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> paramType = parameterContext.getParameter().getType();
        return paramType.equals(Page.class) || AbstractBasePage.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> paramType = parameterContext.getParameter().getType();
        Page page = pageThreadLocal.get();

        if (paramType.equals(Page.class)) {
            return page;
        }

        try {
            return paramType.getConstructor(Page.class).newInstance(page);
        } catch (Exception e) {
            throw new ParameterResolutionException("Failed to instantiate Page Object fixture", e);
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        takeScreenshot("Failure Screenshot");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        takeScreenshot("Aborted Screenshot");
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        takeScreenshot("Skipped Screenshot");
    }

    private void takeScreenshot(String name) {
        Page page = pageThreadLocal.get();
        if (page != null && !page.isClosed()) {
            try {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
            } catch (Exception e) {
                // Safely ignore
            }
        }
    }
}
