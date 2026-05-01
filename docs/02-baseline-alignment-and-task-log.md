# Baseline Alignment and Task Log System

## 1. Purpose

This task prepares the existing Mini Budget app for future feature work.

Before adding larger systems such as weekly allowance, debt bosses, net worth tracking, extra income impact, goals, and gamification, the current app must be reviewed and aligned with the product vision.

The goal is not to rebuild the app.

The goal is to understand what already exists, document the current baseline, identify gaps, prepare safe extension points, and create a task logging system so every future change is traceable, tested, and aligned with the Mini Budget recovery vision.

## 2. Task Name

```txt
Task 02: Baseline Alignment and Task Log System
```

## 3. Required Reading

Before starting this task, the agent must read:

```txt
/docs/mini-budget/01-product-vision.md
/docs/mini-budget/10-codex-agent-instructions.md
/docs/mini-budget/02-baseline-alignment-and-task-log.md
```

The agent should not start implementing future features yet.

This task is an audit, preparation, documentation, and testing-alignment task.

## 4. Current App Foundation

The current Mini Budget app already has a working foundation.

Known existing areas:

- Overview.
- Expenses.
- Expense categories.
- Category summary.
- User details.
- Offline-first local storage.

The agent must inspect the codebase and confirm what currently exists.

The agent must not assume the implementation details.

The agent must document the real current state based on the code.

## 5. Desired Outcome

At the end of this task, the app should have:

1. A documented baseline of current functionality.
2. A clear mapping between existing features and the Mini Budget vision.
3. A list of gaps and preparation work needed for future features.
4. A task log system for documenting future changes.
5. A checklist system for future agent tasks.
6. A testing expectation for every new feature.
7. A clear way to track feature tests, unit tests, and regression checks.
8. No unnecessary rewrites.
9. No major new product features unless required for alignment.

## 6. Non-Goals

This task should not implement the full weekly allowance system.
This task should not implement debt bosses.
This task should not implement net worth tracking.
This task should not implement extra income tracking.
This task should not implement goals.
This task should not implement gamification.
This task should not redesign the entire UI.
This task should not introduce cloud sync.
This task should not change the offline-first foundation.

Only small structural improvements are allowed if they clearly prepare the app for future work.

## 7. Baseline Audit

The agent must create or update the following document:

```txt
/docs/mini-budget/current-app-baseline.md
```

This document should describe what the app currently does.

Suggested structure:

```md
# Current App Baseline

## Last Updated
YYYY-MM-DD

## App Summary
Short description of what the app currently does.

## Existing Screens
| Screen | Purpose | Current Status | Notes |
|---|---|---|---|
| Overview | ... | Working / Partial / Needs Review | ... |
| Expenses | ... | Working / Partial / Needs Review | ... |
| Expense Categories | ... | Working / Partial / Needs Review | ... |
| Category Summary | ... | Working / Partial / Needs Review | ... |
| User Details | ... | Working / Partial / Needs Review | ... |

## Existing Data Models
| Model / Table / Entity | Purpose | Key Fields | Notes |
|---|---|---|---|

## Existing Storage System
Describe local storage approach.

## Existing Calculations
Document current calculations, summaries, totals, or derived values.

## Existing Tests
Document existing unit, feature, UI, or integration tests.

## Known Issues
List known issues found during audit.

## Future Feature Readiness
Describe how ready the current app is for allowance, debt, net worth, goals, and gamification.
```

## 8. Vision Alignment Map

The agent must create or update:

```txt
/docs/mini-budget/vision-alignment-map.md
```

This document maps the current app to the product vision.

Suggested structure:

```md
# Vision Alignment Map

## Purpose
This document maps the current Mini Budget app to the long-term financial recovery vision.

## Alignment Table
| Existing Area | Supports Which Pillar? | Current Strength | Gap | Future Preparation Needed |
|---|---|---|---|---|
| Overview | Face Reality | Shows current summary | Needs recovery-focused interpretation | Add allowance, debt, net worth cards later |
| Expenses | Log Honestly | Allows expense tracking | Needs habit and pressure context | Prepare category pressure calculations |
| Categories | Face Reality | Groups spending | Needs health/pressure indicators | Add budget and weak-category metadata later |
| Summary | Face Reality | Shows spending totals | Needs trend and recovery interpretation | Add weekly/monthly comparison later |
| User Details | Personalization | Stores user context | Needs financial recovery profile | Add income cycle, allowance preferences later |

## Missing Product Pillars
List pillars that do not yet exist in the current app.

## Preparation Recommendations
List safe next steps before feature implementation.
```

## 9. Gap Register

The agent must create or update:

```txt
/docs/mini-budget/gap-register.md
```

This document tracks what is missing or weak.

Suggested structure:

```md
# Gap Register

## Gap Status Values
- Open
- Planned
- In Progress
- Done
- Deferred

## Gap Priority Values
- Critical
- High
- Medium
- Low

## Gaps
| ID | Area | Gap | Why It Matters | Priority | Status | Related Future Task |
|---|---|---|---|---|---|---|
| GAP-001 | Weekly Allowance | No weekly spending limit system | Needed for short-term discipline loop | Critical | Planned | Task 03 |
| GAP-002 | Debt | No debt boss/payment system | Needed for debt recovery journey | Critical | Planned | Task 04 |
```

The agent should add gaps based on the actual app state.

## 10. Task Log System

The agent must create or update:

```txt
/docs/mini-budget/task-log.md
```

This document tracks completed, active, and planned work.

Suggested structure:

```md
# Mini Budget Task Log

## Task Status Values
- Planned
- In Progress
- Blocked
- Completed
- Deferred

## Task Types
- Audit
- Feature
- Refactor
- Test
- Documentation
- Bugfix
- UX
- Data Model

## Tasks
| Task ID | Title | Type | Status | Started | Completed | Summary | Tests Added | Docs Updated |
|---|---|---|---|---|---|---|---|---|
| TASK-001 | Product Vision and Agent Instructions | Documentation | Completed | YYYY-MM-DD | YYYY-MM-DD | Created product direction docs | N/A | Yes |
| TASK-002 | Baseline Alignment and Task Log System | Audit/Documentation/Test Prep | Completed | YYYY-MM-DD | YYYY-MM-DD | Audit current app and create tracking system | Completed | Yes |
```

Each future agent task must update this file.

## 11. Feature Change Log

The agent must create or update:

```txt
/docs/mini-budget/change-log.md
```

This file records actual changes to the app.

Suggested structure:

```md
# Mini Budget Change Log

## Format
Each change should include:

- Date.
- Task ID.
- Files changed.
- Summary.
- User-facing impact.
- Data/storage impact.
- Tests added or updated.
- Documentation updated.

## Changes

### YYYY-MM-DD — TASK-002 — Baseline Alignment

#### Summary
Documented the current app baseline and prepared the project for future feature work.

#### Files Changed
- `/docs/mini-budget/current-app-baseline.md`
- `/docs/mini-budget/vision-alignment-map.md`
- `/docs/mini-budget/gap-register.md`
- `/docs/mini-budget/task-log.md`
- `/docs/mini-budget/change-log.md`
- `/docs/mini-budget/testing-checklist.md`

#### User-Facing Impact
No direct user-facing feature change.

#### Data/Storage Impact
None expected.

#### Tests
Documented existing test coverage and future testing expectations.

#### Documentation
Added baseline and task tracking documentation.
```

Every future change must be recorded here.

## 12. Testing Checklist System

The agent must create or update:

```txt
/docs/mini-budget/testing-checklist.md
```

This file defines how future changes should be tested.

Suggested structure:

```md
# Testing Checklist

## Purpose
Every future Mini Budget feature must include appropriate tests and regression checks.

## Test Types

### Unit Tests
Use for:
- Calculations.
- Helpers.
- Services.
- Data transformations.
- Validation rules.

### Feature Tests
Use for:
- Screen workflows.
- Creating records.
- Editing records.
- Deleting records.
- Persistence behavior.
- User-facing flows.

### Regression Checks
Use for:
- Existing overview still loads.
- Existing expense creation still works.
- Existing category summaries still work.
- Existing user details still work.
- Existing local storage still works.

## Required Checklist Per Task

Before completing any future task, confirm:

- [ ] Existing tests pass.
- [ ] New calculations have unit tests.
- [ ] New user flows have feature or UI tests where practical.
- [ ] Existing core flows were not broken.
- [ ] Empty states were checked.
- [ ] Edit and delete flows were checked where relevant.
- [ ] Local storage behavior was checked.
- [ ] Documentation was updated.
- [ ] Change log was updated.
- [ ] Task log was updated.

## Core Regression Checklist

- [ ] App launches successfully.
- [ ] Overview screen loads.
- [ ] Expense list loads.
- [ ] Expense can be created.
- [ ] Expense can be edited if currently supported.
- [ ] Expense can be deleted if currently supported.
- [ ] Expense category list loads.
- [ ] Category summary loads.
- [ ] User details screen loads.
- [ ] Existing locally stored data remains readable.
- [ ] No cloud/network access is required.
```

The agent must adapt test names and commands to the actual project stack.

## 13. Test Audit

The agent must inspect the current test setup and document it in:

```txt
/docs/mini-budget/current-app-baseline.md
```

The test audit should include:

- Testing framework used.
- Test folders.
- Existing tests.
- How to run tests.
- Missing obvious test coverage.
- Whether tests currently pass.

If no tests exist, document that honestly and create a recommended starting point.

Do not invent tests that do not exist.

## 14. Required Test Preparation

If the app already has a test system:

- Confirm the current tests run.
- Add or update baseline tests only if low-risk and aligned with the existing testing approach.
- Do not rewrite the test framework.

If the app has no tests:

- Document that no tests currently exist.
- Identify the correct test framework for the current stack.
- Create a minimal test plan.
- Add only a very small baseline test if safe and appropriate.

The preferred baseline tests are:

- App starts or main screen renders.
- Expense calculation helper works if such helper exists.
- Category summary calculation works if such helper exists.

## 15. Documentation Standards for Future Tasks

Every future task must update these files where relevant:

```txt
/docs/mini-budget/task-log.md
/docs/mini-budget/change-log.md
/docs/mini-budget/gap-register.md
/docs/mini-budget/testing-checklist.md
```

Feature-specific docs must also be updated when a feature is implemented.

Example:

When implementing debt bosses, update:

```txt
/docs/mini-budget/04-debt-boss-system.md
/docs/mini-budget/task-log.md
/docs/mini-budget/change-log.md
/docs/mini-budget/gap-register.md
/docs/mini-budget/testing-checklist.md
```

## 16. Implementation Guardrails

The agent must follow these rules:

- Do not rewrite working app foundations unnecessarily.
- Do not change storage schema without documenting it.
- Do not remove existing functionality.
- Do not introduce online-only behavior.
- Do not add cloud dependencies.
- Do not implement major future features during this task.
- Do not leave undocumented changes.
- Do not skip tests for calculation-heavy work.
- Do not use harsh or shame-based user-facing text.

## 17. Suggested Agent Workflow

The agent should follow this order:

1. Read required docs.
2. Inspect project structure.
3. Identify app stack, storage approach, and test setup.
4. Audit current screens.
5. Audit current data models.
6. Audit current calculations.
7. Audit current tests.
8. Create/update baseline document.
9. Create/update vision alignment map.
10. Create/update gap register.
11. Create/update task log.
12. Create/update change log.
13. Create/update testing checklist.
14. Run existing tests if available.
15. Add minimal safe baseline tests only if appropriate.
16. Update documentation with test results.
17. Summarize completed work.

## 18. Deliverables

At minimum, this task should produce or update:

```txt
/docs/mini-budget/current-app-baseline.md
/docs/mini-budget/vision-alignment-map.md
/docs/mini-budget/gap-register.md
/docs/mini-budget/task-log.md
/docs/mini-budget/change-log.md
/docs/mini-budget/testing-checklist.md
```

Optional if useful:

```txt
/docs/mini-budget/test-plan.md
/docs/mini-budget/architecture-notes.md
/docs/mini-budget/storage-notes.md
```

## 19. Completion Criteria

Task 02 is complete when:

- The current app baseline is documented.
- Existing features are mapped to the product vision.
- Gaps are registered.
- Future tasks have a task log system.
- Change documentation rules exist.
- Testing expectations are documented.
- Existing tests have been audited.
- Any safe baseline tests are added or recommended.
- No major future feature was accidentally implemented.
- Offline-first behavior remains unchanged.

## 20. Final Agent Response Format

When the agent finishes Task 02, the response should include:

```md
## Task 02 Complete

### What I audited
- ...

### What I documented
- ...

### Gaps found
- ...

### Tests
- Existing tests: pass/fail/not present
- New tests added: ...
- Test command used: ...

### Files changed
- ...

### Recommended next task
- ...
```

If the agent cannot complete something, it must state what was not completed and why.

## 21. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

Task 02 delivered the documentation and tracking system needed for the rest of the v1.0 foundation:

- Current app baseline.
- Vision alignment map.
- Gap register.
- Task log.
- Change log.
- Testing checklist.
- Agent documentation workflow.

Remaining documentation and testing improvements are now tracked in:

- `TASK-012` for test hardening and migration safety.
- `TASK-025` for dashboard/navigation polish and feature discoverability.

Bugs found by testing should be logged as bugfix work and do not reopen Task 02.
