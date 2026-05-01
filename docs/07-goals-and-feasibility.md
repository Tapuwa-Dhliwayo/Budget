# Goals and Feasibility System

## 1. Purpose

The goals system helps the user plan for future needs and wants while staying honest about their current financial recovery.

The app should support ambition without encouraging harmful decisions.

The system should answer:

```txt
Is this goal realistic for my current financial situation?
```

## 2. Product Role

The goals system supports the “Grow Through Goals” pillar.

It helps the user:

* Create financial goals.
* Choose goal archetypes.
* Track progress.
* Understand monthly contribution requirements.
* See whether a goal is feasible.
* Understand whether a goal helps or hurts recovery.
* Make better trade-offs.

## 3. Goal Archetypes

Suggested archetypes:

* Emergency fund.
* Debt payoff.
* Phone, laptop, or car.
* Rent deposit.
* Holiday or travel.
* School fees.
* Home improvement.
* Investment.
* Custom goal.

Archetypes should be templates only. The user must be able to customize goals.

## 4. Suggested Data Fields

### Goal

```txt
id
name
goal_type
target_amount
current_amount
target_date
monthly_contribution_target
priority
health_classification
status
notes
created_at
updated_at
completed_at
```

### Goal Contribution

```txt
id
goal_id
amount
contribution_date
source_type
linked_extra_income_id
notes
created_at
updated_at
```

## 5. Goal Status Values

Suggested statuses:

* Active.
* Paused.
* Completed.
* Cancelled.
* Deferred.

## 6. Goal Health Classification

The app may classify a goal as:

* Healthy.
* Neutral.
* Risky.

## Healthy Goals

Goals that improve stability or recovery.

Examples:

* Emergency fund.
* Debt payoff.
* Skills/course that improves earning ability.
* Essential replacement item.

## Neutral Goals

Goals that are fine if affordable.

Examples:

* Holiday.
* Hobby item.
* Phone upgrade.
* Home decor.

## Risky Goals

Goals that may slow recovery or create pressure.

Examples:

* Expensive want while high-interest debt is unpaid.
* Luxury purchase while weekly allowance is under pressure.
* New car deposit while emergency fund is zero.
* Goal requiring unrealistic monthly contributions.

## 7. Feasibility Calculation

Basic calculation:

```txt
Remaining Amount = Target Amount - Current Amount
Months Remaining = Months until Target Date
Required Monthly Contribution = Remaining Amount / Months Remaining
```

Then compare required contribution to available money.

Possible statuses:

* Feasible.
* Tight.
* Risky.
* Not feasible with current plan.

## 8. Feasibility Messaging

Good:

```txt
This goal is possible, but it will be tight. You would need about R850 per month to reach it on time.
```

Good:

```txt
This goal may slow your debt recovery. You can still choose it, but consider lowering the target or delaying the date.
```

Bad:

```txt
This is a bad goal.
```

Bad:

```txt
You cannot afford this.
```

## 9. What-If Simulator

The goal system should eventually support simple simulations.

Examples:

```txt
What if I reduce takeaways by R300?
What if I add R500 extra income?
What if I delay this goal by 3 months?
What if I reduce the target by R1,000?
```

Outputs:

* New required monthly contribution.
* Feasibility status change.
* Debt recovery impact if relevant.

## 10. Goal Priority

Suggested priority values:

* Critical.
* High.
* Medium.
* Low.

Emergency fund and debt payoff goals may be recommended as higher priority when the user has high debt pressure.

The user should be able to override priority.

## 11. Goal Progress Display

Example:

```txt
Emergency Fund
R2,500 of R10,000 saved
25% complete
Target date: December 2026
Required monthly contribution: R625
Status: Feasible
```

Risky example:

```txt
Holiday
R500 of R12,000 saved
Target date: October 2026
Required monthly contribution: R1,917
Status: Risky
This may put pressure on your debt recovery plan.
```

## 12. Milestones

Suggested milestones:

* Goal created.
* First contribution.
* 25% complete.
* 50% complete.
* 75% complete.
* Goal completed.
* Goal made feasible after adjustment.

## 13. XP Events

Possible XP rewards:

* Create a healthy goal.
* Add first contribution.
* Reach milestone.
* Complete goal.
* Adjust risky goal into feasible range.
* Use extra income for goal contribution.
* Pause a risky goal to protect debt recovery.

## 14. Edge Cases

Handle:

* Target amount is zero.
* Current amount exceeds target amount.
* Target date is in the past.
* No target date.
* No available money calculation yet.
* Goal linked to deleted extra income entry.
* Goal paused.
* Goal cancelled.
* Goal completed early.

## 15. Future Enhancements

* Goal recommendations.
* Goal archetype templates.
* Goal timeline chart.
* Goal/debt conflict warnings.
* Goal contribution automation prompts.
* Goal milestone badges.

## 16. Implementation Status

Status on 2026-05-01: first foundation implemented.

Included:

* Local goal storage.
* Goal contribution storage.
* Goal archetype/type, priority, status, health classification, target date, and monthly contribution target.
* Progress percentage and remaining amount.
* Required monthly contribution calculation.
* Feasibility classification by comparing required monthly contribution to the user's planned monthly contribution target.
* Contribution workflow that updates progress and completes goals when the target is reached.
* Goals screen accessed from More.
* Repository tests for feasibility, unrealistic plans, contribution progress, and completion.

Moved to post-1.0 roadmap:

* Goal editing and status changes for pause, cancel, and defer.
* Extra income to goal contribution linkage.
* Goal recommendations and archetype templates.
* What-if simulator.
* Goal/debt conflict warnings.
* Milestone badges and XP events.
* Migration tests and UI workflow tests.

## 17. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered goal storage, contributions, progress, required monthly contribution, feasibility classification, health classification, completion behavior, and the Goals screen.

Remaining improvements are now tracked in:

* `TASK-018` for goal lifecycle flows, extra-income links, what-if planning, recommendations, and milestones.
* `TASK-019` for XP and achievement integration.
* `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 07.
