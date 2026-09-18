package tests.ui;

import ui.pages.PracticeFormPage;
import ui.data.StudentData;
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
@Feature("DemoQA Practice Form")
@ExtendWith(PlaywrightFixtureExtension.class)
public class PracticeFormTests {

    @Test
    @Story("Successful Complete Student Registration")
    @Description("Verify that a student can successfully register with all fields filled")
    public void testSuccessfulRegistration(PracticeFormPage page) {
        StudentData student = UserDataGenerator.generateValidStudent();
        
        page.open()
            .fillName(student.getFirstName(), student.getLastName())
            .fillEmail(student.getEmail())
            .selectGender(student.getGender())
            .fillMobile(student.getMobileNumber())
            .selectDateOfBirth(student.getDobDay(), student.getDobMonth(), student.getDobYear())
            .selectSubjects(student.getSubject())
            .selectHobbies(student.getHobby())
            .uploadPicture(student.getPicturePath())
            .fillCurrentAddress(student.getAddress())
            .selectStateAndCity(student.getState(), student.getCity())
            .submit();

        PracticeFormPage.ResultsModal modal = page.getResultsModal();
        assertThat(modal.isVisible()).isTrue();
        
        assertThat(modal.getValueFor("Student Name")).isEqualTo(student.getExpectedFullName());
        assertThat(modal.getValueFor("Student Email")).isEqualTo(student.getEmail());
        assertThat(modal.getValueFor("Gender")).isEqualTo(student.getGender());
        assertThat(modal.getValueFor("Mobile")).isEqualTo(student.getMobileNumber());
        assertThat(modal.getValueFor("Date of Birth")).isEqualTo(student.getExpectedDob());
        assertThat(modal.getValueFor("Subjects")).isEqualTo(student.getSubject());
        assertThat(modal.getValueFor("Hobbies")).isEqualTo(student.getHobby());
        assertThat(modal.getValueFor("Picture")).isEqualTo(student.getPictureName());
        assertThat(modal.getValueFor("Address")).isEqualTo(student.getAddress());
        assertThat(modal.getValueFor("State and City")).isEqualTo(student.getExpectedStateCity());
    }

    @Test
    @Story("Mandatory Fields Validation")
    @Description("Verify that submitting an empty form prevents submission")
    public void testEmptySubmissionValidation(PracticeFormPage page) {
        page.open()
            .submit();

        PracticeFormPage.ResultsModal modal = page.getResultsModal();
        assertThat(modal.isVisible()).isFalse();
    }

    @Test
    @Story("Invalid Data Validation")
    @Description("Verify that invalid email and mobile formats are rejected")
    public void testInvalidDataValidation(PracticeFormPage page) {
        StudentData invalidStudent = UserDataGenerator.generateInvalidStudent();

        page.open()
            .fillName(invalidStudent.getFirstName(), invalidStudent.getLastName())
            .selectGender(invalidStudent.getGender())
            .fillEmail(invalidStudent.getEmail())
            .fillMobile(invalidStudent.getMobileNumber())
            .submit();

        PracticeFormPage.ResultsModal modal = page.getResultsModal();
        assertThat(modal.isVisible()).isFalse();
    }
}
