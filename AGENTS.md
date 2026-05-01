# AGENTS.md

## Required Project Reading

Before starting any task in this repository, read the relevant docs for the assigned work.

Always start with:

1. `docs/01-product-vision.md`
2. `docs/10-codex-agent-instructions.md`

Then read the task or feature document that matches the user request.

Current task docs:

- Baseline alignment / task log work: `docs/02-baseline-alignment-and-task-log.md`
- Weekly allowance work: `docs/03-weekly-allowance-system.md`
- Debt boss work: `docs/04-debt-boss-system.md`
- Style direction / revamp work: `docs/STYLE_REVAMP_PLAN.md`
- General improvement history: `docs/BUDGET_APP_IMPROVEMENT_PLAN.md`

Some docs may refer to paths under `docs/mini-budget/`. In this repository, use the matching file directly under `docs/` unless the docs are later moved into that subfolder.

## Task 02 Rule

For Task 02, read `docs/02-baseline-alignment-and-task-log.md` before making changes.

Task 02 is an audit, preparation, documentation, and testing-alignment task. Do not use Task 02 as permission to build future feature systems such as weekly allowance, debt bosses, net worth, extra income, goals, or gamification.

When working on Task 02, create or update the baseline and tracking docs requested by the task document.

## Feature-Specific Rule

For any feature task, read the matching feature spec before implementation.

If the matching feature spec does not exist, write a short implementation plan in `docs/` before changing code.

## Product Rules

Mini Budget is an offline-first financial recovery companion.

Preserve these defaults:

- Keep the app local-first and usable offline.
- Do not add account creation, bank sync, cloud-only analysis, or required internet access unless explicitly requested.
- Keep user-facing text practical, encouraging, and non-judgmental.
- Prioritize financial correctness and the recovery loop over visual polish.
- Add tests or update existing tests for meaningful behavior changes.

## Change Tracking

For future tasks, update the relevant documentation when behavior, data models, screens, or task status changes.

Expected tracking docs from Task 02 include:

- `docs/current-app-baseline.md`
- `docs/vision-alignment-map.md`
- `docs/gap-register.md`
- `docs/task-log.md`
- `docs/change-log.md`

If these files are later moved into `docs/mini-budget/`, use the moved location consistently.
