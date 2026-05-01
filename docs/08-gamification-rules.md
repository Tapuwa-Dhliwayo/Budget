# Gamification Rules

## 1. Purpose

Gamification in Mini Budget exists to make financial recovery more motivating, visible, and emotionally sustainable.

The game systems should support real financial behavior. They should never distract from the recovery goal.

The system should answer:

```txt
How do I stay motivated while doing difficult financial work?
```

## 2. Product Role

Gamification supports all major product pillars:

* Face Reality.
* Survive the Week.
* Defeat Debt.
* Build Net Worth.
* Respect Extra Income.
* Grow Through Goals.

Gamification should make progress feel alive without making the app childish or unserious.

## 3. Core Rule

```txt
Reward useful recovery behavior, not meaningless activity.
```

Good rewards:

* Logging honestly.
* Completing reviews.
* Staying within allowance.
* Recovering after overspending.
* Paying debt.
* Making extra payments.
* Building savings.
* Improving net worth.
* Backing up data.

Bad rewards:

* Random tapping.
* Hiding bad spending.
* Opening the app without useful action.
* Punishing the user harshly for failure.

## 4. Recovery Over Perfection

The most important gamification rule:

```txt
Reward recovery, not only perfection.
```

The user must still feel encouraged after a difficult week.

Example:

```txt
You went over plan, but you logged every expense and completed your review. Recovery XP earned.
```

This prevents the app from becoming another source of shame.

## 5. XP System

XP should represent discipline, honesty, and recovery.

Suggested XP categories:

* Logging XP.
* Allowance XP.
* Debt XP.
* Goal XP.
* Net Worth XP.
* Recovery XP.
* Backup XP.

## 6. Suggested XP Events

### Logging

* Log an expense.
* Log expenses for 3 days in a row.
* Complete a full week of honest logging.

### Weekly Allowance

* Set weekly allowance.
* Stay within allowance.
* Recover from pressured status.
* Complete weekly review.

### Debt

* Add debt boss.
* Log payment.
* Make extra payment.
* Reduce total debt.
* Defeat debt.

### Net Worth

* Create snapshot.
* Improve net worth.
* Improve net worth multiple months in a row.

### Extra Income

* Log extra income.
* Allocate extra income to debt.
* Allocate extra income to savings or goals.

### Goals

* Create goal.
* Reach milestone.
* Complete goal.
* Adjust risky goal into feasible range.

### Recovery

* Log honestly after overspending.
* Complete review after bad week.
* Reduce spending after a pressured week.
* Make partial debt payment instead of skipping.

## 7. Levels and Titles

Ranks should reflect recovery and discipline.

Suggested titles:

1. Facing Reality.
2. Budget Rookie.
3. Expense Watcher.
4. Allowance Defender.
5. Debt Fighter.
6. Habit Builder.
7. Recovery Strategist.
8. Net Worth Climber.
9. Financial Guardian.

Ranks should not be based on wealth or income.

## 8. Debt Boss Battles

Debt boss fights are the strongest gamification feature.

Rules:

```txt
Debt balance = boss HP
Payments = damage
Extra payments = bonus damage
Interest/fees = regeneration
Debt paid off = boss defeated
```

Debt bosses should feel encouraging, not silly.

Good message:

```txt
You dealt R1,200 damage to your Credit Card boss this month.
```

Celebration:

```txt
Debt defeated. One less weight on your future.
```

## 9. Weekly Allowance Shield

The weekly allowance shield represents the user’s spending capacity for the week.

Rules:

* Spending reduces shield.
* Remaining allowance keeps shield active.
* Overspending breaks shield.
* Recovery planning repairs future shields.

Message example:

```txt
Your allowance shield is under pressure, but you still have time to protect the week.
```

## 10. Badges

Suggested badges:

* First Honest Log.
* First Week Tracked.
* Allowance Defender.
* Recovery After Pressure.
* First Debt Attack.
* Extra Payment Strike.
* Debt Boss Defeated.
* Net Worth Improved.
* Emergency Fund Started.
* Backup Guardian.

Badges should be meaningful and tied to real actions.

## 11. Streaks

Streaks can be useful but must be designed carefully.

Possible streaks:

* Expense logging streak.
* Weekly review streak.
* Debt payment streak.
* Backup streak.

Important:

Do not make streak loss feel devastating.

Good:

```txt
Your streak paused, but your recovery did not. Start again today.
```

Bad:

```txt
You lost everything.
```

## 12. Monthly Summary

A monthly summary should feel like a battle report.

Example:

```txt
Monthly Recovery Report
Expenses logged: 58
Debt reduced: R2,450
Extra income used for recovery: R1,500
Net worth movement: +R1,840
Strongest habit: logging honestly
Next focus: reduce takeaways by R300
```

## 13. Anti-Shame Rules

Never use:

* Failure language.
* Insults.
* Guilt-based messaging.
* Harsh punishment.
* Permanent loss framing.

Always prefer:

* Recovery language.
* Honest reflection.
* Next action guidance.
* Encouragement.
* Small wins.

## 14. Edge Cases

Handle:

* User overspends repeatedly.
* User stops logging for weeks.
* User debt increases.
* User deletes data.
* User edits old records.
* User cannot make a debt payment.
* User has negative net worth.
* User has no goals.
* User disables gamification.

## 15. Future Enhancements

* Avatar or profile growth.
* Debt boss visuals.
* Monthly battle report screen.
* Custom titles.
* Achievement gallery.
* Challenge system.
* No-spend challenges.
* Category-specific quests.

## 16. Implementation Status

Status on 2026-05-01: first foundation implemented.

Included:

* Recovery XP total stored on the gamification record.
* Gamification event history table.
* Recovery XP event types for logging, weekly allowance, debt, net worth, extra income, goals, recovery actions, and backups.
* Level and title calculation using the Task 8 title ladder.
* Expanded badge definitions.
* Expense logging automatically awards XP and keeps existing streak behavior.
* Recovery Progress screen accessed from More.
* Repository tests for XP awards, mapped badges, event history, and level/title calculation.

Moved to post-1.0 roadmap:

* Automatic XP wiring across weekly review, debt, net worth, extra income, goals, backups, and recovery actions.
* Monthly battle report screen.
* Achievement gallery.
* Challenge system.
* Optional gamification disable setting.
* Migration tests and UI workflow tests.

## 17. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered recovery XP storage, XP events, levels/titles, expanded badges, expense XP wiring, and the Recovery Progress screen.

Remaining improvements are now tracked in:

* `TASK-019` for XP wiring across all recovery systems, monthly battle report, achievement detail, and duplicate-prevention rules.
* `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 08.
