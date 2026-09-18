package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import ui.components.tables.BaseTable;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class WebTablesPage extends AbstractBasePage {

    private final Locator addNewRecordButton;
    private final Locator modalContent;
    private final Locator searchBox;
    private final Locator tableHeaders;
    private final DataTable dataTable;

    public WebTablesPage(Page page) {
        super(page);
        this.addNewRecordButton = page.locator("#addNewRecordButton");
        this.modalContent = page.locator(".modal-content");
        this.searchBox = page.locator("#searchBox");
        this.tableHeaders = page.locator("table thead th");
        this.dataTable = new DataTable(page, page.locator("table"));
    }

    public static class DataTable extends BaseTable {
        public DataTable(Page page, Locator tableLocator) {
            super(page, tableLocator);
        }
    }

    @Getter
    @Accessors(fluent = true)
    public static class RegistrationModal extends AbstractBaseModal {

        private final Locator firstNameInput;
        private final Locator lastNameInput;
        private final Locator userEmailInput;
        private final Locator ageInput;
        private final Locator salaryInput;
        private final Locator departmentInput;
        private final Locator submitButton;

        public RegistrationModal(Page page, Locator container) {
            super(page, container);
            this.firstNameInput = container.locator("#firstName");
            this.lastNameInput = container.locator("#lastName");
            this.userEmailInput = container.locator("#userEmail");
            this.ageInput = container.locator("#age");
            this.salaryInput = container.locator("#salary");
            this.departmentInput = container.locator("#department");
            this.submitButton = container.locator("#submit");
        }

        @Step("Fill Registration Form: {firstName}, {lastName}, {email}, {age}, {salary}, {department}")
        public RegistrationModal fillForm(String firstName, String lastName, String email, String age, String salary, String department) {
            firstNameInput.clear();
            firstNameInput.fill(firstName);
            lastNameInput.clear();
            lastNameInput.fill(lastName);
            userEmailInput.clear();
            userEmailInput.fill(email);
            ageInput.clear();
            ageInput.fill(age);
            salaryInput.clear();
            salaryInput.fill(salary);
            departmentInput.clear();
            departmentInput.fill(department);
            return this;
        }

        @Step("Submit Registration Form")
        public void submit() {
            submitButton.click();
        }
    }

    @Step("Open Web Tables Page")
    public WebTablesPage open() {
        super.open("/webtables");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this;
    }

    @Step("Open Registration Form Modal")
    public RegistrationModal openRegistrationForm() {
        addNewRecordButton.click();
        return new RegistrationModal(page, modalContent);
    }

    @Step("Search for record: {keyword}")
    public WebTablesPage searchRecord(String keyword) {
        searchBox.clear();
        searchBox.fill(keyword);
        return this;
    }

    @Step("Edit record by email: {email}")
    public RegistrationModal editRecord(String email) {
        Locator targetRow = dataTable.getRowByText(email);
        targetRow.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator button = targetRow.locator("span[id^='edit-record-'], [title='Edit']").first();
        button.scrollIntoViewIfNeeded();
        button.click();
        return new RegistrationModal(page, modalContent);
    }

    @Step("Delete record by email: {email}")
    public WebTablesPage deleteRecord(String email) {
        Locator targetRow = dataTable.getRowByText(email);
        targetRow.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator button = targetRow.locator("span[id^='delete-record-'], [title='Delete']").first();
        button.scrollIntoViewIfNeeded();
        button.click();
        return this;
    }

    @Step("Sort table by column: {columnName}")
    public WebTablesPage sortByColumn(String columnName) {
        String oldFirstRowText = getFirstRowValues();
        tableHeaders.filter(new Locator.FilterOptions().setHasText(columnName)).first().click();
        
        page.waitForFunction("oldText => document.querySelector('table tbody tr').innerText.trim() !== oldText.trim()", oldFirstRowText);
        return this;
    }

    @Step("Get table records count")
    public int getTableRecordsCount() {
        dataTable.waitForBody();
        int count = 0;
        int rows = dataTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            if (!dataTable.getRow(i).innerText().trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    @Step("Get row values for email: {email}")
    public String getRowValuesByEmail(String email) {
        Locator targetRow = dataTable.getRowByText(email);
        targetRow.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return targetRow.first().innerText();
    }

    @Step("Get first row values")
    public String getFirstRowValues() {
        dataTable.waitForBody();
        int rows = dataTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            String text = dataTable.getRow(i).innerText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }
        return "";
    }
}
