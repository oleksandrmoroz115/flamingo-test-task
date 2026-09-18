package ui.components.tables;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BaseTable {
    protected final Page page;
    private final Locator tableLocator;
    private final Locator tableBody;
    private final Locator tableRows;

    public BaseTable(Page page, Locator tableLocator) {
        this.page = page;
        this.tableLocator = tableLocator;
        this.tableBody = tableLocator.locator("tbody");
        this.tableRows = tableBody.locator("tr");
    }

    public Locator getRowByText(String text) {
        return tableRows.filter(new Locator.FilterOptions().setHasText(text));
    }

    public Locator getCell(int rowIndex, int columnIndex) {
        return tableRows.nth(rowIndex).locator("td").nth(columnIndex);
    }

    public void shouldHaveRows(int expectedSize) {
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(tableRows).hasCount(expectedSize);
    }

    public int getRowCount() {
        return tableRows.count();
    }

    public Locator getRow(int index) {
        return tableRows.nth(index);
    }

    public void waitForBody() {
        tableBody.waitFor();
    }

    public Locator getTableLocator() {
        return tableLocator;
    }
}
