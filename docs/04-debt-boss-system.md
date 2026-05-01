# Debt Boss System

## 1. Purpose

The debt boss system turns debt repayment into a visible, motivating recovery journey.

Debt is emotionally heavy. The goal is to make debt feel measurable, fightable, and eventually defeatable.

The system should answer:

```txt
What am I fighting, how much progress have I made, and what action helps me defeat it faster?
```

## 2. Product Role

The debt boss system supports the “Defeat Debt” pillar.

It helps the user:

- List all debts honestly.
- Understand balances and minimum payments.
- Choose a repayment strategy.
- See projected debt-free dates.
- Simulate extra payments.
- Track payment history.
- Celebrate progress.
- Stay encouraged over a long recovery journey.

## 3. Core Debt Fields

Each debt should support:

- Debt name.
- Debt type.
- Starting balance.
- Current balance.
- Interest rate.
- Minimum payment.
- Payment due date.
- Debt review flag.
- Strategy.
- Payment history.
- Notes.
- Created date.
- Closed/defeated date.

## 4. Debt Types

Suggested debt types:

- Credit card.
- Personal loan.
- Store account.
- Vehicle finance.
- Mortgage.
- Family or friend loan.
- Debt review account.
- Other.

## 5. Debt Review Support

The app should support debts under debt review.

Debt review debts may behave differently depending on the user’s situation.

The app should allow:

- Mark debt as under debt review.
- Track arranged payment.
- Track interest if applicable.
- Track whether the balance still changes.
- Add notes about the arrangement.

Important: do not assume debt review means no interest. Allow the user to configure whether interest applies.

## 6. Boss Metaphor

Each debt is represented as a boss.

```txt
Current balance = Boss HP
Payment = Damage
Extra payment = Bonus damage
Interest = Regeneration
Debt fully paid = Boss defeated
```

Example:

```txt
Boss: Credit Card
Starting HP: R42,000
Current HP: R38,750
Damage dealt this month: R1,250
Minimum damage: R900
Extra damage: R350
Projected defeat: March 2028
```

## 7. Repayment Strategies

The app should support three strategies.

## Snowball

Pay smallest debt first.

Best for:

- Motivation.
- Quick wins.
- Emotional momentum.

## Avalanche

Pay highest interest debt first.

Best for:

- Mathematical efficiency.
- Reducing interest cost.
- Users with strong discipline.

## Hybrid

Balance motivation and interest risk.

Recommended default.

Suggested hybrid logic:

- Prioritize small debts when they can be defeated quickly.
- Prioritize high-interest debts when interest is causing serious damage.
- Warn if a dangerous debt is ignored for too long.

Example:

```txt
Your smallest debt can be defeated quickly, but your credit card has the highest interest. A hybrid plan could clear the small debt first, then focus extra payments on the credit card.
```

## 8. Payment History

Each payment should support:

- Debt ID.
- Amount.
- Date.
- Payment type.
- Notes.

Payment types:

- Minimum payment.
- Extra payment.
- Settlement payment.
- Adjustment.
- Interest charge.
- Fee.

The app should distinguish payments from balance adjustments.

## 9. Debt-Free Projection

The app should estimate when a debt will be paid off.

Initial projection can be simple:

```txt
Months To Payoff = Current Balance / Monthly Payment
```

Later projection can include interest.

Projection messaging must be careful:

```txt
At the current payment rate, this debt may be defeated around March 2028.
```

Avoid overconfident wording:

```txt
This debt will be paid off in March 2028.
```

## 10. Extra Payment Simulator

The simulator should show how extra payments change the outcome.

Inputs:

- Extra once-off payment.
- Extra monthly payment.
- Target debt.

Outputs:

- New projected payoff date.
- Time saved.
- Interest potentially reduced if supported.

Example:

```txt
Adding R500 extra per month could defeat this debt 7 months earlier.
```

## 11. Monthly Debt Battle Summary

At the end of each month, show a summary.

Example:

```txt
Debt Battle Summary
Total debt reduced: R2,450
Minimum payments: R1,800
Extra payments: R650
Interest/fees added: R320
Net damage dealt: R2,130
Strongest attack: Credit Card extra payment
```

This should feel encouraging even if progress is slow.

## 12. Warnings

Warnings should be caring and practical.

Minimum payment warning:

```txt
Minimum payments are keeping this debt active for a long time. Even an extra R200 per month could help you defeat it sooner.
```

Interest warning:

```txt
Interest is adding pressure to this debt. Consider making this boss a higher priority if possible.
```

Debt review warning:

```txt
This debt is marked as under debt review. Check whether interest or fees are still being added so your projection stays accurate.
```

## 13. Celebration Moments

Celebrate meaningful milestones:

- First debt added.
- First payment logged.
- First extra payment.
- 10% paid.
- 25% paid.
- 50% paid.
- 75% paid.
- Debt defeated.
- Total debt reduced this month.
- Debt-free date moved earlier.

Celebration should be warm, not childish.

Example:

```txt
You defeated this debt. That is real progress. One less weight on your future.
```

## 14. XP Events

Possible XP rewards:

- Add a debt boss.
- Log a debt payment.
- Make an extra payment.
- Complete a monthly debt review.
- Reduce total debt.
- Defeat a debt.
- Move debt-free date earlier.
- Add interest/fee honestly.

## 15. Edge Cases

Handle:

- Interest rate missing.
- Minimum payment missing.
- Current balance greater than starting balance.
- Debt balance reaches zero.
- Debt balance becomes negative due to overpayment.
- User edits starting balance.
- User deletes payment history.
- User adds fee or interest.
- Debt under review.
- Mortgage included with other debts.

## 16. Mortgage Handling

Mortgage can be tracked as debt, but it should not always be emotionally treated the same as harmful consumer debt.

The app should distinguish:

- Recovery debts: credit card, personal loans, store accounts.
- Long-term asset-backed debts: mortgage, vehicle finance.

The mortgage still affects net worth, but it may not be the main boss fight unless the user chooses to treat it that way.

Example:

```txt
Your mortgage is part of your net worth picture, but your personal debts are the main recovery bosses right now.
```

## 17. Future Enhancements

- Interest-aware amortization.
- Debt strategy comparison.
- Debt-free timeline chart.
- Settlement offer tracking.
- Debt review plan tracking.
- Creditor contact notes.
- Snowball queue.
- Avalanche queue.
- Hybrid recommendation engine.

## 18. Implementation Status

### 2026-05-01 - Debt Boss Foundation

Implemented:

- Local debt boss storage.
- Local payment/action history storage.
- Debt types.
- Repayment strategies: snowball, avalanche, hybrid.
- Debt review flag and interest-still-applies flag.
- Payment/action types: minimum, extra, settlement, adjustment, interest, fee.
- Boss HP display using current balance.
- Damage dealt and progress percentage.
- Simple payoff projection using current balance divided by minimum payment.
- Monthly debt battle summary.
- Bottom-nav Debt Bosses screen.
- Add debt boss dialog.
- Log payment, extra payment, interest, and fee actions.
- Unit tests for adding debt, payment damage, and interest regeneration.

Moved to post-1.0 roadmap:

- Interest-aware amortization.
- Strategy comparison engine.
- Full payment history screen.
- Editing existing debts.
- Deleting debts or payments.
- Settlement offer tracking.
- Debt-free timeline chart.
- Debt boss celebration events.
- Gamification XP integration.

## 19. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered debt boss storage, payment/action history, HP/progress, repayment strategy fields, simple projection, monthly battle summary, and the Debt Bosses screen.

Remaining improvements are now tracked in:

- `TASK-015` for debt editing, history detail, strategy comparison, amortized projection, simulator, and milestones.
- `TASK-019` for recovery XP and celebration integration.
- `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 04.
