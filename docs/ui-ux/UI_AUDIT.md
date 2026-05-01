# UI Audit (Baseline)

Date: 2026-05-01

## App Stack

- Android Views + Fragments (not Jetpack Compose)
- Material Components / Material3 theme (`Theme.Material3.Light.NoActionBar`)
- Many screens render dynamic rows by inflating/creating `TextView`s into `LinearLayout` containers

## Existing Screens (Fragments)

- Dashboard: `app/src/main/java/com/example/budgettracker/ui/dashboard/DashboardFragment.kt`
- Expenses list + add/edit expense:
  - `app/src/main/java/com/example/budgettracker/ui/expenses/ExpenseListFragment.kt`
  - `app/src/main/java/com/example/budgettracker/ui/expenses/AddExpenseFragment.kt`
- Categories: `app/src/main/java/com/example/budgettracker/ui/categories/CategoriesFragment.kt`
- Debt: `app/src/main/java/com/example/budgettracker/ui/debt/DebtBossFragment.kt`
- Goals: `app/src/main/java/com/example/budgettracker/ui/goals/GoalsFragment.kt`
- Analytics: `app/src/main/java/com/example/budgettracker/ui/analytics/AnalyticsFragment.kt`
- More/Settings hub: `app/src/main/java/com/example/budgettracker/ui/more/MoreFragment.kt`
- Weekly allowance: various fragments under `app/src/main/java/com/example/budgettracker/ui/weekly/`
- Net worth: `app/src/main/java/com/example/budgettracker/ui/networth/`
- Extra income: `app/src/main/java/com/example/budgettracker/ui/extraincome/`
- Profile: `app/src/main/java/com/example/budgettracker/ui/profile/`
- Privacy: `app/src/main/java/com/example/budgettracker/ui/privacy/`

## Current Visual Baseline

- The repo currently ships a **light** theme token set in `app/src/main/res/values/colors.xml` (`surface_canvas`, `surface_panel`, `ink_primary`, etc).
- Several screens already use a “panel/card” language (see Expenses list).
- Dashboard includes progress indicators and text sections, but a lot of the content is still text-first and dynamically appended `TextView`s.

## Inconsistencies / Pain Points

- Mixed visual language: some screens look “revamped” (Expenses), others are more text-heavy.
- Repeated patterns are implemented ad-hoc per screen (section headers, metric rows, empty states).
- Dynamic `TextView` creation makes styling drift easy (text sizes, colors, padding repeated in code).

## Candidate Reusable UI Patterns

- Screen header with title + optional subtitle
- “Panel” container (surface + border + consistent padding)
- “Metric row” (label + value + helper)
- “Status chip” (pill) for stable/warn/danger labels
- “Empty state” card with supportive copy

## Screens That Need The Most Visual Unification

- Dashboard (highest priority)
- Debt Boss screen
- Goals screen
- Weekly allowance screens

