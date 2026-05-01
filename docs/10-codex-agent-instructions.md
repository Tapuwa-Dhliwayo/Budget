# Mini Budget Codex Agent Instructions

## 1. Required Reading

Before implementing any Mini Budget feature, agents must read:

1. `/docs/mini-budget/01-product-vision.md`
2. `/docs/mini-budget/10-codex-agent-instructions.md`

For feature-specific work, agents must also read the relevant feature specification file.

Examples:

- Weekly allowance work: read `03-weekly-allowance-system.md`.
- Debt work: read `04-debt-boss-system.md`.
- Net worth work: read `05-net-worth-system.md`.
- Extra income work: read `06-extra-income-impact.md`.
- Goals work: read `07-goals-and-feasibility.md`.
- Gamification work: read `08-gamification-rules.md`.
- Backup/security work: read `09-offline-first-and-privacy.md`.

If a feature spec does not exist yet, create a short implementation plan before changing code.

## 2. Product Direction

Mini Budget is a private, offline-first financial recovery companion.

The app helps the user:

- Log expenses honestly.
- Manage a weekly allowance.
- Pay down debts.
- Track net worth.
- Understand extra income impact.
- Set feasible goals.
- Build better financial habits.
- Stay motivated through gamification.

The app must feel encouraging, calm, honest, and supportive.

The app must not shame the user.

## 3. Tone Rules for User-Facing Text

All user-facing messages must follow the Mini Budget tone.

Use wording that is:

- Encouraging.
- Practical.
- Clear.
- Non-judgmental.
- Coach-like.
- Uplifting but honest.

Avoid wording that is:

- Harsh.
- Condemning.
- Sarcastic.
- Fear-based.
- Overly clinical.
- Overly childish.

Bad:

```txt
You failed your budget.
```

Good:

```txt
This week went over plan, but you logged honestly. Let’s use that information to make next week easier.
```

Bad:

```txt
You wasted too much money on takeaways.
```

Good:

```txt
Takeaways are putting pressure on your allowance. Reducing this category by R300 could give you more room for debt recovery.
```

## 4. Offline-First Rules

Mini Budget must work offline by default.

Do not add features that require internet access unless explicitly requested.

Do not add bank syncing.
Do not add automatic transaction import.
Do not add AI cloud analysis.
Do not add shared household budgets.
Do not require account creation.

Future cloud sync may be added only as an optional feature for syncing between the user’s own devices.

The app must preserve local-first usage even if sync is added later.

## 5. Data Responsibility Rules

The user owns their data.

Features should support:

- Local storage.
- Backup export.
- Backup import.
- Data deletion.
- PIN or biometric protection.
- Optional encryption.

Agents must avoid implementing features that lock user data into a remote service.

## 6. Financial Guidance Boundaries

Mini Budget can provide budgeting guidance, projections, warnings, and simulations.

Mini Budget must not pretend to be a certified financial advisor.

Use language like:

```txt
This may help you recover faster.
```

Avoid language like:

```txt
You must do this.
```

The app can suggest that a goal is risky, but it should not block user choice unless the feature explicitly requires validation.

## 7. Core Feature Hierarchy

When choosing between possible improvements, prioritize in this order:

1. Weekly allowance and spending control.
2. Debt tracking and debt boss progress.
3. Net worth tracking.
4. Extra income impact.
5. Goal feasibility.
6. Gamification.
7. Reports, backups, and security.
8. Optional sync.

Do not prioritize visual polish over financial correctness.
Do not prioritize gamification over useful financial behavior.
Do not add complexity before the core recovery loop works.

## 8. Recovery Loop Implementation Rule

Features should support this loop:

```txt
Face the truth
→ Make a weekly plan
→ Log honestly
→ See pressure early
→ Recover without shame
→ Pay debt
→ Watch net worth improve
→ Feel encouraged to continue
```

If a proposed change does not support this loop, question whether it belongs in the current version.

## 9. Gamification Rules

Gamification must be tied to real financial actions.

Good gamification events:

- Logging an expense.
- Completing a weekly review.
- Staying within weekly allowance.
- Paying debt.
- Making an extra debt payment.
- Adding extra income.
- Allocating extra income to debt or savings.
- Reducing overspending in a category.
- Backing up data.

Bad gamification events:

- Rewarding meaningless taps.
- Rewarding fake progress.
- Encouraging hiding expenses.
- Penalizing the user harshly for failure.
- Making the user feel ashamed.

Important:

> Reward recovery, not only perfection.

The user should earn progress for honest correction after a difficult week.

## 10. Debt Boss Rules

Debt should be treated as a first-class feature.

Each debt should support:

- Name.
- Type.
- Starting balance.
- Current balance.
- Interest rate.
- Minimum payment.
- Payment due date.
- Debt review flag.
- Strategy.
- Payment history.
- Notes.

Debt strategies should include:

- Snowball.
- Avalanche.
- Hybrid.

Hybrid should generally be treated as the recommended emotional-practical default unless the feature spec says otherwise.

Debt boss rules:

- Current balance represents boss HP.
- Payments reduce boss HP.
- Interest may increase boss HP.
- Extra payments should feel like bonus damage.
- Defeating a debt should be celebrated.

## 11. Weekly Allowance Rules

The weekly allowance is the main short-term spending control system.

It should help the user understand:

- Weekly spending limit.
- Amount spent.
- Amount remaining.
- Days left.
- Safe daily amount.
- Whether spending is stable, pressured, or critical.
- What can be adjusted to recover.

The weekly allowance should not shame overspending.
It should detect pressure early and suggest recovery actions.

## 12. Net Worth Rules

Net worth should be tracked as:

```txt
Net Worth = Assets - Debts
```

Net worth features should show:

- Current net worth.
- Monthly snapshot.
- Change over time.
- Asset contribution.
- Debt reduction contribution.

Net worth should help the user see that progress is happening even while debt remains.

## 13. Extra Income Rules

Extra income should be tracked separately from regular income.

Each extra income entry should support:

- Source.
- Amount.
- Date.
- Allocation.
- Notes.

Allocation options:

- Debt payment.
- Emergency fund.
- Goal contribution.
- Living expenses.
- Personal reward.
- Other.

The app should show the impact of extra income on recovery.

Example:

```txt
Your extra income helped reduce debt by R1,500 this month.
```

## 14. Goal Rules

Goals should be user-owned and customizable.

The app may provide archetypes:

- Emergency fund.
- Debt payoff.
- Phone, laptop, or car.
- Rent deposit.
- Holiday or travel.
- School fees.
- Home improvement.
- Investment.
- Custom.

Each goal should support:

- Target amount.
- Target date.
- Required monthly contribution.
- Feasibility status.
- Progress.
- Milestones.
- Optional category or purpose.

The app may classify goals as:

- Healthy.
- Neutral.
- Risky.

Goal guidance must be supportive.

Bad:

```txt
This is a bad goal.
```

Good:

```txt
This goal may slow your debt recovery. You can still choose it, but consider lowering the target or delaying it until your high-interest debt is under control.
```

## 15. Implementation Style Rules

When implementing features:

- Keep changes small and focused.
- Preserve existing app functionality.
- Avoid large rewrites unless explicitly requested.
- Prefer clear data models over clever abstractions.
- Add meaningful validation.
- Keep calculations testable.
- Avoid hardcoding financial assumptions where settings would be better.
- Use simple explanations for calculated values.

Financial calculations should be placed in reusable helper/service layers where possible, not buried directly inside UI code.

## 16. Testing Rules

For each financial feature, test at least:

- Empty state.
- Normal valid data.
- Zero values.
- Large values.
- Missing optional fields.
- Editing existing records.
- Deleting records.
- Calculation edge cases.

For debt features, test:

- Minimum payment only.
- Extra payment.
- Interest rate omitted.
- Interest rate present.
- Debt review flag enabled.
- Debt fully paid.

For allowance features, test:

- Under allowance.
- Near allowance.
- Over allowance.
- No expenses yet.
- Final day of week.
- New week transition.

## 17. UX Rules

Every major screen should answer one clear question.

Examples:

- Overview: “Am I okay right now?”
- Weekly Allowance: “How much can I safely spend this week?”
- Debt Boss: “How close am I to defeating this debt?”
- Net Worth: “Am I becoming financially stronger?”
- Extra Income: “Did my extra work move me forward?”
- Goals: “Is this target realistic?”

Avoid screens that are only lists of numbers without interpretation.

## 18. Empty State Rules

Empty states should invite the next helpful action.

Bad:

```txt
No debts found.
```

Good:

```txt
No debts added yet. Add your first debt boss so Mini Budget can help you track progress and plan your recovery.
```

Bad:

```txt
No expenses.
```

Good:

```txt
No expenses logged this week. Start with your next purchase so your allowance stays accurate.
```

## 19. Do Not Add

Unless explicitly requested, do not add:

- Bank integrations.
- Remote AI features.
- Social sharing.
- Shared budgets.
- Investment trading features.
- Crypto tracking.
- Complex accounting reports.
- Multi-currency support unless needed.
- Business finance features.

## 20. Agent Completion Checklist

Before finishing a feature, confirm:

- The feature supports the product vision.
- The feature works offline.
- Existing screens still work.
- User-facing text follows the supportive tone.
- Financial calculations are understandable.
- Empty states are helpful.
- Edge cases are handled.
- The implementation does not require cloud services.
- The feature does not shame the user.
- The feature passes feature tests and unit tests
---
