# Restful Booker API Test Cases

**Base URL**: `https://restful-booker.herokuapp.com`

## TC-REST-01: Authentication (Token Generation)
**Tags**: `api`, `smoke`, `auth`
**Pre-conditions**: API is accessible.
**Test Steps**:
1. Send POST request to `/auth` with valid credentials.
**Test Data**:
```json
{
  "username": "{{admin_username}}",
  "password": "{{admin_password}}"
}
```
*(Credentials injected from environment configuration)*
**Expected Result**:
- HTTP Status Code is 200 OK.
- Response body contains a non-empty `token` string.

## TC-REST-02: Create, Retrieve, Update, and Delete Booking (Full CRUD Cycle)
**Tags**: `api`, `regression`, `crud`
**Pre-conditions**: Valid authentication token generated. API is accessible.
**Test Steps**:
1. **Create**: Send POST request to `/booking` with valid booking payload. Extract `bookingid` from response.
2. **Retrieve**: Send GET request to `/booking/{bookingid}`.
3. **Update**: Send PUT request to `/booking/{bookingid}` with the auth token in headers (`Cookie: token={token}`) and an updated payload.
4. **Retrieve Updated**: Send GET request to `/booking/{bookingid}`.
5. **Delete**: Send DELETE request to `/booking/{bookingid}` with auth token.
6. **Verify Deletion**: Send GET request to `/booking/{bookingid}`.
**Test Data**:
*Create Payload*:
```json
{
    "firstname" : "Jim",
    "lastname" : "Brown",
    "totalprice" : 111,
    "depositpaid" : true,
    "bookingdates" : {
        "checkin" : "2024-01-01",
        "checkout" : "2024-01-10"
    },
    "additionalneeds" : "Breakfast"
}
```
*Update Payload*: Change `firstname` to "James" and `totalprice` to 150.
**Expected Result**:
- Step 1: HTTP 200 OK. Response matches payload schema. `bookingid` is present.
- Step 2: HTTP 200 OK. Response body matches the created payload data.
- Step 3: HTTP 200 OK. Response body reflects the updated values ("James", 150).
- Step 4: HTTP 200 OK. Response body reflects the updated values.
- Step 5: HTTP 201 Created (as per documentation).
- Step 6: HTTP 404 Not Found.

## TC-REST-03: Negative - Update Booking Without Auth Token
**Tags**: `api`, `negative`, `security`
**Pre-conditions**: A booking exists (or is created as part of the test setup).
**Test Steps**:
1. Send PUT request to `/booking/{bookingid}` with an updated payload but NO authorization token header.
**Test Data**: Valid update payload, `bookingid` of an existing record.
**Expected Result**:
- HTTP Status Code is 403 Forbidden.

## TC-REST-04: Negative - Delete Booking Without Auth Token
**Tags**: `api`, `negative`, `security`
**Pre-conditions**: A booking exists (or is created as part of the test setup).
**Test Steps**:
1. Send DELETE request to `/booking/{bookingid}` with NO authorization token header.
**Test Data**: `bookingid` of an existing record.
**Expected Result**:
- HTTP Status Code is 403 Forbidden.

## TC-REST-05: Negative - Retrieve Non-Existent Booking
**Tags**: `api`, `negative`
**Pre-conditions**: None.
**Test Steps**:
1. Send GET request to `/booking/{invalid_id}`.
**Test Data**: `invalid_id` = 99999999
**Expected Result**:
- HTTP Status Code is 404 Not Found.
