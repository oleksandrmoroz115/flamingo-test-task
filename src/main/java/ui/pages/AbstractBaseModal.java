package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public abstract class AbstractBaseModal {
    protected Page page;
    protected Locator container;

    public AbstractBaseModal(Page page, Locator container) {
        this.page = page;
        this.container = container;
    }
}
