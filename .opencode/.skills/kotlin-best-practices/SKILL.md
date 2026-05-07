---
name: kotlin-best-practices
description: "Best practices for clean and maintainable code in Kotlin. Use this skill when writing Kotlin code that must be easy to read, maintain, and follow industry standards."
---

# Kotlin Best Practices — Clean Code & SOLID

## Variables y Nomenclatura

- Name classes with `PascalCase`: `MongoConnection`, `DatabaseManager`
- Name functions and variables with `camelCase`: `getDatabase()`, `connectionUri`
- Name constants with `UPPER_SNAKE_CASE`: `MAX_CONNECTIONS`, `DEFAULT_TIMEOUT`
- Name packages with lowercase: `org.iesra.tallermongo`
- Name collections in plural: `productos`, `clientes`, `prestamos`
- Name databases in snake_case: `tienda_online`, `biblioteca_digital`
- Use descriptive names: avoid `data`, `temp`, `info`; prefer `userData`, `sessionToken`

## Inmutabilidad

- Prefer `val` over `var` by default
- Use `var` only when mutation is required
- Use immutable data classes for DTOs and domain objects
- Use `listOf()`, `setOf()`, `mapOf()` instead of mutable variants when possible
- For collections that must be mutable, use `mutableListOf()` but expose as `List` externally

## Null Safety

- Avoid `null` where possible; prefer non-null types
- Never use `!!` operator; prefer safe calls (`?.`) and elvis operator (`?:`)
- Use `requireNotNull()` or `checkNotNull()` with meaningful messages for non-null validation
- Validate input explicitly and fail fast: `require(condition) { "Error message" }`
- Use `?.let { }` blocks for safe null handling

## Funciones y Estructura

- Keep functions small (max 20-30 lines) and focused on a single responsibility
- Use expression bodies for simple functions: `fun isValid(uri: String) = uri.startsWith("mongodb")`
- Prefer named arguments for functions with multiple parameters of the same type
- Avoid too many parameters; use data classes or parameter objects
- Use early returns to reduce nesting
- One level of abstraction per function

## Clases y Principios SOLID

### Single Responsibility Principle (SRP)
- Each class should have one reason to change
- Example: `DatabaseManager` handles databases, `CollectionManager` handles collections

### Open/Closed Principle (OCP)
- Open for extension, closed for modification
- Use interfaces and abstract classes

### Liskov Substitution Principle (LSP)
- Subtypes must be substitutable for their base types

### Interface Segregation Principle (ISP)
- Prefer small, specific interfaces over large general ones

### Dependency Inversion Principle (DIP)
- Depend on abstractions, not concretions
- Inject dependencies via constructor

## Errores y Excepciones

- Use exceptions for exceptional flows, not for normal control flow
- Throw specific exceptions with meaningful messages
- Handle errors explicitly with clear exception types
- Use `runCatching` when you need to capture failures without propagating
- Never swallow failures silently
- Validate at boundaries (public API entry points)

## Kotlin Específico

- Use `object` for singletons: `object MongoConnection { ... }`
- Use `companion object` for class-level constants and factory methods
- Use data classes for DTOs and value objects
- Use sealed classes for closed hierarchies (errors, states)
- Use `when` exhaustively with sealed classes/enums
- Prefer Kotlin collection operations: `map`, `filter`, `fold`, `reduce`, `associate`
- Use extension functions sparingly and keep them focused
- Use `apply`, `also`, `let`, `run`, `with` appropriately:
  - `apply`: configure an object after creation
  - `also`: perform side effects
  - `let`: null-safe transformation
  - `run`: execute block with context and return result

## Documentación con KDoc

Document all public functions and classes:

```kotlin
/**
 * Establishes a connection to MongoDB Atlas using the provided configuration.
 *
 * @param config The MongoDB connection configuration containing URI and database name
 * @return A connected MongoClient instance
 * @throws MongoSecurityException If authentication fails
 * @throws MongoTimeoutException If connection times out
 */
fun connect(config: MongoConfig): MongoClient
```

```kotlin
/**
 * Represents a product in the store inventory.
 *
 * @property id Unique identifier
 * @property name Product display name
 * @property price Price in euros
 * @property stock Current inventory count
 */
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int
)
```

## Patrones Recomendados

- **Repository Pattern**: Abstract data access behind an interface
- **Builder Pattern**: For complex object construction
- **Factory Pattern**: For creating instances based on configuration
- **Decorator Pattern**: For adding behavior without modifying original class
- **Adapter Pattern**: To decouple from external systems (MongoDB driver)

## Testing

- Use JUnit 5 for unit tests
- Test one thing per test function
- Use descriptive test names: `shouldReturnProductsSortedByPrice`
- Separate unit tests from integration tests
- Mock external dependencies (database, network)

## Errores Comunes a Evitar

1. Using `var` when `val` would work
2. Using `!!` instead of safe null handling
3. Creating God Classes with too many responsibilities
4. Deep nesting (more than 3 levels)
5. Magic numbers/strings without constants
6. Not handling exceptions explicitly
7. Exposing mutable collections
8. Using `ArrayList` instead of `listOf()`
9. Not documenting public APIs
10. Premature optimization
11. Using while(True) instead of proper loops
12. Not following naming conventions
13. Overusing extension functions for unrelated functionality
14. Not using data classes for simple DTOs