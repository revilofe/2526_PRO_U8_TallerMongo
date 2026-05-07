---
name: workshop-markdown-structure
description: "Markdown conventions for workshop-style technical documentation. Use this skill when structuring README files, guides, exercises and reference solutions in this project."
---

# Workshop Markdown Structure

## What I do

- Define the expected Markdown structure for workshop-style documentation in this project.
- Standardize headings, code blocks, exercise layout and general formatting for learner-facing materials.
- Help keep documentation easy to scan, consistent and reusable across guides and solutions.

## When to use me

- Use me when creating or reorganizing README files, guides, exercises or solution documents.
- Use me when a Markdown document needs clearer structure, more consistent formatting or a workshop-oriented layout.

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

## Consistency Rules

- Use spanish of spain for all documentation, exercises and solutions to maintain consistency with the target audience.
- Keep naming aligned with the Kotlin codebase.
- Reuse the same collection and model names throughout the docs.
- Keep command examples minimal and realistic.
- Do not mix unrelated topics in the same section.
- Use the same formatting style for all exercises and solutions.
- When referring to files, classes or variables, ensure they are defined in the documentation or codebase.
- When introducing a new concept, provide a brief definition or link to a resource for further reading.
