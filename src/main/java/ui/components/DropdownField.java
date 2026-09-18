package ui.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class DropdownField {

    private final Page page;
    private final Locator trigger;
    private final String optionSelectorTemplate;

    public DropdownField(Page page, Locator trigger, String optionSelectorTemplate) {
        this.page = page;
        this.trigger = trigger;
        this.optionSelectorTemplate = optionSelectorTemplate;
    }

    @Step("Select dropdown option by text: {text}")
    public void selectOptionByText(String text) {
        trigger.scrollIntoViewIfNeeded();
        trigger.click();
        page.locator(String.format(optionSelectorTemplate, text)).scrollIntoViewIfNeeded();
        page.locator(String.format(optionSelectorTemplate, text)).click();
    }
}
