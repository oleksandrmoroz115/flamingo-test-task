# DemoQA Web Tables UI Test Cases

**URL**: `https://demoqa.com/webtables`

## TC-UI-TABLE-01: Add a New Record
**Tags**: `ui`, `smoke`, `tables`
**Pre-conditions**: User is on the Web Tables page.
**Test Steps**:
1. Click the "Add" button.
2. Verify the Registration Form modal appears.
3. Fill all fields: First Name, Last Name, Email, Age, Salary, Department.
4. Click "Submit" on the modal.
5. Verify the modal closes.
6. Verify the table contains a new row with the entered details.
**Test Data**:
- First Name: Alice
- Last Name: Smith
- Email: `{{test_email_alice}}`
- Age: 30
- Salary: 50000
- Department: QA
**Expected Result**:
- Modal functions correctly.
- The exact details inputted appear in a row within the table grid.

## TC-UI-TABLE-02: Edit an Existing Record
**Tags**: `ui`, `regression`, `tables`
**Pre-conditions**: User is on the Web Tables page. Table has at least one existing row.
**Test Steps**:
1. Locate a specific row (e.g., the first default row for "Cierra").
2. Click the "Edit" action icon for that row.
3. Update specific fields in the modal (e.g., change First Name and Salary).
4. Click "Submit".
5. Verify the table row updates immediately to reflect the new values.
**Test Data**:
- Target row: Cierra Vega
- New First Name: Sierra
- New Salary: 12000
**Expected Result**:
- The modal populates with the existing data.
- After submission, the specific row shows "Sierra" and "12000" instead of the old data.

## TC-UI-TABLE-03: Delete a Record
**Tags**: `ui`, `regression`, `tables`
**Pre-conditions**: User is on the Web Tables page. Table has at least one existing row.
**Test Steps**:
1. Identify a record to delete (e.g., "Alden").
2. Store total row count containing data.
3. Click the "Delete" action icon for that row.
4. Verify the row disappears from the table.
**Test Data**: Target row containing "Alden".
**Expected Result**:
- The record immediately vanishes from the grid.
- The total data row count decreases by 1.
- Searching for "Alden" yields no results.

## TC-UI-TABLE-04: Search Functionality
**Tags**: `ui`, `regression`, `tables`
**Pre-conditions**: User is on the Web Tables page. Table contains default seed data.
**Test Steps**:
1. Enter a valid string in the "Search" text box (e.g., a First Name or a Department).
2. Verify the table rows are filtered.
3. Clear the search box and verify all rows return.
**Test Data**: Search Query: "Legal" (Department of Kierra).
**Expected Result**:
- While searching "Legal", only rows containing "Legal" are displayed.
- Rows not containing the query are hidden.
- After clearing, the original table state is restored.

## TC-UI-TABLE-05: Sorting Validation
**Tags**: `ui`, `regression`, `tables`
**Pre-conditions**: User is on the Web Tables page. Table contains multiple rows with varied data.
**Test Steps**:
1. Click the "Age" column header once. Extract row data and verify it is sorted in Ascending order.
2. Click the "Age" column header again. Extract row data and verify it is sorted in Descending order.
3. Repeat steps 1 & 2 for the "Salary" column.
**Test Data**: N/A
**Expected Result**:
- Clicking the column header toggles a sorting class (`-asc` / `-desc`).
- The rows render in mathematically correct ascending and descending order based on the selected numeric column.
