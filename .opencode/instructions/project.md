# Project Instructions

## Working Style

- Read the existing code and documentation before making changes.
- Prefer the smallest correct change over broad rewrites.
- Keep examples and code easy to understand for students.
- Preserve consistency with the workshop structure and naming already used in the repository.

## Code Changes

- Use Kotlin conventions consistently.
- Prefer clear names over short names.
- Avoid unnecessary abstractions.
- Keep public APIs documented when relevant.
- Do not hardcode credentials, tokens or environment-specific secrets.

## MongoDB Guidance

- Use KMongo as the primary MongoDB library unless there is a clear reason not to.
- Prefer typed collections and Kotlin data classes when they improve readability.
- Keep database examples focused on CRUD learning goals.
- Separate unit tests from tests that require a real MongoDB instance.

## Documentation Guidance

- Keep explanations progressive and learner-friendly.
- Explain what a section teaches before showing the code.
- Keep Markdown structure consistent across workshop materials.
- Clearly separate exercises from solutions.

## Testing Guidance

- New unit tests should use Kotest with `DescribeSpec`.
- Use MockK for mocking and interaction verification.
- Prefer deterministic tests with small fixtures.

## Safety

- Do not delete or overwrite unrelated user changes.
- Do not introduce generated noise or temporary files into the repository unless required.
- Ask before destructive operations or irreversible git actions.
