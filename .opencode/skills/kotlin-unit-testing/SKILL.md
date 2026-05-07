---
name: kotlin-unit-testing
description: "Best practices for unit tests in Kotlin workshop projects. Use this skill when creating or updating tests for CRUD logic, managers and services in this repository. Tests must use Kotest and MockK."
---

# Kotlin Unit Testing

## What I do

- Define how to write small, reliable and readable unit tests for this Kotlin workshop.
- Standardize test structure around Kotest, `DescribeSpec`, MockK and behavior-focused assertions.
- Clarify what belongs in unit tests and what should stay out of them.

## When to use me

- Use me when creating or updating unit tests for managers, services, validators or CRUD logic.
- Use me when adapting tests to the project's expected stack: Kotest plus MockK.

## Main Goal

Write small, readable and reliable unit tests that validate workshop behavior without making the test suite harder than the production code.

This project must use these tools for unit testing:

- `Kotest` for test structure and assertions.
- `MockK` for mocks, stubs and interaction verification.

Do not default to plain JUnit-style tests or Mockito when working under this skill.
Use a consistent Kotest style so tests also work as executable documentation.

## Testing Principles

- Use `Kotest` as the primary test framework.
- Use `MockK` as the primary mocking library.
- Use `DescribeSpec` as the mandatory spec style for new tests in this repository.
- Test one behavior per test.
- Name tests by expected behavior.
- Prefer deterministic tests with no hidden state.
- Separate unit tests from integration tests that require a real MongoDB instance.

## Mandatory Tooling

When adding or updating test code, follow these rules:

1. Use `DescribeSpec` as the default and required Kotest style.
2. Use Kotest matchers for assertions.
3. Use MockK for mocking collaborators.
4. Do not introduce Mockito.
5. Do not write new tests using raw JUnit assertions unless there is a very specific compatibility reason.

If an old test already uses another style, prefer leaving it as-is unless you are already refactoring that test file. For any new test file, use `DescribeSpec`.

## Preferred Dependencies

Use dependencies equivalent to these when documenting or implementing tests:

```kotlin
testImplementation("io.kotest:kotest-runner-junit5-jvm:<version>")
testImplementation("io.kotest:kotest-assertions-core:<version>")
testImplementation("io.kotest:kotest-property:<version>")
testImplementation("io.mockk:mockk:<version>")
```

If the exact version is unknown, do not invent one blindly. Reuse the version already present in the project or leave a clear placeholder to be resolved.

## Style and Naming

Follow the structure promoted in the Kotest guide:

- `describe("...")` for the unit or behavior under test.
- `it("...")` for the concrete expected behavior.
- Use nested `describe` blocks only when they improve clarity.
- Prefer human-readable behavior descriptions over method-name mirroring.

Example style:

```kotlin
class ProductServiceTest : DescribeSpec({
    describe("ProductService") {
        it("should reject a product with negative price") {
            // test body
        }
    }
})
```

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

Preferred Kotest structure:

```kotlin
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ProductServiceTest : DescribeSpec({
    describe("ProductService") {
        it("should reject a product with negative price") {
            val repository = mockk<ProductRepository>()
            val service = ProductService(repository)

            shouldThrow<IllegalArgumentException> {
                service.createProduct(Product(nombre = "Teclado", precio = -1.0, stock = 3))
            }
        }
    }
})
```

## Kotest Guidance

- Use Kotest matchers such as `shouldBe`, `shouldContain`, `shouldBeTrue`, `shouldBeFalse` and type-specific matchers where they improve readability.
- Use `shouldThrow<T> { ... }` for exception testing instead of manual `try/catch`.
- Use lifecycle hooks such as `beforeTest` and `afterTest` only when they reduce duplication without hiding the setup.
- Use data-driven testing when the same behavior must be validated with several inputs.
- Use non-deterministic helpers like `eventually` only for asynchronous behavior that genuinely needs them.

## Scope for This Project

- Cover validation logic.
- Cover mapping and repository/service behavior.
- Cover CRUD-related branches that can be tested without a live database.
- If an integration test is needed, label it clearly and isolate external setup.

## Good Practices

- Keep fixtures small.
- Reuse helper builders only when they reduce duplication.
- Assert meaningful outcomes with Kotest matchers, not implementation details.
- Mock external dependencies only when necessary.
- Prefer plain fakes or stubs over heavy mocking for simple cases.
- Use MockK verification only for behavior that matters to the contract.
- Use `every { ... } returns ...` and `verify { ... }` clearly and minimally.

## MockK Guidance

- Use `mockk<T>()` for collaborators.
- Use `every { ... } returns ...` for stubbing.
- Use `every { dependency.doSomething(any()) } just runs` for `Unit` functions.
- Use `verify(exactly = 1) { ... }` when interaction count is relevant.
- Use `clearMocks(...)` only when a shared mock is truly needed.
- Prefer fresh mocks per test over shared mutable setup.
- Use `slot<T>()` or mutable lists when argument capture is part of the behavior under test.
- Use `spyk(...)` only when partial real behavior is genuinely helpful and the resulting test remains easy to understand.
- Use `callOriginal()` only in targeted cases where mixing real and simulated behavior improves the test.
- Use relaxed mocks sparingly; they are acceptable when the default values reduce noise without hiding relevant behavior.

Example:

```kotlin
val repository = mockk<BookRepository>()
every { repository.findByTitle("Dune") } returns Book("Dune", "Frank Herbert")

val result = service.findByTitle("Dune")

result?.title shouldBe "Dune"
verify(exactly = 1) { repository.findByTitle("Dune") }
```

Argument capture example:

```kotlin
val slot = slot<Book>()
every { repository.save(capture(slot)) } returns savedBook

service.register(book)

slot.captured.title shouldBe "Dune"
```

## Things to Avoid

1. One test covering multiple unrelated behaviors.
2. Real network or database usage in unit tests.
3. Assertions that only check non-null without checking the real outcome.
4. Brittle tests tied to formatting or incidental implementation details.
5. Copying production logic into the test.
6. Introducing Mockito or mixing multiple mocking libraries.
7. Writing new tests with JUnit assertions when Kotest matchers can express the intent more clearly.
8. Over-verifying every mock interaction when the result assertion is enough.
9. Mixing several Kotest spec styles in new test files without a clear reason.
10. Using deep shared setup that makes the `describe`/`it` blocks harder to read.
