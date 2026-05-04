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

### 2026-05-02 - UI-FIX - Toolbar Inset Spacing Fix

#### Summary
Fixed shared toolbar inset handling so the toolbar height expands by the status bar inset instead of only adding top padding. This prevents Base child screen content from visually tucking under or overlapping the header area.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/MainActivity.kt`
- `docs/change-log.md`

#### User-Facing Impact
Base views such as Bonus Credits now have cleaner spacing between the app header and screen content.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Base Navigation Fix and Base Screen Upgrade

#### Summary
Fixed bottom navigation behavior for Base child screens by explicitly resetting bottom-tab navigation to tab roots and showing toolbar Up navigation for non-tab destinations. Upgraded Base hub and Base child screens with arcade panels, chips, progress/status styling, and clearer Recovery Arcade language.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/MainActivity.kt`
- `app/src/main/java/com/example/budgettracker/ui/analytics/AnalyticsFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/extraincome/ExtraIncomeFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/gamification/GamificationFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/goals/GoalsFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/networth/NetWorthFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/privacy/DataOwnershipFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/summary/SummaryFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/weekly/WeeklyReviewSummaryFragment.kt`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/layout/fragment_profile.xml`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
Opening a screen from Base now provides a clear Up path back. Switching bottom navigation tabs clears Base child screens instead of leaving Base nested state active. Base child screens now look more like arcade recovery modules.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Loadout and Bosses Arcade Upgrade

#### Summary
Upgraded the Spending Loadout and Debt Bosses screens. Loadout categories now render as shield cards with readiness status, rank labels, and shield cap meters. Debt bosses now render as battle cards with threat status, HP metrics, damage progress, battle intel, warnings, and compact hit/extra/regen actions.

#### Files Changed
- `app/src/main/res/layout/fragment_category_list.xml`
- `app/src/main/res/layout/item_category.xml`
- `app/src/main/java/com/example/budgettracker/ui/categories/CategoryListFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/debt/DebtBossFragment.kt`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
Loadout feels like configuring category defenses, and Bosses feels like tracking debt battles instead of reading plain finance rows.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Damage Log Gamified Pass

#### Summary
Upgraded the Damage Log screen with a Mission Scope filter panel, stronger arcade summary card, run-rank chip, and gamified log entries with severity labels, XP markers, auto-damage badges, and dynamic card stroke colors.

#### Files Changed
- `app/src/main/res/layout/fragment_expense_list.xml`
- `app/src/main/res/layout/item_expense.xml`
- `app/src/main/java/com/example/budgettracker/ui/expenses/ExpenseListFragment.kt`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
The Damage Log now reads as a tactical spending log with scan-friendly severity and recovery feedback instead of a plain transaction list.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Main HUD Graph and Gamified Reports

#### Summary
Upgraded the Dashboard Recovery Run, Damage Report, and Pressure Zones sections from plain generated text rows into visual HUD modules: a 7-day spending bar graph, category damage cards with progress meters, and ranked pressure-zone alert cards.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardFragment.kt`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
The main HUD now shows trends and category pressure visually, making the app feel less like a report screen and more like a recovery game interface.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Main HUD Safe Spend Shield Upgrade

#### Summary
Reworked the Dashboard Safe Spend Shield section from a multiline text summary into a more visual HUD card with a large shield metric, circular percentage indicator, status chip, compact weekly spend intel, and guidance callout.

#### Files Changed
- `app/src/main/res/layout/fragment_dashboard.xml`
- `app/src/main/res/drawable/ra_inner_panel_bg.xml`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardFragment.kt`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
The main HUD weekly allowance area is easier to scan and feels closer to the arcade prototype instead of presenting the user with a block of text.

#### Data/Storage Impact
None.

#### Tests
```bash
./gradlew assembleDebug
```

Passed on 2026-05-02.

### 2026-05-02 - UI-REVAMP - Recovery Arcade Stitch Prototype Integration

#### Summary
Adapted the supplied arcade-style HTML prototype into the native Android app. Updated shared theme/chrome, bottom navigation icons and labels, screen copy, dialog copy, and generated row/status text so the app reads as a Recovery Arcade HUD instead of a styled spreadsheet.

#### Files Changed
- `app/src/main/res/values/styles_recovery_arcade.xml`
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/values/strings.xml`
- `app/src/main/res/color/bottom_nav_arcade_tint.xml`
- `app/src/main/res/drawable/ic_nav_base.xml`
- `app/src/main/res/drawable/ic_nav_boss.xml`
- `app/src/main/res/drawable/ic_nav_hud.xml`
- `app/src/main/res/drawable/ic_nav_loadout.xml`
- `app/src/main/res/drawable/ic_nav_log.xml`
- `app/src/main/res/layout/activity_main.xml`
- `app/src/main/res/layout/fragment_*.xml`
- `app/src/main/res/layout/item_*.xml`
- `app/src/main/res/menu/bottom_navigation.xml`
- `app/src/main/java/com/example/budgettracker/ui/**/*.kt`
- `app/src/main/java/com/example/budgettracker/adapters/CategorySpendingAdapter.kt`
- `docs/ui-ux/CHANGELOG.md`
- `docs/change-log.md`

#### User-Facing Impact
Navigation, headers, summaries, empty states, dialogs, generated cards, progress/status text, and button labels now use Recovery Arcade language such as Safe Spend Shield, Damage Log, Spending Loadout, Weekly Debrief, Boss Arena, Bonus Credits, Resource Vaults, Net Worth Meter, Data Vault, and Recovery XP.

#### Data/Storage Impact
None. No Room schema or storage behavior changed.

#### Tests
```bash
./gradlew assembleDebug
./gradlew test
```

Both passed on 2026-05-02.

### 2026-05-01 - V1.0-DOCS - Foundation Closure and Post-1.0 Roadmap

#### Summary
Closed Tasks 01 through 09 as the Mini Budget v1.0 foundation, excluding bugs found by testing. Consolidated remaining improvements into a structured post-v1.0 roadmap from `TASK-012` through `TASK-025`, and added a full reusable commit message for the foundation work.

#### Files Changed
- `docs/02-baseline-alignment-and-task-log.md`
- `docs/03-weekly-allowance-system.md`
- `docs/04-debt-boss-system.md`
- `docs/05-net-worth-system.md`
- `docs/06-extra-income-impact.md`
- `docs/07-goals-and-feasibility.md`
- `docs/08-gamification-rules.md`
- `docs/09-offline-first-and-privacy.md`
- `docs/11-implementation-roadmap.md`
- `docs/gap-register.md`
- `docs/task-log.md`
- `docs/v1.0-foundation-summary.md`
- `docs/v1.0-commit-message.md`

#### User-Facing Impact
No direct app behavior change. The project now has a clear v1.0 closure point and a scoped post-v1.0 task plan.

#### Data/Storage Impact
None.

#### Tests
Documentation-only change. Existing primary test command remains:

```bash
./gradlew test
```

### 2026-05-01 - TASK-009 - Offline-First, Privacy, and Local Backup Foundation

#### Summary
Added the first Data Ownership implementation with local-first privacy status, local database backup copy creation, backup freshness tracking, backup path display, and a backup XP hook.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/repository/BackupRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/privacy/DataOwnershipFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/privacy/DataOwnershipViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/privacy/DataOwnershipViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/privacy/DataOwnershipUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/res/layout/fragment_data_ownership.xml`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `app/src/test/java/com/example/budgettracker/BackupRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/FakeBackupStore.kt`
- `docs/09-offline-first-and-privacy.md`
- `docs/current-app-baseline.md`
- `docs/task-log.md`
- `docs/gap-register.md`
- `docs/vision-alignment-map.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can open Data Ownership from More, see the local-first privacy promise, create a local database backup copy, and see when and where the latest backup was created.

#### Data/Storage Impact
No Room schema change. Backup metadata is stored in SharedPreferences:

- `last_backup_date`
- `last_backup_path`

Local backup files are copied into app-controlled storage.

#### Tests
Added repository tests for:

- No-backup reminder status.
- Recent backup freshness.
- Backup metadata after creating a backup.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-008 - Gamification and Recovery XP Foundation

#### Summary
Added recovery-focused gamification with XP totals, event history, levels/titles, expanded badges, expense XP wiring, and a Recovery Progress screen under More.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/GamificationEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/entity/GamificationEventEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/GamificationEventDao.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/GamificationRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/gamification/GamificationFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/gamification/GamificationViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/gamification/GamificationViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/gamification/GamificationUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/res/layout/fragment_gamification.xml`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `app/src/test/java/com/example/budgettracker/GamificationRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/FakeGamificationEventDao.kt`
- `docs/08-gamification-rules.md`
- `docs/current-app-baseline.md`
- `docs/task-log.md`
- `docs/gap-register.md`
- `docs/vision-alignment-map.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can open Recovery Progress from More to see recovery XP, level/title, badges, streaks, and recent XP events. Expense logging now earns recovery XP.

#### Data/Storage Impact
Room database version increased to `11`. Added:

- `gamification.totalXp`
- `gamification_events`

Added migration `10 -> 11`.

#### Tests
Added repository tests for:

- Expense logging XP and badge awards.
- Recovery event XP and mapped badges.
- Level/title calculation from XP.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-007 - Goals and Feasibility Foundation

#### Summary
Added the first Goals implementation with local goal storage, goal contributions, progress tracking, required monthly contribution calculation, health classification, feasibility classification, and a Goals screen under More.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/GoalEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/entity/GoalContributionEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/GoalDao.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/GoalContributionDao.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/GoalRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/goals/GoalsFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/goals/GoalsViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/goals/GoalsViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/goals/GoalsUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/res/layout/fragment_goals.xml`
- `app/src/main/res/layout/dialog_goal.xml`
- `app/src/main/res/layout/dialog_goal_contribution.xml`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `app/src/test/java/com/example/budgettracker/GoalRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/FakeGoalDao.kt`
- `app/src/test/java/com/example/budgettracker/FakeGoalContributionDao.kt`
- `docs/07-goals-and-feasibility.md`
- `docs/current-app-baseline.md`
- `docs/task-log.md`
- `docs/gap-register.md`
- `docs/vision-alignment-map.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can open Goals from More, create financial goals, see required monthly contributions, review feasibility, add contributions, and complete goals when the target is reached.

#### Data/Storage Impact
Room database version increased to `10`. Added:

- `goals`
- `goal_contributions`

Added migration `9 -> 10`.

#### Tests
Added repository tests for:

- Feasible monthly contribution calculation.
- Unrealistic goal classification.
- Contribution progress and goal completion.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-006 - Extra Income Impact Foundation

#### Summary
Added the first Extra Income implementation with local extra-income storage, income/allocation types, monthly impact summaries, optional debt linking, and an Extra Income screen under More.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/ExtraIncomeEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/ExtraIncomeDao.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/ExtraIncomeRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/extraincome/ExtraIncomeFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/extraincome/ExtraIncomeViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/extraincome/ExtraIncomeViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/extraincome/ExtraIncomeUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/res/layout/fragment_extra_income.xml`
- `app/src/main/res/layout/dialog_extra_income.xml`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `app/src/test/java/com/example/budgettracker/ExtraIncomeRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/FakeExtraIncomeDao.kt`
- `docs/06-extra-income-impact.md`
- `docs/current-app-baseline.md`
- `docs/task-log.md`
- `docs/gap-register.md`
- `docs/vision-alignment-map.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can open Extra Income from More, record extra income, assign it to an outcome, optionally link debt-payment allocations to a debt boss, and see how the month’s extra money supported recovery or ordinary life.

#### Data/Storage Impact
Room database version increased to `9`. Added:

- `extra_income`

Added migration `8 -> 9`.

#### Tests
Added repository tests for:

- Monthly allocation bucket summaries.
- Linked debt display in impact summaries.
- Validation for invalid linked-debt allocations.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-005-HOTFIX - Net Worth Migration Validation

#### Summary
Fixed a Room schema mismatch introduced by the Task 5 net worth migration. The migration created indexes for the new net worth tables, but the matching entity annotations were missing those indexes.

#### User-Facing Impact
Existing local databases should upgrade to version `8` without crashing, allowing old data to remain visible after the app opens.

#### Data/Storage Impact
No new schema version. Entity annotations now match the existing `7 -> 8` migration.

#### Tests
```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-005 - Net Worth Foundation

#### Summary
Added the first Net Worth implementation with local asset storage, current assets-minus-debts calculation, manual snapshots, recent snapshot movement, and a Net Worth screen under More.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/AssetEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/entity/NetWorthSnapshotEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/AssetDao.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/NetWorthSnapshotDao.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/NetWorthRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/networth/NetWorthFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/networth/NetWorthViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/networth/NetWorthViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/networth/NetWorthUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/res/layout/fragment_net_worth.xml`
- `app/src/main/res/layout/dialog_asset.xml`
- `app/src/main/res/layout/dialog_net_worth_snapshot.xml`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `app/src/test/java/com/example/budgettracker/NetWorthRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/FakeAssetDao.kt`
- `app/src/test/java/com/example/budgettracker/FakeNetWorthSnapshotDao.kt`
- `docs/05-net-worth-system.md`
- `docs/current-app-baseline.md`
- `docs/task-log.md`
- `docs/gap-register.md`
- `docs/vision-alignment-map.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can open Net Worth from More, add assets, update asset values, save net worth snapshots, and see current assets, debts, net worth, recent snapshots, and movement guidance.

#### Data/Storage Impact
Room database version increased to `8`. Added:

- `assets`
- `net_worth_snapshots`

Added migration `7 -> 8`.

#### Tests
Added repository tests for:

- Calculating current net worth as assets minus debts.
- Preserving snapshot totals and reporting movement.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - NAV-001 - Scalable Primary Navigation

#### Summary
Restructured navigation so the bottom bar remains a stable five-destination primary nav and secondary screens move into a More hub.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- `app/src/main/res/layout/fragment_more.xml`
- `app/src/main/res/menu/bottom_navigation.xml`
- `app/src/main/res/navigation/nav_graph.xml`
- `docs/current-app-baseline.md`
- `docs/testing-checklist.md`
- `docs/change-log.md`

#### User-Facing Impact
Users can still access Summary, Weekly Review, Analytics, and Profile from More while Dashboard, Expenses, Categories, Debts, and More remain available from the bottom bar.

#### Data/Storage Impact
None. No schema change.

#### Tests
```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-004 - Debt Boss Foundation

#### Summary
Added the first Debt Boss implementation with local debt storage, payment/action history, boss HP progress, simple payoff projection, monthly debt battle summary, and a new Debt Bosses screen.

#### User-Facing Impact
Users can add debts, see current boss HP, damage dealt, progress percentage, strategy, warnings, and log payments, extra payments, interest, or fees.

#### Data/Storage Impact
Room database version increased to `7`. Added:

- `debts`
- `debt_payments`

Added migration `6 -> 7`.

#### Tests
Added repository tests for:

- Adding debt and calculating projection.
- Recording payment damage.
- Recording interest regeneration.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

#### Documentation
Updated Task 04 spec, task log, gap register, change log, baseline, vision map, and testing checklist.

### 2026-05-01 - TASK-003 - Category Limits, Weekly Summary, Action Tracking, and Trends

#### Summary
Extended weekly allowance with category-specific limits, completed recovery actions, a full weekly review summary screen, and historical trend bars.

#### User-Facing Impact
Users can set weekly limits per category, check off recovery actions, open a weekly review summary, and see recent weekly allowance trends.

#### Data/Storage Impact
Room database version increased to `6`. Added:

- `weekly_category_allowance`
- `weekly_recovery_action`

Added migration `5 -> 6`.

#### Tests
Added/updated weekly allowance tests for category limits and recovery action completion.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-003 - Weekly Pressure, Review, History, and Recovery Actions

#### Summary
Extended the weekly allowance system with category pressure detection, generated recovery actions, weekly review notes, and recent weekly allowance history.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/WeeklyReviewEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/WeeklyReviewDao.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/WeeklyAllowanceDao.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/ExpenseDao.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/WeeklyAllowanceRepository.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardFragment.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardViewModel.kt`
- `app/src/main/res/layout/fragment_dashboard.xml`
- `app/src/main/res/layout/dialog_weekly_review.xml`
- `app/src/test/java/com/example/budgettracker/FakeWeeklyReviewDao.kt`
- `app/src/test/java/com/example/budgettracker/FakeWeeklyAllowanceDao.kt`
- `app/src/test/java/com/example/budgettracker/FakeExpenseDao.kt`
- `app/src/test/java/com/example/budgettracker/WeeklyAllowanceRepositoryTest.kt`
- `docs/03-weekly-allowance-system.md`
- `docs/change-log.md`
- `docs/gap-register.md`
- `docs/current-app-baseline.md`
- `docs/testing-checklist.md`

#### User-Facing Impact
The dashboard Weekly Allowance card now shows the main category pressure point, practical recovery actions, a weekly review prompt, and recent weekly allowance history.

#### Data/Storage Impact
Room database version increased to `5`. Added `weekly_review` table with:

- `weekStartDate`
- `weekEndDate`
- `wentWell`
- `challenge`
- `nextWeekAdjustment`
- `createdAt`
- `updatedAt`

Added migration `4 -> 5`.

#### Tests
Added/updated weekly allowance tests for category pressure, recovery actions, and weekly review persistence.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-003 - Exclude Recurring From Weekly Spend

#### Summary
Updated weekly allowance calculations so recurring expenses do not count toward weekly expenditure. Recurring payments remain monthly commitments and continue to be included in normal monthly spending and analytics.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/dao/ExpenseDao.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/WeeklyAllowanceRepository.kt`
- `app/src/test/java/com/example/budgettracker/FakeExpenseDao.kt`
- `app/src/test/java/com/example/budgettracker/WeeklyAllowanceRepositoryTest.kt`
- `docs/03-weekly-allowance-system.md`
- `docs/change-log.md`

#### User-Facing Impact
The dashboard Weekly Allowance card now reflects day-to-day weekly spend without recurring monthly payments inflating weekly pressure.

#### Data/Storage Impact
None. No schema change.

#### Tests
Added a regression test confirming recurring expenses are excluded from weekly allowance spend.

```bash
./gradlew test
```

Result on 2026-05-01: pass.

### 2026-05-01 - TASK-003 - Weekly Allowance Foundation

#### Summary
Added the first weekly allowance implementation: manual current-week allowance setup, current-week expense totals, remaining allowance, safe daily spend, days remaining, and supportive status guidance on the dashboard.

#### Files Changed
- `app/src/main/java/com/example/budgettracker/data/entity/WeeklyAllowanceEntity.kt`
- `app/src/main/java/com/example/budgettracker/data/dao/WeeklyAllowanceDao.kt`
- `app/src/main/java/com/example/budgettracker/data/repository/WeeklyAllowanceRepository.kt`
- `app/src/main/java/com/example/budgettracker/data/database/AppDatabase.kt`
- `app/src/main/java/com/example/budgettracker/utils/DataModels.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardViewModel.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardViewModelFactory.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardUiState.kt`
- `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardFragment.kt`
- `app/src/main/res/layout/fragment_dashboard.xml`
- `app/src/main/res/layout/dialog_weekly_allowance.xml`
- `app/src/test/java/com/example/budgettracker/FakeWeeklyAllowanceDao.kt`
- `app/src/test/java/com/example/budgettracker/WeeklyAllowanceRepositoryTest.kt`
- `app/src/test/java/com/example/budgettracker/DashboardViewModelTest.kt`
- `docs/current-app-baseline.md`
- `docs/vision-alignment-map.md`
- `docs/gap-register.md`
- `docs/task-log.md`
- `docs/change-log.md`
- `docs/testing-checklist.md`

#### User-Facing Impact
The dashboard now shows a Weekly Allowance card. Users can set or update the current week's allowance manually and see spent, remaining, days left, safe daily spend, and a non-judgmental status message.

#### Data/Storage Impact
Room database version increased to `4`. Added `weekly_allowance` table with:

- `weekStartDate`
- `weekEndDate`
- `allowanceAmount`
- `createdAt`
- `updatedAt`

Added migration `3 -> 4`.

#### Tests
Added repository tests for:

- No allowance set.
- Stable weekly spending.
- Over-plan weekly spending.

Existing JVM test suite run with:

```bash
./gradlew test
```

Result on 2026-05-01: pass.

Instrumentation tests were not run because they require an emulator or connected Android device.

#### Documentation
Updated baseline, vision alignment, gap register, task log, change log, and testing checklist.

### 2026-05-01 - TASK-002 - Baseline Alignment

#### Summary
Documented the current app baseline and prepared the project for future feature work.

#### Files Changed
- `docs/current-app-baseline.md`
- `docs/vision-alignment-map.md`
- `docs/gap-register.md`
- `docs/task-log.md`
- `docs/change-log.md`
- `docs/testing-checklist.md`

#### User-Facing Impact
No direct user-facing feature change.

#### Data/Storage Impact
None. This task did not change the Room schema or app data behavior.

#### Tests
Existing JVM test suite run with:

```bash
./gradlew test
```

Result on 2026-05-01: pass.

Instrumentation tests were audited but not run because they require an emulator or connected Android device:

```bash
./gradlew connectedAndroidTest
```

#### Documentation
Added baseline, vision alignment, gap register, task log, change log, and testing checklist documents.
\n## 2026-05-04 Budget Cycle Hardening\n- Added canonical budget period contract doc and unified resolver adoption work in progress.
