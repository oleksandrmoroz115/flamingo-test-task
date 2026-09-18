package ui.pages;

import config.Configuration;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public abstract class AbstractBasePage {
    protected Page page;

    public AbstractBasePage(Page page) {
        this.page = page;
    }

    public void open(String path) {
        page.navigate(Configuration.get().demoqaBaseUrl() + path);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.addStyleTag(new Page.AddStyleTagOptions().setContent("footer, #fixedban { display: none !important; }"));
    }
}
