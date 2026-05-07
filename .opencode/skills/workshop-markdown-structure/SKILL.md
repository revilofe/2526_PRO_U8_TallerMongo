---
name: workshop-markdown-structure
description: "Markdown conventions for workshop-style technical documentation. Use this skill when structuring README files, guides, exercises and reference solutions in this project."
---

# Workshop Markdown Structure

## Document Structure

Use a predictable structure so students can scan and follow each guide easily:

1. Title
2. Objective
3. Requirements
4. Step-by-step explanation
5. Code examples
6. Exercises
7. Solutions
8. Summary or next step

## Markdown Rules

- Use hierarchical headings in order without skipping levels.
- Use fenced code blocks with a language tag.
- Use `kotlin` for Kotlin code and `text` for environment variables or shell output.
- Use tables only when they improve comparison or quick lookup.
- Use blockquotes for warnings, tips or reminders.
- Keep sections compact and focused.

## Code Example Rules

- Every code block should be understandable in isolation.
- Include imports when they help the learner run the example.
- Prefer complete examples over fragmented snippets unless the fragment is the teaching focus.
- If code is partial, say so explicitly.

## Exercise Formatting

Preferred format:

```md
## Exercise: Insert a book

### Goal
Describe what the learner has to achieve.

### Steps
1. ...
2. ...

### Expected result
...

### Solution
```kotlin
// reference solution
```
```

## Consistency Rules

- Keep naming aligned with the Kotlin codebase.
- Reuse the same collection and model names throughout the docs.
- Keep command examples minimal and realistic.
- Do not mix unrelated topics in the same section.
