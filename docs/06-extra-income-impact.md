# Extra Income Impact System

## 1. Purpose

The extra income impact system helps the user see how additional work, bonuses, side income, or once-off money improves their recovery journey.

The user should feel that short-term sacrifice and extra effort are producing long-term benefits.

The system should answer:

```txt
Did my extra work move me forward?
```

## 2. Product Role

This system supports the “Respect Extra Income” pillar.

It helps the user:

* Track extra income separately from regular income.
* Decide where extra money should go.
* See whether extra income helped debt, savings, goals, or spending.
* Feel encouraged when extra work produces measurable progress.
* Avoid letting extra money disappear unnoticed.

## 3. Extra Income Types

Suggested types:

* Overtime.
* Freelance work.
* Bonus.
* Side project.
* Once-off income.
* Reimbursement.
* Cash gift.
* Sale of item.
* Refund.
* Other.

## 4. Suggested Data Fields

### Extra Income Entry

```txt
id
source
income_type
amount
date_received
allocation_type
linked_debt_id
linked_goal_id
notes
created_at
updated_at
```

## 5. Allocation Types

Extra income should be assigned to an outcome.

Suggested allocation types:

* Debt payment.
* Emergency fund.
* Goal contribution.
* Living expenses.
* Personal reward.
* Buffer.
* Other.

The app should not shame the user for using some extra income personally. It should simply make the trade-off visible.

## 6. Impact Summary

The app should summarize extra income impact by month.

Example:

```txt
Extra Income This Month
Total: R3,500
Debt recovery: R2,000
Savings/goals: R1,000
Spending/personal: R500
```

If linked to debt:

```txt
Your extra income reduced debt by R2,000 this month.
That moved your debt-free journey forward.
```

If linked to goals:

```txt
Your extra income added R1,000 to your emergency fund goal.
```

## 7. Recovery Framing

Extra income should feel meaningful.

Good:

```txt
That extra work created real movement. R1,500 went directly into your recovery plan.
```

Avoid:

```txt
You should have used all extra income for debt.
```

## 8. Suggested Dashboard Card

```txt
Extra Income Impact
R2,000 earned this month
R1,500 used for recovery
Main impact: Credit Card boss reduced
```

## 9. Linked Actions

Extra income may optionally create or link to:

* Debt payment.
* Goal contribution.
* Emergency fund contribution.
* Allowance buffer.

The user should be able to record extra income first and allocate it later.

## 10. XP Events

Possible XP rewards:

* Add first extra income entry.
* Allocate extra income to debt.
* Allocate extra income to emergency fund.
* Allocate extra income to a goal.
* Record side income for multiple months.
* Use extra income to move debt-free date earlier.

## 11. Edge Cases

Handle:

* Extra income with no allocation yet.
* Partial allocation.
* Allocation split across debt and goals.
* Linked debt deleted.
* Linked goal deleted.
* Refunds or reimbursements that should not count as true extra income.
* Negative adjustments.
* Duplicate entries.

## 12. Future Enhancements

* Split allocation across multiple outcomes.
* Extra income trend chart.
* Side income source history.
* “What if I earn R500 extra?” simulator.
* Link extra income directly to debt boss attacks.
* Monthly extra income streaks.

## 13. Implementation Status

Status on 2026-05-01: first foundation implemented.

Included:

* Local extra income entry storage.
* Income type and allocation type tracking.
* Monthly impact summary.
* Recovery, debt, savings/goals/buffer, personal/living, and unallocated buckets.
* Optional linked debt context for debt payment allocations.
* Extra Income screen accessed from More.
* Repository tests for monthly bucket summaries and linked debt display.

Moved to post-1.0 roadmap:

* Split allocations across multiple outcomes.
* Automatic linked debt payment/action creation.
* Goal contribution linking after goals exist.
* Dashboard extra income impact card.
* Trend chart and side income source history.
* Migration tests and UI workflow tests.

## 14. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered extra income storage, source/type capture, allocation buckets, monthly impact summary, optional debt context, and the Extra Income screen.

Remaining improvements are now tracked in:

* `TASK-017` for split allocations, linked debt/goal actions, source history, trends, and dashboard integration.
* `TASK-019` for XP and recovery report integration.
* `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 06.
