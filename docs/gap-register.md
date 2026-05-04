# Gap Register

## Gap Status Values
- Planned
- Done
- Deferred

## Gap Priority Values
- Critical
- High
- Medium
- Low

## V1.0 Gap Policy

Tasks 01 through 09 are closed for the Mini Budget v1.0 foundation, excluding bugs found by testing.

The remaining items below are not unfinished v1.0 work. They are planned post-v1.0 improvements grouped into single-agent, testable tasks in `docs/11-implementation-roadmap.md`.

## Gaps
| ID | Area | Gap | Why It Matters | Priority | Status | Related Future Task |
|---|---|---|---|---|---|---|
| GAP-001 | Weekly Allowance | Derived allowance suggestions, smarter category limit suggestions, and deeper review insight remain. | Needed for the full short-term discipline and recovery loop. | Critical | Planned | TASK-014 |
| GAP-002 | Debt | Advanced strategy, amortization, editing, history detail, and celebrations remain. | Debt recovery is a core product pillar. | Critical | Planned | TASK-015 |
| GAP-003 | Net Worth | Dashboard integration, snapshot detail rows, richer trends, and milestones remain. | User needs visible long-term financial strength and movement over time. | High | Planned | TASK-016 |
| GAP-004 | Extra Income | Split allocations, trends, dashboard integration, and linked debt-payment/goal actions remain. | Extra work and once-off income should visibly connect to recovery impact. | High | Planned | TASK-017 |
| GAP-005 | Goals | Editing, status changes, extra-income links, simulations, recommendations, and milestones remain. | User needs recovery-friendly goals that can adapt as the plan changes. | Medium | Planned | TASK-018 |
| GAP-006 | Recurring Expenses | Recurring expenses are marked but do not yet have recurrence rules, due dates, pause/end states, or generated planning views. | Monthly commitments should support planning and allowance calculations. | High | Planned | TASK-013 |
| GAP-007 | Backup and Ownership | Restore/import, backup verification, user-selected export destinations, and stronger backup health guidance remain. | Offline-first apps still need user-controlled backup and restore. | High | Planned | TASK-020 |
| GAP-008 | Security | PIN, biometric unlock, auto-lock, and sensitive-value hiding remain. | Financial data is sensitive even when local-only. | Medium | Planned | TASK-021 |
| GAP-009 | Analytics Periods and Reports | Analytics lacks period selection and readable export/report flows. | User cannot review previous months or export personal records easily. | Medium | Planned | TASK-022 |
| GAP-010 | Migration Safety | Migration tests and schema review are not yet strong enough for long-term schema growth. | Future schema changes need stronger regression protection. | High | Planned | TASK-012 |
| GAP-011 | UI Workflow Tests | Main user flows do not yet have broad UI workflow/smoke coverage. | Add/edit/delete flows can regress without detection. | Medium | Planned | TASK-012 |
| GAP-012 | Category Metadata | Categories need richer pressure/health metadata and guidance hooks. | Weekly allowance and recovery suggestions need stronger category signals. | Medium | Planned | TASK-024 |
| GAP-013 | Receipt Photo Flow | `photoPath` exists but capture/display flow is incomplete. | Data model suggests functionality users cannot fully access. | Low | Planned | TASK-024 |
| GAP-014 | Profile Depth | Profile needs income cycle, planning preferences, backup preferences, and recovery settings. | Recovery features need personal planning context. | Medium | Planned | TASK-023 |
| GAP-015 | Weekly Reviews | Weekly review exists but guided reflection and generated insight can be deeper. | Weekly allowance should help the user recover and learn without shame. | High | Planned | TASK-014 |
| GAP-016 | Gamification | Recovery XP event wiring across all recovery flows and monthly battle reports remain. | Gamification should reward useful recovery behavior throughout the app. | Medium | Planned | TASK-019 |
| GAP-017 | Dashboard and Navigation Integration | Secondary recovery systems need better dashboard presence while bottom navigation remains limited. | The app must stay scalable as features grow. | Medium | Planned | TASK-025 |
\n## 2026-05-04 Budget Cycle Hardening\n- Added canonical budget period contract doc and unified resolver adoption work in progress.
