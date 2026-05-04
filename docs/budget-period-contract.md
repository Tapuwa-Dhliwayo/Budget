# Budget Period Contract

Date: 2026-05-04  
Program: Budget Cycle Consistency & Start-Day Migration Hardening

## Canonical terminology
- Use **Budget Cycle** for user-facing labels.
- A cycle has a `monthId` anchor (format `yyyy-MM`) and a concrete date range.

## Canonical resolver
Implemented via `BudgetPeriodResolver` and consumed via `DateUtils`.

### monthId derivation
Given `date` and `startDay`:
1. Clamp `startDay` to `1..28`.
2. If `date.dayOfMonth >= startDay`, anchor cycle to `date` month.
3. Otherwise anchor cycle to previous month.
4. Return `yyyy-MM` of anchor month.

### range derivation
Given `monthId` and `startDay`:
1. Clamp `startDay` to `1..28`.
2. Start date = `YearMonth(monthId).atDay(startDay)`.
3. End date = `startDate + 1 month - 1 day`.

## Examples
- start day = 1, date = 2026-05-01 => monthId = `2026-05`, range `2026-05-01..2026-05-31`.
- start day = 28, date = 2026-05-01 => monthId = `2026-04`, range `2026-04-28..2026-05-27`.
- start day = 28, date = 2026-05-28 => monthId = `2026-05`, range `2026-05-28..2026-06-27`.
- year boundary: start day = 28, date = 2026-01-01 => monthId = `2025-12`, range `2025-12-28..2026-01-27`.

## Preservation/revision note
- The anchor semantics are preserved.
- Revised: all modules should use one resolver contract and explicit cycle wording in UI.
- Start-day policy: future-cycle re-anchor, historical cycles preserved.
