# Vision Alignment Map

## Purpose
This document maps the current Mini Budget app to the long-term financial recovery vision.

## Alignment Table
| Existing Area | Supports Which Pillar? | Current Strength | Gap | Future Preparation Needed |
|---|---|---|---|---|
| Dashboard | Face Reality / Survive the Week | Shows monthly budget, weekly allowance, spend, remaining funds, top categories, and comparisons. | Weekly allowance is manual and basic. No debt, net worth, or deeper next-action guidance yet. | Add category pressure, debt pressure, net worth movement, and weekly recovery actions. |
| Expenses | Log Honestly / Face Reality | Users can log, edit, delete, filter, page, and mark recurring expenses. | Recurring is only a marker. Expenses do not yet connect to weekly pressure or recovery suggestions. | Add weekly allowance pressure calculations and richer recurring rules. |
| Categories | Face Reality | Spending is grouped into budget categories with limits. | No weak-category or pressure metadata. Color selection is not user-facing. | Add category health, allowance pressure, and recovery suggestion metadata. |
| Summary | Face Reality / Build Habits | Shows monthly overview, category summaries, top categories, and gamification status. | Mostly descriptive, not interpretive. | Add recovery-focused monthly review and weekly comparison. |
| Analytics | Face Reality | Shows category spend, budget health, over-budget count, and top category insight. | No month picker, trend chart, or action guidance. | Add selectable periods, pressure detection, and recovery recommendations. |
| Profile | Personalization | Stores local user name. | No financial recovery profile, income cycle, or settings. | Add income cycle, allowance preferences, backup preferences, and optional security settings. |
| Gamification | Build Better Habits / Recovery XP | Recovery XP, levels/titles, expanded badges, event history, and expense XP wiring exist. | Most non-expense recovery actions are not automatically wired into XP yet. | Wire XP events into weekly reviews, debt payments, net worth snapshots, extra income, goals, backups, and recovery actions. |
| Local Storage | Data Ownership | Room database is local-first and offline, and the Data Ownership screen can create local backup copies with backup freshness tracking. | No restore/import, encrypted backup, user-selected export destination, CSV/PDF export, or optional app lock yet. | Add restore/import, backup verification, encrypted backup, security settings, and readable exports without requiring cloud sync. |
| Debt Bosses | Defeat Debt | Debts can be tracked as bosses with HP, payment damage, interest/fee regeneration, strategy, and projection. | Advanced strategy, amortization, detailed history, and celebrations are not implemented yet. | Add editing, full history, debt-free timeline, strategy engine, and gamification events. |
| Net Worth | Build Net Worth | Assets can be tracked, current debt balances are included, net worth is calculated as assets minus debts, and manual snapshots show movement. | No dashboard summary, rich charting, snapshot detail rows, projections, or milestone celebrations yet. | Add dashboard card, trend visualizations, detailed snapshot preservation, and net worth milestones. |
| Extra Income | Respect Extra Income | Extra income can be tracked separately, allocated to recovery or spending outcomes, and summarized monthly. | No split allocation, trend chart, dashboard card, or automatic debt/goal action linkage yet. | Add split allocation, debt payment linkage, goal linkage, and impact trends. |
| Goals | Grow Through Goals | Goals can be created, contributed to, classified by health, and checked for feasibility against a planned monthly contribution target. | No edit/status workflow, recommendations, what-if simulator, extra-income linkage, or milestone badges yet. | Add goal editing, pause/cancel/defer actions, extra-income contribution links, recommendations, simulations, and milestones. |

## Missing Product Pillars
- Survive the Week: manual weekly allowance foundation exists, but full weekly review, category pressure, and recovery actions are missing.
- Defeat Debt: first debt boss and payment system exists; advanced strategy and projection work remains.
- Build Net Worth: first asset and snapshot foundation exists; richer movement interpretation remains.
- Respect Extra Income: first extra income and allocation foundation exists; richer impact workflows remain.
- Grow Through Goals: first goal and feasibility foundation exists; richer planning workflows remain.
- Recovery XP: first recovery XP and event-history foundation exists; cross-feature event wiring remains.

## Preparation Recommendations
1. Continue weekly allowance follow-ups before large debt or gamification work, because weekly spending control is the main short-term recovery loop.
2. Keep expense and category calculations testable before connecting them to allowance pressure.
3. Avoid overloading `MonthlyBudgetEntity` with income, allowance, or debt responsibilities. Add focused models when those features start.
4. Add migration tests before introducing larger schema changes.
5. Add a period selector pattern that can be reused across dashboard, analytics, summary, and weekly allowance.
6. Keep user-facing guidance non-judgmental and coach-like.
