# Weekly Allowance System

## 1. Purpose

The weekly allowance system is the main short-term discipline loop in Mini Budget.

Monthly budgets can feel too abstract when the user is trying to control day-to-day spending. Weekly allowance makes spending pressure easier to understand and easier to recover from.

The system should answer:

```txt
How much can I safely spend this week without damaging my recovery plan?
```

## 2. Product Role

The weekly allowance system supports the “Survive the Week” pillar.

It helps the user:

- Know what they can spend this week.
- See how much they have already spent.
- Understand whether they are spending too quickly.
- Recover before the month collapses.
- Stay motivated without shame.

## 3. Core Concepts

## Weekly Allowance

The amount the user can spend during the current week after accounting for fixed commitments, debt payments, savings targets, and other planned obligations.

## Spent This Week

Total expenses logged during the current weekly allowance period.

## Remaining Allowance

```txt
Remaining Allowance = Weekly Allowance - Spent This Week
```

## Safe Daily Spend

```txt
Safe Daily Spend = Remaining Allowance / Days Remaining In Week
```

This helps the user recover if they spent too quickly earlier in the week.

## Weekly Status

Possible statuses:

- Stable.
- Watchful.
- Pressured.
- Critical.
- Over plan.
- Recovered.

## 4. Suggested Status Logic

The exact thresholds can be tuned later.

Initial suggestion:

```txt
Stable: spent <= expected spend for this point in the week
Watchful: spent slightly faster than expected
Pressured: remaining allowance is low for days remaining
Critical: remaining allowance is very low
Over plan: spent exceeds weekly allowance
Recovered: user was previously over pace but adjusted enough to return to stable/watchful
```

## 5. Required Data

The weekly allowance system may need:

- Current week start date.
- Current week end date.
- Weekly allowance amount.
- Expenses within the week.
- Fixed monthly commitments.
- Required debt minimums.
- Planned savings or goal contributions.
- User payday or income cycle.
- Optional manual override.

## 6. Calculation Options

## Option A: Manual Weekly Allowance

The simplest first version.

The user manually sets their weekly allowance.

Pros:

- Easy to build.
- Easy to understand.
- Good for early versions.

Cons:

- Does not automatically adapt to income and commitments.

## Option B: Monthly Budget Derived Weekly Allowance

The app calculates weekly allowance from monthly available spending money.

Basic formula:

```txt
Monthly Flexible Money = Monthly Income - Fixed Commitments - Debt Minimums - Planned Savings
Weekly Allowance = Monthly Flexible Money / Number Of Weeks In Month
```

Pros:

- More useful.
- Connects weekly spending to bigger recovery plan.

Cons:

- Needs better income and commitment data.

## Option C: Hybrid

The app calculates a suggested weekly allowance but lets the user override it.

Recommended approach:

```txt
Suggested Weekly Allowance = calculated by app
Chosen Weekly Allowance = user accepted or manually adjusted amount
```

This respects personal responsibility while still guiding the user.

## 7. Recommended First Implementation

Start with the hybrid model, but allow manual setup if full financial data is missing.

If income/commitment data is incomplete:

```txt
Set your weekly allowance manually for now. Mini Budget can suggest one later once your income, debts, and commitments are set up.
```

If enough data exists:

```txt
Based on your income, commitments, debt minimums, and savings plans, a safe weekly allowance is R1,450.
```

## 8. Weekly Allowance Shield

The weekly allowance can be represented visually as a shield.

The shield represents spending capacity for the week.

- Full shield: week is safe.
- Lower shield: spending pressure is rising.
- Broken shield: allowance exceeded.
- Repaired shield: future plan adjusted after overspending.

This is a gamified visual, but it must remain useful and calm.

## 9. Suggested Dashboard Card

Example:

```txt
Weekly Allowance
R680 remaining
R820 spent of R1,500
3 days left
Safe daily spend: R226
Status: Stable
```

If pressured:

```txt
Weekly Allowance
R240 remaining
R1,260 spent of R1,500
3 days left
Safe daily spend: R80
Status: Pressured
Takeaways and transport are the main pressure points this week.
```

If over plan:

```txt
Weekly Allowance
R120 over plan
R1,620 spent of R1,500
Status: Over plan
You can still recover by logging honestly and adjusting next week’s plan.
```

## 10. Recovery Behavior

The system must support recovery after overspending.

Recovery actions may include:

- Reduce spending for remaining days.
- Move non-essential planned spending to next week.
- Add extra income.
- Reallocate from a lower-priority goal.
- Accept the overspend and adjust next week.
- Add a reflection note.

The app should never imply the week is pointless once overspending happens.

Example:

```txt
This week is over plan, but it is not wasted. Because you logged honestly, you can see what caused the pressure and plan a better next week.
```

## 11. Weekly Review

At the end of each week, the app should offer a short review.

Questions:

- Did you stay within allowance?
- Which category caused the most pressure?
- What helped you spend well?
- What made spending harder?
- What should change next week?

The review should generate a weekly summary.

Example:

```txt
This week you spent R1,620 against a R1,500 allowance.
Main pressure: takeaways.
Positive action: all expenses were logged.
Suggested next step: reduce takeaways by R150 next week.
```

## 12. XP Events

Possible XP rewards:

- Set weekly allowance.
- Log first expense of the week.
- Log expenses daily.
- Complete weekly review.
- Stay within allowance.
- Recover from pressured status.
- Log honestly after exceeding allowance.

## 13. Edge Cases

Handle:

- No allowance set.
- No expenses logged.
- Week has already ended.
- Expenses edited after week ended.
- User changes week start day.
- User changes allowance mid-week.
- User deletes an expense.
- User has irregular income.
- User has negative available money.

## 14. Negative Available Money

If calculated available money is negative, do not hide it.

Example:

```txt
Your planned commitments are higher than your income. This means there is no safe weekly allowance yet. Start by reviewing commitments, debt minimums, or extra income options.
```

This must be handled gently.

## 15. Future Enhancements

- Category-specific weekly allowances.
- Payday-based cycles.
- Irregular income mode.
- Weekly allowance history.
- Weekly streaks.
- Smart category pressure detection.
- What-if allowance simulator.

## 16. Implementation Status

### 2026-05-01 - Manual Weekly Allowance Foundation

Implemented:

- Manual current-week allowance setup.
- Local Room table for weekly allowance plans.
- Dashboard Weekly Allowance card.
- Current week spending total based on logged expenses.
- Remaining allowance.
- Days remaining in the week.
- Safe daily spend.
- Status values for not set, stable, watchful, pressured, critical, and over plan.
- Supportive, non-shaming guidance text.
- Unit tests for no allowance, stable spending, and over-plan spending.
- Recurring expenses are excluded from weekly allowance spend because they represent monthly commitments.
- Category pressure detection based on non-recurring weekly spend.
- Generated recovery actions based on weekly status and main pressure category.
- Weekly review notes for what went well, what was difficult, and one adjustment for next week.
- Recent weekly allowance history on the dashboard.

Moved to post-1.0 roadmap:

- App-suggested allowance from income, fixed commitments, debt minimums, and savings.
- Payday-based or irregular income weeks.
- Recovered status based on prior pressure state.

### 2026-05-01 - Recurring Expense Treatment

Recurring expenses are not counted against weekly allowance spending. They remain part of monthly spending/analytics, but weekly allowance pressure now uses non-recurring expenses only.

### 2026-05-01 - Pressure, Review, History, and Recovery Actions

Implemented:

- Main category pressure detection for the week.
- Recovery actions generated from the weekly status and main pressure category.
- Weekly review storage and dashboard review dialog.
- Weekly allowance history from recent saved weekly allowance rows.
- Category-specific weekly allowance limits.
- Recovery action completion tracking.
- Full weekly review summary screen.
- Historical trend visualization using recent weekly allowance usage.

Moved to post-1.0 roadmap:

- Category limit templates or automatic suggestions.
- Recovery action completion history beyond current generated actions.
- More detailed trend charts and week-over-week insight text.

### 2026-05-01 - Category Limits, Review Summary, Action Tracking, and Trends

Implemented:

- Category-specific weekly limits for the current week.
- Category pressure now shows limit remaining and over-limit state when a weekly category limit exists.
- Recovery actions can be checked off and completion persists locally.
- Weekly review summary screen with overview, notes, category pressure, action completion, and historical trend bars.
- Recent weekly trend visualization based on saved weekly allowance rows.

Moved to post-1.0 roadmap:

- Suggested category limits based on history.
- Dedicated category-limit management screen.
- Richer trend charts and recovery insight copy.

## 17. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered manual weekly allowance, non-recurring weekly expenditure, category pressure, category-specific limits, weekly review, recovery actions, action completion, history, and trend visualization.

Remaining improvements are now tracked in:

- `TASK-014` for derived allowance suggestions, smarter category limits, guided review, and richer weekly insights.
- `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 03.
