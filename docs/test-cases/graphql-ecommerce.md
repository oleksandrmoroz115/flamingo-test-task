# GraphQL Ecommerce API Test Cases

**Schema**: Ecommerce
**Base URL**: `https://api-eu-central-1-shared-euc1-02.hygraph.com/v2/cl8tc1sxb7gqk01uk7y1m7zto/master` (Or configured Hygraph Ecommerce endpoint)

## TC-GQL-01: Positive - Query List with Pagination/Limit
**Tags**: `api`, `graphql`, `positive`
**Pre-conditions**: Ecommerce GraphQL endpoint is accessible.
**Test Steps**:
1. Send a POST request to the GraphQL endpoint containing a query for products with `first` (limit) and `skip` arguments.
**Test Data**:
```graphql
query GetProducts($first: Int!, $skip: Int!) {
  products(first: $first, skip: $skip) {
    id
    name
    price
  }
}
```
Variables: `{"first": 2, "skip": 0}`
**Expected Result**:
- HTTP Status 200 OK.
- Response body contains `data.products` array.
- The array length equals 2 (the limit).

## TC-GQL-02: Positive - Query Single Entity by ID using Variables
**Tags**: `api`, `graphql`, `positive`
**Pre-conditions**: Known valid Product ID exists in the database.
**Test Steps**:
1. Send a POST request to the GraphQL endpoint with a query for a single product.
2. Provide the product ID strictly via GraphQL variables (no string interpolation in query).
**Test Data**:
```graphql
query GetProductById($id: ID!) {
  product(where: { id: $id }) {
    id
    name
    description
  }
}
```
Variables: `{"id": "valid_product_id"}` (dynamic from setup)
**Expected Result**:
- HTTP Status 200 OK.
- Response body contains `data.product`.
- The returned `id` matches the variable provided.

## TC-GQL-03: Positive - Query with Fragments and Nested Fields
**Tags**: `api`, `graphql`, `positive`
**Pre-conditions**: None.
**Test Steps**:
1. Send a POST request to the GraphQL endpoint utilizing a fragment for product details and fetching nested category data.
**Test Data**:
```graphql
fragment ProductDetails on Product {
  id
  name
  price
}

query GetProductsWithCategories {
  products(first: 1) {
    ...ProductDetails
    categories {
      id
      name
    }
  }
}
```
**Expected Result**:
- HTTP Status 200 OK.
- Response includes `data.products` array.
- Each product item contains the fragment fields (`id`, `name`, `price`) and the nested `categories` array.

## TC-GQL-04: Negative - Query with Non-Existent ID
**Tags**: `api`, `graphql`, `negative`
**Pre-conditions**: None.
**Test Steps**:
1. Send a POST request querying a single product with a non-existent ID.
**Test Data**:
```graphql
query GetProductById($id: ID!) {
  product(where: { id: $id }) {
    id
    name
  }
}
```
Variables: `{"id": "invalid_non_existent_id_999"}`
**Expected Result**:
- HTTP Status 200 OK (GraphQL convention).
- Response body contains `data.product: null`. (No errors array, just null data for the entity).

## TC-GQL-05: Negative - Malformed Query (Syntax Error)
**Tags**: `api`, `graphql`, `negative`
**Pre-conditions**: None.
**Test Steps**:
1. Send a POST request with an invalid syntax (e.g., missing brace).
**Test Data**:
```graphql
query {
  products(first: 1) {
    id
    name
```
**Expected Result**:
- HTTP Status 400 Bad Request (or 200 depending on server, verify implementation).
- Response body contains an `errors` array.
- `errors[0].message` describes a syntax error.
- `data` object is absent or null.

## TC-GQL-06: Negative - Query Requesting Non-Existent Field
**Tags**: `api`, `graphql`, `negative`
**Pre-conditions**: None.
**Test Steps**:
1. Send a POST request querying a field that does not exist in the schema.
**Test Data**:
```graphql
query {
  products(first: 1) {
    id
    nonExistentFakeFieldXYZ
  }
}
```
**Expected Result**:
- HTTP Status 400 Bad Request.
- Response body contains an `errors` array.
- `errors[0].message` indicates the validation error (cannot query field).
