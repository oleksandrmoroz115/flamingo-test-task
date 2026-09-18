package tests.ui;

import ui.pages.WebTablesPage;
import ui.data.WebTableRecord;
import ui.data.WebTableKeywords;
import ui.data.UserDataGenerator;
import infrastructure.fixtures.PlaywrightFixtureExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
@Feature("DemoQA Web Tables")
@ExtendWith(PlaywrightFixtureExtension.class)
public class WebTablesTests {

    @Test
    @Story("Add a New Record")
    @Description("Verify adding a new record to the web table")
    public void testAddRecord(WebTablesPage page) {
        WebTableRecord record = UserDataGenerator.generateValidRecord();
        
        page.open();

        int initialCount = page.getTableRecordsCount();

        WebTablesPage.RegistrationModal modal = page.openRegistrationForm();
        modal.fillForm(
            record.getFirstName(), 
            record.getLastName(), 
            record.getEmail(), 
            record.getAge(), 
            record.getSalary(), 
            record.getDepartment()
        );
        modal.submit();

        int finalCount = page.getTableRecordsCount();
        assertThat(finalCount).isEqualTo(initialCount + 1);

        String rowData = page.getRowValuesByEmail(record.getEmail());
        assertThat(rowData).contains(
            record.getFirstName(), 
            record.getLastName(), 
            record.getEmail(), 
            record.getAge(), 
            record.getSalary(), 
            record.getDepartment()
        );
    }

    @Test
    @Story("Edit an Existing Record")
    @Description("Verify editing a dynamically created record updates its values in the table")
    public void testEditRecord(WebTablesPage page) {
        WebTableRecord initialRecord = UserDataGenerator.generateValidRecord();
        WebTableRecord updatedRecord = UserDataGenerator.generateValidRecord();
        
        page.open();

        // Setup: Create a dynamic record first
        WebTablesPage.RegistrationModal setupModal = page.openRegistrationForm();
        setupModal.fillForm(
            initialRecord.getFirstName(), 
            initialRecord.getLastName(), 
            initialRecord.getEmail(), 
            initialRecord.getAge(), 
            initialRecord.getSalary(), 
            initialRecord.getDepartment()
        );
        setupModal.submit();

        // Perform Edit
        WebTablesPage.RegistrationModal editModal = page.editRecord(initialRecord.getEmail());
        editModal.fillForm(
            updatedRecord.getFirstName(), 
            updatedRecord.getLastName(), 
            updatedRecord.getEmail(), 
            updatedRecord.getAge(), 
            updatedRecord.getSalary(), 
            updatedRecord.getDepartment()
        );
        editModal.submit();

        // Verify
        String rowData = page.getRowValuesByEmail(updatedRecord.getEmail());
        assertThat(rowData).contains(updatedRecord.getFirstName(), updatedRecord.getSalary());
    }

    @Test
    @Story("Delete a Record")
    @Description("Verify deleting a dynamically created record removes it from the table")
    public void testDeleteRecord(WebTablesPage page) {
        WebTableRecord recordToDelete = UserDataGenerator.generateValidRecord();
        
        page.open();

        // Setup: Create a dynamic record first
        WebTablesPage.RegistrationModal modal = page.openRegistrationForm();
        modal.fillForm(
            recordToDelete.getFirstName(), 
            recordToDelete.getLastName(), 
            recordToDelete.getEmail(), 
            recordToDelete.getAge(), 
            recordToDelete.getSalary(), 
            recordToDelete.getDepartment()
        );
        modal.submit();

        int countBeforeDelete = page.getTableRecordsCount();
        
        // Perform Delete
        page.deleteRecord(recordToDelete.getEmail());

        int countAfterDelete = page.getTableRecordsCount();
        assertThat(countAfterDelete).isEqualTo(countBeforeDelete - 1);
    }

    @Test
    @Story("Search Functionality")
    @Description("Verify search filters the table rows correctly using dynamically generated record")
    public void testSearchFunctionality(WebTablesPage page) {
        WebTableRecord searchRecord = UserDataGenerator.generateValidRecord();
        
        page.open();

        // Setup: Create a dynamic record first
        WebTablesPage.RegistrationModal modal = page.openRegistrationForm();
        modal.fillForm(
            searchRecord.getFirstName(), 
            searchRecord.getLastName(), 
            searchRecord.getEmail(), 
            searchRecord.getAge(), 
            searchRecord.getSalary(), 
            searchRecord.getDepartment()
        );
        modal.submit();

        // Perform Search via unique email
        page.searchRecord(searchRecord.getEmail());
        
        int count = page.getTableRecordsCount();
        assertThat(count).isEqualTo(1);
        
        String rowData = page.getRowValuesByEmail(searchRecord.getEmail());
        assertThat(rowData).contains(
            searchRecord.getFirstName(), 
            searchRecord.getLastName()
        );
    }

    @Test
    @org.junit.jupiter.api.Disabled("DemoQA native table sorting is currently inactive on the UI")
    @Story("Sorting Validation")
    @Description("Verify column sorting functionality")
    public void testSortingValidation(WebTablesPage page) {
        page.open();

        page.sortByColumn(WebTableKeywords.COLUMN_AGE.getValue());
        String firstRowAsc = page.getFirstRowValues();
        
        page.sortByColumn(WebTableKeywords.COLUMN_AGE.getValue());
        String firstRowDesc = page.getFirstRowValues();
        
        assertThat(firstRowAsc).isNotEqualTo(firstRowDesc);
    }
}
