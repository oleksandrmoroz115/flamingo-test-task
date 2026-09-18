# DemoQA Practice Form UI Test Cases

**URL**: `https://demoqa.com/automation-practice-form`

## TC-UI-FORM-01: Successful Complete Student Registration
**Tags**: `ui`, `regression`, `form`
**Pre-conditions**: User is on the Practice Form page. Page is fully loaded. (Handle any overlapping ads).
**Test Steps**:
1. Fill First Name and Last Name.
2. Fill User Email.
3. Select Gender radio button.
4. Fill Mobile Number (10 digits).
5. Click Date of Birth input. In the custom Date Picker, select a specific Month, Year, and Day.
6. Type in Subjects auto-complete field and select a suggested option (e.g., "Maths").
7. Select a Hobbies checkbox (e.g., "Sports").
8. Upload a picture (select a test image file from `src/test/resources`).
9. Fill Current Address textarea.
10. Open State dropdown, select a state (e.g., "NCR").
11. Open City dropdown, select a city (e.g., "Delhi").
12. Click Submit button.
13. Wait for the confirmation modal to appear.
**Test Data**:
- First Name: John
- Last Name: Doe
- User Email: `{{test_user_email}}` (from config/faker)
- Gender: Male
- Mobile: 1234567890
- DOB: May 15, 1995
- Subject: Maths
- Hobby: Sports
- File: `test-image.jpg`
- Address: 123 Test Ave, Automation City
- State: NCR
- City: Delhi
**Expected Result**:
- Form submits successfully.
- A modal dialogue titled "Thanks for submitting the form" appears.
- The table inside the modal correctly displays all inputted values mapping strictly to the submitted Test Data.

## TC-UI-FORM-02: Mandatory Fields Validation (Empty Submission)
**Tags**: `ui`, `negative`, `form`
**Pre-conditions**: User is on the Practice Form page.
**Test Steps**:
1. Ensure all fields are empty.
2. Click the Submit button.
3. Validate field states.
**Test Data**: None.
**Expected Result**:
- Form is NOT submitted.
- The confirmation modal does NOT appear.
- Mandatory fields (First Name, Last Name, Gender, Mobile Number) display a CSS validation state (e.g., `border-color` turns red, or `was-validated` class is applied to the form).

## TC-UI-FORM-03: Invalid Data Validation (Phone Number/Email Format)
**Tags**: `ui`, `negative`, `form`
**Pre-conditions**: User is on the Practice Form page.
**Test Steps**:
1. Fill First Name, Last Name, and Gender correctly.
2. Fill Email with invalid format.
3. Fill Mobile Number with less than 10 digits or alphabetic characters.
4. Click Submit button.
**Test Data**:
- Email: `invalid-email-format`
- Mobile: `12345`
**Expected Result**:
- Form is NOT submitted.
- Confirmation modal does NOT appear.
- Email and Mobile fields exhibit invalid validation CSS states (red borders).
