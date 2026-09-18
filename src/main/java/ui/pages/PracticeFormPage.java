package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ui.components.DropdownField;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Paths;
import java.util.regex.Pattern;

@Getter
@Accessors(fluent = true)
public class PracticeFormPage extends AbstractBasePage {

    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator userEmailInput;
    private final Locator userNumberInput;
    private final Locator dobInput;
    private final Locator dobMonthSelect;
    private final Locator dobYearSelect;
    private final Locator subjectsInput;
    private final Locator subjectsOption;
    private final Locator uploadPicture;
    private final Locator currentAddress;
    private final DropdownField stateDropdown;
    private final DropdownField cityDropdown;
    private final Locator submitButton;
    private final Locator modalContent;

    private static final String LABEL_BY_TEXT_XPATH = "//label[text()='%s']";
    private static final String DOB_DAY_XPATH = "//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and text()='%s']";
    private static final String STATE_OPTION_XPATH = "//div[contains(@id, 'react-select-3-option') and text()='%s']";
    private static final String CITY_OPTION_XPATH = "//div[contains(@id, 'react-select-4-option') and text()='%s']";

    public PracticeFormPage(Page page) {
        super(page);
        this.firstNameInput = page.locator("#firstName");
        this.lastNameInput = page.locator("#lastName");
        this.userEmailInput = page.locator("#userEmail");
        this.userNumberInput = page.locator("#userNumber");
        this.dobInput = page.locator("#dateOfBirthInput");
        this.dobMonthSelect = page.locator(".react-datepicker__month-select");
        this.dobYearSelect = page.locator(".react-datepicker__year-select");
        this.subjectsInput = page.locator("#subjectsInput");
        this.subjectsOption = page.locator(".subjects-auto-complete__option");
        this.uploadPicture = page.locator("#uploadPicture");
        this.currentAddress = page.locator("#currentAddress");
        this.stateDropdown = new DropdownField(page, page.locator("#state"), STATE_OPTION_XPATH);
        this.cityDropdown = new DropdownField(page, page.locator("#city"), CITY_OPTION_XPATH);
        this.submitButton = page.locator("#submit");
        this.modalContent = page.locator(".modal-content");
    }

    private Locator labelByText(String text) {
        return page.locator(String.format(LABEL_BY_TEXT_XPATH, text));
    }

    private Locator dobDay(String day) {
        return page.locator(String.format(DOB_DAY_XPATH, day)).first();
    }

    @Step("Open Practice Form Page")
    public PracticeFormPage open() {
        super.open("/automation-practice-form");
        return this;
    }

    @Step("Fill First Name: {firstName} and Last Name: {lastName}")
    public PracticeFormPage fillName(String firstName, String lastName) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        return this;
    }

    @Step("Fill Email: {email}")
    public PracticeFormPage fillEmail(String email) {
        userEmailInput.fill(email);
        return this;
    }

    @Step("Select Gender: {gender}")
    public PracticeFormPage selectGender(String gender) {
        labelByText(gender).click();
        return this;
    }

    @Step("Fill Mobile Number: {mobile}")
    public PracticeFormPage fillMobile(String mobile) {
        userNumberInput.fill(mobile);
        return this;
    }

    @Step("Select Date of Birth: {day} {month} {year}")
    public PracticeFormPage selectDateOfBirth(String day, String month, String year) {
        dobInput.click();
        dobMonthSelect.selectOption(month);
        dobYearSelect.selectOption(year);
        dobDay(day).click();
        return this;
    }

    @Step("Select Subject: {subject}")
    public PracticeFormPage selectSubjects(String subject) {
        subjectsInput.fill(subject);
        subjectsOption.filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + subject + "$"))).first().click();
        return this;
    }

    @Step("Select Hobby: {hobby}")
    public PracticeFormPage selectHobbies(String hobby) {
        labelByText(hobby).click();
        return this;
    }

    @Step("Upload Picture: {relativeFilePath}")
    public PracticeFormPage uploadPicture(String relativeFilePath) {
        uploadPicture.setInputFiles(Paths.get(relativeFilePath));
        return this;
    }

    @Step("Fill Current Address: {address}")
    public PracticeFormPage fillCurrentAddress(String address) {
        currentAddress.fill(address);
        return this;
    }

    @Step("Select State: {state} and City: {city}")
    public PracticeFormPage selectStateAndCity(String state, String city) {
        stateDropdown.selectOptionByText(state);
        cityDropdown.selectOptionByText(city);
        return this;
    }

    @Step("Submit Form")
    public PracticeFormPage submit() {
        submitButton.click();
        return this;
    }

    public ResultsModal getResultsModal() {
        return new ResultsModal(page, modalContent);
    }

    public static class ResultsModal extends AbstractBaseModal {

        private static final String ROW_VALUE_XPATH = "//td[text()='%s']/following-sibling::td";

        public ResultsModal(Page page, Locator container) {
            super(page, container);
        }

        private Locator rowValueLocator(String label) {
            return container.locator(String.format(ROW_VALUE_XPATH, label));
        }

        public String getValueFor(String label) {
            return rowValueLocator(label).innerText();
        }

        public boolean isVisible() {
            return container.isVisible();
        }
    }
}
