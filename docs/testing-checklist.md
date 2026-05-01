# Testing Checklist

## Purpose
Every future Mini Budget feature must include appropriate tests and regression checks. The test depth should match the risk of the change.

## Current Test Stack
- JVM unit tests use JUnit and `kotlinx-coroutines-test`.
- Android instrumentation tests use AndroidX JUnit, Espresso dependency, and Room in-memory databases.
- Main JVM command:

```bash
./gradlew test
```

Task 02 result on 2026-05-01: pass.

- Instrumentation command:

```bash
./gradlew connectedAndroidTest
```

Instrumentation tests require an emulator or connected Android device.

## Test Types

### Unit Tests
Use for:
- Calculations.
- Helpers.
- Repositories.
- ViewModels.
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
- Existing dashboard still loads.
- Existing expense creation still works.
- Existing expense edit/delete still works.
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
- [ ] Room migrations were added and documented for schema changes.
- [ ] Migration tests were added for non-trivial schema changes.
- [ ] Documentation was updated.
- [ ] Change log was updated.
- [ ] Task log was updated.

## Core Regression Checklist
- [ ] App launches successfully.
- [ ] Dashboard screen loads.
- [ ] Expense list loads.
- [ ] Expense can be created.
- [ ] Expense can be edited.
- [ ] Expense can be deleted.
- [ ] Recurring expense marker can be set and preserved.
- [ ] Recurring expenses do not count against weekly allowance spend.
- [ ] Weekly allowance category pressure identifies the largest non-recurring category.
- [ ] Weekly review notes can be saved and reloaded.
- [ ] Weekly allowance history displays recent saved weeks.
- [ ] Category-specific weekly limits can be set and reflected in pressure output.
- [ ] Recovery actions can be marked completed and remain completed after reload.
- [ ] Weekly review summary screen loads current summary and trend history.
- [ ] Debt boss list loads.
- [ ] Debt boss can be added.
- [ ] Debt payment reduces current HP.
- [ ] Interest/fee action increases current HP.
- [ ] Debt battle summary calculates minimum, extra, interest/fees, and net damage.
- [ ] Net Worth screen loads from More.
- [ ] Asset can be added and current net worth updates.
- [ ] Asset value can be updated.
- [ ] Net worth snapshot can be saved and shown in recent snapshots.
- [ ] Net worth calculation uses current debt boss balances.
- [ ] Extra Income screen loads from More.
- [ ] Extra income can be added.
- [ ] Extra income allocation buckets update monthly impact totals.
- [ ] Debt-linked extra income shows the linked debt name.
- [ ] Goals screen loads from More.
- [ ] Goal can be added.
- [ ] Goal feasibility calculates required monthly contribution.
- [ ] Goal contribution updates progress and completes a goal when target is reached.
- [ ] Recovery Progress screen loads from More.
- [ ] Expense logging awards recovery XP.
- [ ] Recovery XP events appear in recent events.
- [ ] XP levels and titles update as XP grows.
- [ ] Data Ownership screen loads from More.
- [ ] Local backup can be created.
- [ ] Backup status records last backup date and path.
- [ ] Bottom navigation has no more than five primary destinations.
- [ ] More screen loads and opens Summary, Weekly Review, Analytics, Net Worth, Extra Income, Goals, Recovery Progress, Data Ownership, and Profile.
- [ ] Expense filters work for all, day, week, and recurring monthly views.
- [ ] Category list loads.
- [ ] Category can be created.
- [ ] Category can be edited.
- [ ] Category can be soft deleted.
- [ ] Summary screen loads.
- [ ] Analytics screen loads.
- [ ] Profile screen loads and saves user details.
- [ ] Existing locally stored data remains readable.
- [ ] No cloud/network access is required.

## Current Coverage Gaps
- No UI workflow tests for bottom navigation or add/edit/delete flows.
- No migration tests.
- No tests for recurring expense filters/paging.
- No tests for dashboard daily trend month behavior.
- Backup repository tests exist for backup reminder status and metadata. Restore/import, file-copy integration, encrypted backup, and UI workflow tests are still missing.
- Weekly allowance repository tests exist for the manual foundation. UI workflow tests, review tests, and derived allowance tests are still missing.
- Debt repository tests exist for the first debt boss foundation. UI workflow tests and migration tests are still missing.
- Net worth repository tests exist for current calculation and snapshot movement. UI workflow tests, migration tests, and richer trend tests are still missing.
- Extra income repository tests exist for monthly allocation summaries and linked debt display. UI workflow tests, split allocation tests, and migration tests are still missing.
- Goal repository tests exist for feasibility, unrealistic goals, contribution progress, and completion. UI workflow tests, migration tests, and edit/status tests are still missing.
- Gamification repository tests exist for XP, mapped badges, level/title calculation, and event history. UI workflow tests, migration tests, and cross-feature event wiring tests are still missing.
