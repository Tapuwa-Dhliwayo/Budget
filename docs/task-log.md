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
| TASK-001 | Product Vision and Agent Instructions | Documentation | Completed | 2026-05-01 | 2026-05-01 | Added product direction and agent instruction docs. | N/A | Yes |
| TASK-002 | Baseline Alignment and Task Log System | Audit / Documentation / Test Prep | Completed | 2026-05-01 | 2026-05-01 | Audited current app structure, data models, calculations, storage, gaps, and tests. Created baseline task tracking docs. | No new tests added; existing tests audited and run. | Yes |
| TASK-003 | Weekly Allowance System | Feature | Completed | 2026-05-01 | 2026-05-01 | Added manual weekly allowance setup, current-week calculations, dashboard status, category limits, pressure detection, weekly review summary, recovery action tracking, history, and trend visualization. | Yes | Yes |
| TASK-004 | Debt Boss System | Feature | Completed | 2026-05-01 | 2026-05-01 | Added debt boss tracking, payment/action history, boss HP/progress, simple payoff projection, monthly battle summary, and Debt Bosses screen. | Yes | Yes |
| TASK-005 | Net Worth System | Feature | Completed | 2026-05-01 | 2026-05-01 | Added asset tracking, current net worth calculation, manual snapshots, recent snapshot movement, and a Net Worth screen under More. | Yes | Yes |
| TASK-006 | Extra Income Impact System | Feature | Completed | 2026-05-01 | 2026-05-01 | Added extra income tracking, allocation outcomes, monthly impact summary, optional debt linking, and an Extra Income screen under More. | Yes | Yes |
| TASK-007 | Goals and Feasibility System | Feature | Completed | 2026-05-01 | 2026-05-01 | Added goal tracking, contribution progress, required monthly contribution, feasibility classification, health classification, and a Goals screen under More. | Yes | Yes |
| TASK-008 | Gamification and Recovery XP | Feature | Completed | 2026-05-01 | 2026-05-01 | Added recovery XP, event history, levels/titles, expanded badges, expense XP wiring, and a Recovery Progress screen under More. | Yes | Yes |
| TASK-009 | Offline-First, Privacy, and Local Backup | Feature | Completed | 2026-05-01 | 2026-05-01 | Added Data Ownership screen, local-first privacy status, local database backup copy creation, backup freshness tracking, and backup XP hook. | Yes | Yes |

## V1.0 Foundation Closure

Status on 2026-05-01: Tasks 01 through 09 are closed as the Mini Budget v1.0 foundation, excluding bugs found by testing.

Remaining feature improvements are now tracked as post-v1.0 tasks starting at `TASK-012`. These are scoped so each can be completed, documented, and tested in a single agent run.

## Post-1.0 Planned Tasks

| Task ID | Title | Type | Status | Started | Completed | Summary | Tests Added | Docs Updated |
|---|---|---|---|---|---|---|---|---|
| TASK-012 | Test Hardening and Migration Safety | Test / Data Model | Planned | | | Add migration safety, schema review, and focused workflow smoke coverage for the v1.0 foundation. | Planned | Planned |
| TASK-013 | Recurring Expense Rules and Monthly Commitment Planning | Feature / Data Model | Planned | | | Add recurrence rules, due dates, pause/end states, and monthly commitment planning while keeping recurring commitments out of weekly flexible spend. | Planned | Planned |
| TASK-014 | Weekly Allowance Intelligence and Review Upgrade | Feature / UX | Planned | | | Add derived allowance suggestions, smarter category limits, and deeper weekly review insight. | Planned | Planned |
| TASK-015 | Debt Boss Management, History, and Strategy Engine | Feature | Planned | | | Add debt edit/archive/delete, detailed history, strategy comparison, extra payment simulation, amortized projection, and milestone hooks. | Planned | Planned |
| TASK-016 | Net Worth Trends and Snapshot Detail | Feature | Planned | | | Add snapshot detail rows, trend visualization, dashboard integration, and net worth milestone messaging. | Planned | Planned |
| TASK-017 | Extra Income Allocation and Recovery Links | Feature | Planned | | | Add split allocations, linked debt/goal actions, source history, trends, and dashboard impact card. | Planned | Planned |
| TASK-018 | Goals Lifecycle and What-If Planning | Feature | Planned | | | Add goal edit/pause/cancel/defer flows, extra-income links, what-if planning, and recommendations. | Planned | Planned |
| TASK-019 | Recovery XP Integration and Monthly Battle Report | Feature | Planned | | | Wire XP across recovery actions and add monthly recovery reporting and achievement detail. | Planned | Planned |
| TASK-020 | Backup Restore, Export Destinations, and Verification | Feature | Planned | | | Add restore/import, backup verification, user-selected export destinations, and backup health guidance. | Planned | Planned |
| TASK-021 | Security and Privacy Controls | Feature | Planned | | | Add optional PIN, biometric unlock, auto-lock, sensitive-value hiding, and local-only privacy controls. | Planned | Planned |
| TASK-022 | Reports, Analytics Periods, and Readable Exports | Feature | Planned | | | Add analytics period selection, CSV/PDF-style reports where practical, and improved report states. | Planned | Planned |
| TASK-023 | Profile and Planning Settings | Feature / UX | Planned | | | Add income cycle, payday, allowance preferences, backup preferences, and recovery profile fields. | Planned | Planned |
| TASK-024 | Expense and Category Depth | Feature / UX | Planned | | | Add receipt photo flow, category color/icon editing improvements, category metadata, and category guidance hooks. | Planned | Planned |
| TASK-025 | Dashboard Integration and Navigation Polish | UX | Planned | | | Integrate recovery systems into the dashboard and keep the More hub scalable without expanding the bottom bar. | Planned | Planned |

## Future Task Checklist
For each future task:

- [ ] Read `AGENTS.md`.
- [ ] Read `docs/01-product-vision.md`.
- [ ] Read `docs/10-codex-agent-instructions.md`.
- [ ] Read the relevant task or feature spec.
- [ ] Update `docs/task-log.md`.
- [ ] Update `docs/change-log.md`.
- [ ] Update `docs/gap-register.md` if gaps are opened, changed, or closed.
- [ ] Add or update tests for behavior changes.
- [ ] Run the relevant test command.
- [ ] Keep the app offline-first.
\n## 2026-05-04 Budget Cycle Hardening\n- Added canonical budget period contract doc and unified resolver adoption work in progress.
