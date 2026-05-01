# Current App Baseline

## Last Updated
2026-05-01

## App Summary
Mini Budget is currently a local-first Android budgeting app built with Kotlin, Material components, Jetpack Navigation, ViewModels, coroutines, and Room. It supports monthly budget tracking, expense logging, categories, weekly allowance, debt bosses, net worth snapshots, extra income impact, goals, recovery XP/gamification, local backup status, summary/analytics views, and a user profile.

The app is not yet the full recovery companion described in the product vision. It has a useful expense and monthly budget foundation, plus first versions of weekly allowance, debt bosses, net worth, extra income impact, goals, recovery XP, and local data ownership. Full restore/import, security, and deeper recovery guidance are not implemented yet.

## Existing Screens
| Screen | Purpose | Current Status | Notes |
|---|---|---|---|
| Dashboard | Shows current month budget progress, weekly allowance status, category pressure, category limits, recovery actions with completion tracking, weekly review prompt, recent allowance history, remaining funds, top categories, daily trend text, category breakdown text, previous-month comparison, and edit actions. | Working / Partial | Good overview base. Weekly allowance is still manual first version. Needs debt, net worth, and derived allowance suggestions later. Daily trend currently uses the last 7 real calendar days, not necessarily days in the selected month. |
| Weekly Review Summary | Shows current weekly allowance result, review notes, category pressure/limits, action completion, and recent trend bars. | Working / Partial | Useful first summary screen. Trend visualization is simple and can be deepened later. Accessed from More. |
| Debt Bosses | Tracks debts as bosses with HP/current balance, progress, strategy, warnings, simple projection, and payment/action logging. | Working / Partial | First foundation exists. Needs editing, detailed history, amortization, strategy comparison, timeline, and celebrations. |
| Net Worth | Tracks active assets, calculates assets minus current debt balances, saves snapshots, and shows recent snapshot movement. | Working / Partial | Accessed from More. Needs richer trends, snapshot details, milestone celebrations, and dashboard integration. |
| Extra Income | Tracks extra income separately, assigns allocations, summarizes monthly impact, and links debt allocations to debt boss names. | Working / Partial | Accessed from More. Does not yet split allocations, create linked debt payments, or show trends. |
| Goals | Tracks goals, progress, required monthly contribution, feasibility status, health classification, priority, and contributions. | Working / Partial | Accessed from More. Needs editing, pause/cancel flows, goal/extra-income linkage, recommendations, and richer simulations. |
| Recovery Progress | Shows recovery XP, level/title, logging streaks, badges, and recent XP events. | Working / Partial | Accessed from More. XP event foundation exists, but only expense logging is automatically wired so far. |
| Data Ownership | Shows local-first privacy status, backup freshness, backup file path, and allows creating a local database backup copy. | Working / Partial | Accessed from More. Restore/import, encrypted backups, and user-selected export destinations are not implemented yet. |
| Expenses | Lists expenses, supports add/edit/delete, filtering by all/day/week/monthly recurring, simple paging, totals, and recurring separation. | Working / Partial | Recurring is currently a marker only. It does not generate future transactions or support recurrence rules. |
| Add/Edit Expense | Captures amount, description, category, date, and recurring flag. | Working | Photo path exists in the data model, but no visible receipt capture flow is implemented in the current screen. |
| Categories | Lists active categories, supports add/edit, soft delete, icons, colors, and monthly category limits. | Working / Partial | Category colors are stored but editing color is not exposed in the dialog. |
| Summary | Shows monthly overview, category summaries, top categories, and gamification status. | Working / Partial | Mostly text-based. Useful as a baseline but not yet recovery-focused. |
| Analytics | Shows spending analytics by category, total spent, budget health, over-budget count, and top category insight. | Working / Partial | No month picker yet. No charting beyond structured rows/cards. |
| More | Groups secondary and future feature screens behind a scalable hub. | Working / Partial | Bottom navigation now stays limited to primary destinations: Dashboard, Expenses, Categories, Debts, and More. |
| Profile | Stores first name and last name and supports exit action. | Working / Partial | Accessed from More. No recovery profile fields, income cycle, preferences, PIN/biometric, export/import, or settings. |

## Existing Data Models
| Model / Table / Entity | Purpose | Key Fields | Notes |
|---|---|---|---|
| `MonthlyBudgetEntity` / `monthly_budget` | Stores one monthly starting-funds record. | `monthId`, `startDate`, `startingFunds` | `monthId` uses `yyyy-MM`. Dashboard creates missing months with `0.0` budget. |
| `CategoryEntity` / `categories` | Stores expense categories and budget limits. | `id`, `name`, `color`, `icon`, `budgetLimit`, `isActive` | Supports soft delete through `isActive`. |
| `ExpenseEntity` / `expenses` | Stores logged expenses. | `id`, `amount`, `description`, `date`, `categoryId`, `isRecurring`, `photoPath`, `createdAt` | Has foreign key to categories. `isRecurring` is currently a monthly marker. `photoPath` is stored but not fully surfaced in the current UI. |
| `GamificationEntity` / `gamification` | Stores streak, badge, and XP progress. | `id`, `currentStreak`, `longestStreak`, `lastLoggedDate`, `badgesEarned`, `totalExpensesLogged`, `totalXp` | Badges are comma-separated IDs. Expense logging is automatically wired to XP. |
| `GamificationEventEntity` / `gamification_events` | Stores recovery XP event history. | `id`, `eventType`, `xpEarned`, `occurredDate`, `message`, `relatedId`, `createdAt` | First recovery XP foundation. Most non-expense features still need automatic event wiring. |
| `UserProfileEntity` / `user_profile` | Stores local user identity. | `id`, `firstName`, `lastName`, `createdDate` | Single-user profile only. |
| `WeeklyAllowanceEntity` / `weekly_allowance` | Stores manual weekly allowance plans. | `weekStartDate`, `weekEndDate`, `allowanceAmount`, `createdAt`, `updatedAt` | First version supports current-week manual setup and dashboard calculations. |
| `WeeklyReviewEntity` / `weekly_review` | Stores weekly review notes. | `weekStartDate`, `weekEndDate`, `wentWell`, `challenge`, `nextWeekAdjustment`, `createdAt`, `updatedAt` | Supports a basic dashboard review dialog. |
| `WeeklyCategoryAllowanceEntity` / `weekly_category_allowance` | Stores category-specific weekly limits. | `weekStartDate`, `weekEndDate`, `categoryId`, `limitAmount`, `updatedAt` | Used to flag over-limit category pressure. |
| `WeeklyRecoveryActionEntity` / `weekly_recovery_action` | Stores completion state for generated recovery actions. | `weekStartDate`, `weekEndDate`, `actionText`, `isCompleted`, `updatedAt` | Generated action text is the stable key for the current implementation. |
| `DebtEntity` / `debts` | Stores debt boss records. | `id`, `name`, `debtType`, `startingBalance`, `currentBalance`, `interestRate`, `minimumPayment`, `paymentDueDay`, `isUnderDebtReview`, `interestStillApplies`, `strategy`, `notes`, `createdDate`, `closedDate` | First debt boss foundation. |
| `DebtPaymentEntity` / `debt_payments` | Stores debt payment/action history. | `id`, `debtId`, `amount`, `date`, `paymentType`, `notes`, `createdAt` | Includes payments, extra payments, settlements, adjustments, interest, and fees. |
| `AssetEntity` / `assets` | Stores current asset values for net worth. | `id`, `name`, `assetType`, `currentValue`, `notes`, `createdAt`, `updatedAt`, `isActive` | First asset foundation. Values are manually maintained. |
| `NetWorthSnapshotEntity` / `net_worth_snapshots` | Stores historical net worth totals. | `id`, `snapshotDate`, `totalAssets`, `totalDebts`, `netWorth`, `notes`, `createdAt`, `updatedAt` | Preserves monthly/manual historical totals for movement tracking. |
| `ExtraIncomeEntity` / `extra_income` | Stores extra income entries and their allocation outcome. | `id`, `source`, `incomeType`, `amount`, `dateReceived`, `allocationType`, `linkedDebtId`, `linkedGoalId`, `notes`, `createdAt`, `updatedAt` | First extra income foundation. Goal links are reserved for future goals work. |
| `GoalEntity` / `goals` | Stores financial goals and feasibility inputs. | `id`, `name`, `goalType`, `targetAmount`, `currentAmount`, `targetDate`, `monthlyContributionTarget`, `priority`, `healthClassification`, `status`, `notes`, `createdAt`, `updatedAt`, `completedAt` | First goals foundation. |
| `GoalContributionEntity` / `goal_contributions` | Stores progress contributions toward goals. | `id`, `goalId`, `amount`, `contributionDate`, `sourceType`, `linkedExtraIncomeId`, `notes`, `createdAt`, `updatedAt` | Contributions update goal progress and can complete goals. |
| Backup metadata / SharedPreferences | Stores local backup status. | `last_backup_date`, `last_backup_path` | Backup files are copied into app-controlled local storage. |

## Existing Storage System
- Storage is local-only Room database named `budget_tracker.db`.
- Database version is currently `11`.
- Registered entities are monthly budget, categories, expenses, gamification, gamification events, user profile, weekly allowance/review records, debt boss records, assets, net worth snapshots, extra income entries, goals, and goal contributions.
- Migration `1 -> 2` creates `user_profile`.
- Migration `2 -> 3` adds `expenses.isRecurring`.
- Migration `3 -> 4` creates `weekly_allowance`.
- Migration `4 -> 5` creates `weekly_review`.
- Migration `5 -> 6` creates `weekly_category_allowance` and `weekly_recovery_action`.
- Migration `6 -> 7` creates `debts` and `debt_payments`.
- Migration `7 -> 8` creates `assets` and `net_worth_snapshots`.
- Migration `8 -> 9` creates `extra_income`.
- Migration `9 -> 10` creates `goals` and `goal_contributions`.
- Migration `10 -> 11` adds `gamification.totalXp` and creates `gamification_events`.
- `exportSchema` is currently `false`.
- The app prepopulates default categories, gamification, and default user during database creation.
- No cloud sync, account creation, bank sync, or required network access is present.
- Local backup copies can be created from the Data Ownership screen. Restore/import is not implemented yet.

## Existing Calculations
| Area | Calculation | Current Behavior |
|---|---|---|
| Monthly overview | total budget, total spent, remaining, percentage used, over-budget flag | Uses `monthly_budget.startingFunds` and expense sums for the month. |
| Category spending | category spent, budget limit, percentage used, remaining, over-budget flag | Uses category budget limits and expense sums for a month. |
| Category summaries | category amount and percentage of total spending | Filters out zero-spend categories and sorts descending. |
| Top categories | highest spending categories | Takes top categories by total spent. |
| Daily spending trend | last N days spending | Uses `LocalDate.now()` and sums each day. Currently not scoped to the provided month ID. |
| Previous month comparison | current spent vs previous month spent | Compares monthly overview totals and calculates percentage change. |
| Weekly allowance | allowance, non-recurring spent this week, remaining, days remaining, safe daily spend, status, category pressure, category limits, recovery actions, action completion, trend history | Uses manually set current-week allowance and non-recurring expense sums for Monday-Sunday week. |
| Debt boss | current HP, damage dealt, progress, simple payoff months, warnings, monthly battle summary | Uses current balance, starting balance, minimum payment, and payment/action rows. Projection is intentionally simple and does not include amortized interest yet. |
| Net worth | current assets, current debts, net worth, snapshot movement, asset movement, debt movement | Calculates current net worth live as active assets minus current debt boss balances. Historical movement uses saved snapshots. |
| Extra income impact | monthly extra income total, recovery amount, debt recovery, savings/goals/buffer, personal/living, unallocated, recovery percentage, main impact | Uses extra income entries for the selected month. Linked debt is informational and does not automatically create a debt payment yet. |
| Goals | remaining amount, progress percentage, months remaining, required monthly contribution, feasibility, health classification | Compares required monthly contribution to the user's planned monthly contribution target. Does not yet calculate true free cash flow. |
| Recovery XP | total XP, level, title, current level range, recent events | Expense logging automatically earns XP. Repository supports XP events for weekly review, debt, net worth, extra income, goals, and backups, but those feature flows are not all wired yet. |
| Streaks | current/longest expense logging streak | Updates when a new expense is added. Same-day logging does not increase the streak. |
| Badges | first entry, week warrior, streak master, budget hero, debt, net worth, extra income, goals, weekly review badges | Stored as comma-separated IDs. Some badges depend on event hooks that are still future wiring work. |
| Backup status | days since last backup, backup freshness, local backup path | Uses SharedPreferences metadata and local database backup files in app-controlled storage. |

## Existing Tests
| Test Type | Location | Coverage |
|---|---|---|
| JVM unit tests | `app/src/test/java/com/example/budgettracker` | Analytics repository calculations, dashboard month creation/UI state, expense insertion through view model, fake DAOs/repositories, main dispatcher rule. |
| Android instrumentation tests | `app/src/androidTest/java/com/example/budgettracker` | Room DAO tests for categories, expenses, and monthly budget using in-memory database. |

### Existing Test Command
```bash
./gradlew test
```

Result on 2026-05-01: pass.

### Instrumentation Test Command
```bash
./gradlew connectedAndroidTest
```

Instrumentation tests require an emulator or Android device.

Instrumentation tests were audited but not run during Task 02 because no emulator/device run was requested.

## Post-1.0 Planned Improvements and Known Test Risks
- Task docs currently refer to `/docs/mini-budget/...`, but the repository stores the docs directly under `docs/`. `AGENTS.md` documents this path mapping.
- Recurring expenses are only marked as monthly recurring; there are no recurrence rules, next due dates, generated instances, pause state, or recurrence history.
- Restore/import, encrypted backup, user-selected export destinations, PIN, biometric, and app lock features are not implemented.
- Goals cannot be edited, paused, cancelled, deferred, or linked automatically to extra-income entries yet.
- Recovery XP events are not yet automatically wired across all recovery actions. Expense logging is wired; debt, net worth, extra income, goals, weekly review, recovery actions, and backups need follow-up integration.
- Extra income allocations cannot be split across multiple outcomes yet.
- Extra income debt allocations do not automatically create Debt Boss payment/action history yet.
- Net worth snapshot details are not stored yet; snapshots preserve historical totals, not per-asset/per-debt line items.
- `photoPath` exists on expenses, but receipt photo capture/display is not fully implemented in the current user flow.
- Category color exists in the data model, but the category dialog does not expose color selection.
- Analytics and dashboard month selection is limited. Dashboard creates the current month, but analytics does not expose a month picker.
- `exportSchema = false` means Room schema history is not exported for migration review.
- Migration tests are not present.
- UI workflow tests are not present.
- Dashboard daily trend ignores the supplied month ID and uses the last N days from today.
- Default database prepopulation should be reviewed with a real fresh install to confirm callback timing and data creation behavior.

## Future Feature Readiness
| Future Feature | Readiness | Notes |
|---|---|---|
| Weekly allowance | V1.0 foundation closed / Post-1.0 planned | Manual current-week allowance, status, dashboard card, category pressure, category limits, weekly review notes, summary screen, history, recovery actions, and action completion exist. Follow-up work is tracked in `TASK-014`. |
| Debt bosses | V1.0 foundation closed / Post-1.0 planned | Debt models, payment history, boss HP/progress, simple projection, and screen exist. Follow-up work is tracked in `TASK-015`. |
| Net worth | V1.0 foundation closed / Post-1.0 planned | Asset storage, current net worth calculation, manual snapshots, movement guidance, and a More-accessible screen exist. Follow-up work is tracked in `TASK-016`. |
| Extra income impact | V1.0 foundation closed / Post-1.0 planned | Extra income entries, allocation tracking, monthly impact summary, debt link context, and a More-accessible screen exist. Follow-up work is tracked in `TASK-017`. |
| Goals | V1.0 foundation closed / Post-1.0 planned | Goal storage, contributions, progress, monthly requirement, feasibility status, health classification, and a More-accessible screen exist. Follow-up work is tracked in `TASK-018`. |
| Gamification | V1.0 foundation closed / Post-1.0 planned | Recovery XP, levels/titles, event history, expanded badges, and Recovery Progress screen exist. Follow-up work is tracked in `TASK-019`. |
| Backup/export/import | V1.0 foundation closed / Post-1.0 planned | Local backup copy creation, backup status, and backup XP exist. Follow-up work is tracked in `TASK-020`, `TASK-021`, and `TASK-022`. |
