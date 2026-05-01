## File: `/docs/mini-budget/05-net-worth-system.md`

# Net Worth System

## 1. Purpose

The net worth system helps the user see long-term financial recovery.

Debt repayment can feel slow and discouraging. The user may still feel stuck even when they are making good choices. Net worth tracking gives visible evidence that the user is becoming financially stronger over time.

The system should answer:

```txt
Am I becoming financially stronger?
```

## 2. Product Role

The net worth system supports the “Build Net Worth” pillar.

It helps the user:

* See the bigger financial picture.
* Track assets and debts together.
* Understand whether their position is improving.
* Separate short-term spending stress from long-term recovery.
* See debt reduction as real progress.
* See savings and asset growth as real progress.

## 3. Core Formula

```txt
Net Worth = Total Assets - Total Debts
```

The formula must be simple, visible, and understandable.

## 4. Asset Types

Suggested asset types:

* Bank account balance.
* Emergency fund.
* Cash.
* Investments.
* Retirement fund.
* Property estimated value.
* Vehicle estimated value.
* Business asset.
* Other.

## 5. Debt Types Included

The net worth system should include all debts, including:

* Credit cards.
* Personal loans.
* Store accounts.
* Vehicle finance.
* Mortgage.
* Family or friend loans.
* Debt review accounts.
* Other debts.

The debt boss system can focus emotionally on recovery debts, but net worth should include the full financial picture.

## 6. Mortgage Handling

A mortgage should be included in net worth calculations.

If the user also records the property value, the property and mortgage should appear together in the long-term picture.

Example:

```txt
Property value: R900,000
Mortgage balance: R780,000
Estimated property equity: R120,000
```

The app should not treat mortgage debt with the same emotional tone as harmful consumer debt unless the user chooses to.

## 7. Suggested Data Fields

### Asset

```txt
id
name
type
current_value
notes
created_at
updated_at
is_active
```

### Net Worth Snapshot

```txt
id
snapshot_date
total_assets
total_debts
net_worth
notes
created_at
updated_at
```

### Optional Snapshot Detail

```txt
id
snapshot_id
item_type: asset/debt
item_id
name
value
created_at
updated_at
```

Snapshot details are useful because they preserve historical values even when the current asset or debt changes later.

## 8. Snapshot Philosophy

Net worth should be recorded as snapshots over time.

The current net worth can be calculated live, but historical net worth should be stored as snapshots.

Recommended snapshot frequency:

* Monthly by default.
* Manual snapshot allowed.
* Optional reminder at month end.

## 9. Dashboard Summary

Example:

```txt
Net Worth
Current: -R185,400
This month: +R1,840
Debt reduced: R1,200
Assets increased: R640
```

The app should frame negative net worth carefully.

Good:

```txt
Your net worth is still negative, but it improved by R1,840 this month. That is real movement.
```

Bad:

```txt
You are worth -R185,400.
```

## 10. Progress Interpretation

The system should explain movement.

Possible movement reasons:

* Assets increased.
* Assets decreased.
* Debt decreased.
* Debt increased.
* New debt added.
* Asset value updated.
* Mortgage balance changed.

Example:

```txt
Your net worth improved mainly because your total debt decreased by R1,200.
```

## 11. Emotional Tone

Net worth can be emotionally sensitive.

The app must not shame the user for negative net worth.

Use:

```txt
Your position is improving.
```

Avoid:

```txt
You are still far behind.
```

## 12. Net Worth Milestones

Celebrate meaningful milestones:

* First snapshot created.
* Net worth improved month over month.
* Net worth improved three months in a row.
* Negative net worth reduced by 10%.
* Net worth crosses zero.
* Asset total reaches a target.
* Debt-to-asset ratio improves.

Example:

```txt
Your net worth improved for the third month in a row. That is a recovery trend.
```

## 13. XP Events

Possible XP rewards:

* Create first net worth snapshot.
* Update asset values.
* Complete monthly snapshot.
* Improve net worth month over month.
* Reduce total debt.
* Increase emergency fund.
* Reach positive net worth.

## 14. Edge Cases

Handle:

* No assets recorded.
* No debts recorded.
* Negative net worth.
* Asset value set to zero.
* Debt value set to zero.
* Property value missing.
* Mortgage exists without property asset.
* Property asset exists without mortgage.
* User edits old snapshot.
* User deletes asset used in old snapshot.
* User deletes debt used in old snapshot.

Historical snapshots should remain understandable even if current records are deleted or changed.

## 15. Future Enhancements

* Net worth trend chart.
* Asset allocation view.
* Debt-to-asset ratio.
* Property equity tracking.
* Manual valuation history.
* Month-end financial review.
* Net worth projection.

---

## 16. Implementation Status

Status on 2026-05-01: first foundation implemented.

Included:

* Active asset storage.
* Current net worth calculation using `Assets - current debt boss balances`.
* Manual snapshot creation.
* Recent snapshot list and movement interpretation.
* Net Worth screen accessed from More.
* Repository tests for net worth formula and snapshot movement.

Moved to post-1.0 roadmap:

* Snapshot detail rows for each asset/debt at the time of snapshot.
* Dashboard net worth summary.
* Richer trend visualizations and milestone celebrations.
* Asset deletion/archive workflow.
* Migration tests and UI workflow tests.

## 17. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered asset storage, current net worth calculation, manual snapshots, recent movement interpretation, and the Net Worth screen.

Remaining improvements are now tracked in:

* `TASK-016` for snapshot details, dashboard integration, richer trends, and milestones.
* `TASK-019` for XP and achievement integration.
* `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 05.
