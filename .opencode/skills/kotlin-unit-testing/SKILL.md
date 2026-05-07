---
name: kotlin-unit-testing
description: "Best practices for unit tests in Kotlin workshop projects. Use this skill when creating or updating JUnit tests for CRUD logic, managers and services in this repository."
---

# Kotlin Unit Testing

## Main Goal

Write small, readable and reliable unit tests that validate workshop behavior without making the test suite harder than the production code.

## Testing Principles

- Use JUnit 5.
- Test one behavior per test.
- Name tests by expected behavior.
- Prefer deterministic tests with no hidden state.
- Separate unit tests from integration tests that require a real MongoDB instance.

## Test Naming

Prefer names such as:

- `shouldInsertBookWhenDataIsValid`
- `shouldReturnEmptyListWhenCollectionHasNoDocuments`
- `shouldRejectProductWithNegativePrice`

## Structure

Use Arrange / Act / Assert clearly:

1. Prepare the input and collaborators.
2. Execute the behavior under test.
3. Verify the result or side effects.

## Scope for This Project

- Cover validation logic.
- Cover mapping and repository/service behavior.
- Cover CRUD-related branches that can be tested without a live database.
- If an integration test is needed, label it clearly and isolate external setup.

## Good Practices

- Keep fixtures small.
- Reuse helper builders only when they reduce duplication.
- Assert meaningful outcomes, not implementation details.
- Mock external dependencies only when necessary.
- Prefer plain fakes or stubs over heavy mocking for simple cases.

## Things to Avoid

1. One test covering multiple unrelated behaviors.
2. Real network or database usage in unit tests.
3. Assertions that only check non-null without checking the real outcome.
4. Brittle tests tied to formatting or incidental implementation details.
5. Copying production logic into the test.
