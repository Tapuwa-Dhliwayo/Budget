# Mini Budget Post-1.0 Roadmap

## 1. Purpose

Mini Budget v1.0 is now treated as the foundation release for the local-first financial recovery app.

Tasks 01 through 09 are closed for v1.0, excluding bugs found by testing. Remaining feature ideas and improvements are no longer tracked as unfinished work inside those original task files. They are collected here as post-v1.0 tasks starting at `TASK-012`.

Each post-v1.0 task must be small enough for one agent run to complete, document, and test.

## 2. V1.0 Foundation Closed

| Task | Area | V1.0 Result |
|---|---|---|
| TASK-001 | Product vision and agent rules | Added product direction and agent operating rules. |
| TASK-002 | Baseline alignment | Added baseline docs, task tracking, gap tracking, change log, and testing checklist. |
| TASK-003 | Weekly allowance | Added manual weekly allowance, non-recurring weekly spend, pressure detection, category limits, weekly review, action completion, history, and trend summary. |
| TASK-004 | Debt Boss | Added debt boss records, payment/action history, HP/progress, simple projection, monthly battle summary, and debt screen. |
| TASK-005 | Net worth | Added asset tracking, current net worth calculation, snapshots, movement guidance, and net worth screen. |
| TASK-006 | Extra income | Added extra income tracking, allocation buckets, monthly impact summary, optional debt context, and extra income screen. |
| TASK-007 | Goals | Added goals, contributions, progress, required monthly contribution, feasibility, health classification, and goals screen. |
| TASK-008 | Gamification | Added recovery XP, levels, titles, badges, event history, expense XP wiring, and recovery progress screen. |
| TASK-009 | Offline-first and privacy | Added data ownership screen, local backup creation, backup freshness, local-first privacy messaging, and backup XP hook. |

## 3. Task Rules

Every post-v1.0 task must:

- Read `AGENTS.md` and the relevant docs before implementation.
- Keep the app local-first and account-free.
- Preserve existing user data and add safe migrations for schema changes.
- Update the task log, change log, gap register, and relevant feature spec.
- Add or update tests for the behavior changed.
- Run the relevant test command before closing the task.
- Leave bugs discovered during testing either fixed in the same run or logged clearly as bugfix work.

## 4. Post-1.0 Tasks

## TASK-012: Test Hardening and Migration Safety

Goal: make the v1.0 foundation safer to extend.

Scope:

- Enable Room schema export or add an equivalent migration review process.
- Add migration tests covering the current database path through version 11.
- Add focused repository tests for important aggregation boundaries.
- Add at least one navigation or screen smoke test strategy for core flows if practical.
- Document the expected test commands.

Tests:

- Migration tests for existing schema versions.
- Existing `./gradlew test`.
- Instrumentation command documented if an emulator/device is required.

## TASK-013: Recurring Expense Rules and Monthly Commitment Planning

Goal: turn recurring expenses from a marker into useful monthly commitment planning.

Scope:

- Add recurrence rule fields such as frequency, due day, next due date, paused state, and optional end date.
- Show recurring expenses as monthly commitments without counting them in weekly flexible spending.
- Add a clean monthly recurring commitments section.
- Add safe migration and backward compatibility for existing `isRecurring` data.

Tests:

- Recurrence rule storage and migration.
- Weekly spend excludes recurring commitments.
- Monthly commitment totals include recurring commitments.

## TASK-014: Weekly Allowance Intelligence and Review Upgrade

Goal: deepen the short-term recovery loop.

Scope:

- Add derived weekly allowance suggestions using income/profile, recurring commitments, debt minimums, and goal targets where data exists.
- Add smarter category limit suggestions.
- Improve weekly review prompts and summary insight generation.
- Improve dashboard and review summary states for recovery after pressure.

Tests:

- Derived allowance calculations.
- Category suggestion logic.
- Weekly review summary states.

## TASK-015: Debt Boss Management, History, and Strategy Engine

Goal: make debt recovery more complete and actionable.

Scope:

- Add edit, archive, delete, and defeated-state flows for debts.
- Improve payment/action history detail.
- Add strategy comparison for snowball, avalanche, and hybrid.
- Add extra payment simulator and amortized projection where interest data is available.
- Add debt milestone and celebration hooks.

Tests:

- Debt edit/archive/delete behavior.
- Strategy sorting and projection calculations.
- Payment history and defeated-state transitions.

## TASK-016: Net Worth Trends and Snapshot Detail

Goal: make long-term progress visible and explainable.

Scope:

- Add snapshot detail rows for assets and debts.
- Add richer trend visualization.
- Add dashboard net worth summary card.
- Add milestone and movement reason messaging.

Tests:

- Snapshot detail persistence.
- Historical trend calculations.
- Dashboard net worth state.

## TASK-017: Extra Income Allocation and Recovery Links

Goal: connect extra income directly to recovery outcomes.

Scope:

- Support split allocations across debt, goals, savings, buffer, living, and personal use.
- Allow extra income to create or link to debt payments and goal contributions.
- Add source history and trend summary.
- Add dashboard extra income impact card.

Tests:

- Split allocation totals.
- Debt payment and goal contribution linking.
- Monthly impact summary with mixed allocations.

## TASK-018: Goals Lifecycle and What-If Planning

Goal: make goals adaptable without weakening the recovery plan.

Scope:

- Add edit, pause, cancel, defer, and complete flows.
- Link extra income to goal contributions.
- Add what-if planning for target date, target amount, contribution amount, and extra income.
- Add recommendation messaging for risky or tight goals.

Tests:

- Goal lifecycle state transitions.
- Contribution and extra-income linking.
- What-if feasibility calculations.

## TASK-019: Recovery XP Integration and Monthly Battle Report

Goal: wire gamification across all meaningful recovery actions.

Scope:

- Award XP for weekly reviews, recovery action completion, debt payments, debt defeats, net worth snapshots, extra income allocations, goal milestones, and backups.
- Add duplicate-prevention rules where needed.
- Add monthly recovery battle report.
- Add achievement gallery detail.

Tests:

- XP event creation across feature flows.
- Duplicate prevention.
- Monthly report aggregation.

## TASK-020: Backup Restore, Export Destinations, and Verification

Goal: complete the user-owned data safety loop.

Scope:

- Add restore/import from app backup with explicit overwrite confirmation.
- Add backup verification before restore where practical.
- Add user-selected export/import destination support.
- Add backup health guidance.

Tests:

- Backup file creation and verification.
- Restore/import success path.
- Invalid or cancelled import handling.

## TASK-021: Security and Privacy Controls

Goal: protect sensitive local financial data without requiring an account.

Scope:

- Add optional PIN lock.
- Add biometric unlock where available.
- Add auto-lock setting.
- Add hide-sensitive-values behavior for the app switcher if supported.
- Add clear local-only privacy controls.

Tests:

- PIN setup and validation.
- Lock/unlock flow.
- Settings persistence.

## TASK-022: Reports, Analytics Periods, and Readable Exports

Goal: make financial history easier to review and share privately.

Scope:

- Add period/month selection to analytics.
- Add readable CSV exports for expenses, debts, goals, extra income, and net worth where practical.
- Add monthly recovery report export.
- Improve analytics charts and empty states.

Tests:

- Period filtering.
- Export content.
- Empty and partial-data report states.

## TASK-023: Profile and Planning Settings

Goal: give planning features the personal context they need.

Scope:

- Add income cycle, payday, allowance preference, backup preference, and recovery profile fields.
- Keep all profile data local.
- Surface settings from the More hub.
- Use profile fields in later allowance and planning calculations.

Tests:

- Profile setting persistence.
- Validation for date/cycle fields.
- Backward compatibility for existing profiles.

## TASK-024: Expense and Category Depth

Goal: strengthen the everyday logging and category model.

Scope:

- Add receipt photo capture/display for the existing `photoPath` field.
- Add category color/icon editing improvements.
- Add category health metadata and guidance hooks.
- Improve category-specific recovery suggestions.

Tests:

- Expense photo path flow.
- Category edit persistence.
- Category metadata calculations.

## TASK-025: Dashboard Integration and Navigation Polish

Goal: keep the app scalable as more features are added.

Scope:

- Add compact dashboard cards for debt, net worth, extra income, goals, backup health, and recovery XP where useful.
- Refine More hub grouping as feature count grows.
- Review top-level navigation for discoverability without expanding the bottom bar.
- Polish responsive spacing, text contrast, and neutral visual consistency.

Tests:

- Dashboard state construction.
- More hub navigation smoke checks.
- Existing unit tests.

## 5. Bug Policy

The v1.0 foundation is considered closed. Bugs discovered by testing are not treated as unfinished v1.0 feature work.

Bug handling:

- Fix critical bugs immediately when they block use or data visibility.
- Attach normal bugs to the affected post-v1.0 task when they are related to planned work.
- Create separate bugfix entries for unrelated regressions.
- Always preserve old user data unless the user explicitly asks for a reset.
