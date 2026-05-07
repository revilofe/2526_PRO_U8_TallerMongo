---
name: professional-commits
description: "Creates professional, well-structured commit messages following Conventional Commits specification. Use when committing code changes, creating pull requests, or organizing git history."
---

# Professional Commits

## Conventional Commits Specification

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

## Commit Types

| Type | Description |
|------|-------------|
| `feat` | New feature for the user |
| `fix` | Bug fix for the user |
| `docs` | Documentation only changes |
| `style` | Formatting, missing semicolons, etc. |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement |
| `test` | Adding or updating tests |
| `build` | Changes that affect the build system or dependencies |
| `ci` | Changes to CI configuration files and scripts |
| `chore` | Other changes that don't modify src or test files |
| `revert` | Reverts a previous commit |

## Scope (Optional)

Use the module, feature, or component affected:

- `feat(auth)`: Authentication feature
- `fix(database)`: Database bug fix
- `docs(api)`: API documentation

## Message Rules

### Description
- Use imperative mood: "add" not "added" or "adds"
- Maximum 50 characters
- No period at the end
- Lowercase after colon

### Body (Optional)
- Separate from subject with blank line
- Wrap at 72 characters
- Explain **what** and **why**, not how
- Use bullet points with `-` for multiple lines

### Footer (Optional)
- Reference issues: `Closes #123`, `Fixes #456`
- Mention breaking changes: `BREAKING CHANGE: ...`

## Examples

### Simple commit
```
feat: add user authentication with JWT tokens
```

### With scope
```
feat(auth): implement OAuth2 login with Google
```

### With body and footer
```
feat(api): add REST endpoints for product management

- GET /products - list all products
- POST /products - create new product
- PUT /products/{id} - update existing product

Closes #42
```

### Breaking change
```
feat!: migrate from REST to GraphQL API

BREAKING CHANGE: All REST endpoints are now deprecated.
Migration guide: /docs/migration.md
```

## Workflow for Creating Commits

1. **Check status**: Run `git status` to see pending changes
2. **Review diffs**: Run `git diff` to understand what changed
3. **Group logically**: If multiple concerns, consider separate commits
4. **Draft message**: Apply Conventional Commits format
5. **Validate**: Ensure message follows all rules above
6. **Stage and commit**: Use `git add` and `git commit`

## Anti-Patterns to Avoid

- ❌ `Fixed bug` → ✅ `fix: resolve null pointer in user service`
- ❌ `WIP` → ✅ `feat(search): add fuzzy search implementation`
- ❌ `Changes` → ✅ `refactor(auth): extract token validation to helper`
- ❌ `asdfgh` → ✅ `fix(api): handle timeout gracefully`
- ❌ Starting with capital letter
- ❌ Ending with period
- ❌ Exceeding 50 characters in subject