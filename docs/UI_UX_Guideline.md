# Mini Budget UI/UX Agent Specification

## Document Purpose

This document gives implementation guidance for improving the visual design and user experience of **Mini Budget**.

The base features already exist. The goal is not to rebuild the application logic. The goal is to make the app feel like a cohesive, polished, gamified financial recovery app instead of a bland collection of text screens.

The agent should focus on consistent visual language, reusable UI patterns, emotional tone, improved layout, chart styling, progress feedback, and documentation of every visual change.

---

# 1. Product Vision

Mini Budget is a personal budgeting app for someone trying to recover from poor spending habits, personal debt, discouragement, and financial avoidance.

The app should help the user face their finances without shame.

The user should feel:

* “I am rebuilding.”
* “I am making progress.”
* “I can recover from bad days.”
* “My finances are hard, but not impossible.”
* “The next step is clear.”

The app must not feel like:

* A spreadsheet.
* A bank statement.
* A corporate dashboard.
* A finance lecture.
* A guilt-inducing debt tracker.
* A random collection of styled cards.

The final direction is:

## Recovery Arcade

A serious personal financial recovery app with the energy of a game HUD.

The design combines:

* The energy of an arcade/game interface.
* The emotional purpose of a recovery dashboard.
* Softer colors than harsh neon arcade themes.
* Consistent gamified language.
* Supportive, non-condemning feedback.

---

# 2. Core Design Principles

## 2.1 Recovery First

The app should frame money management as recovery and rebuilding, not punishment.

Bad spending days should not be presented as failure. They should be presented as recoverable setbacks.

Use language like:

* “Recovery action available.”
* “This round was rough.”
* “You can stabilize tomorrow.”
* “Small progress still counts.”

Avoid language like:

* “You failed.”
* “You are bad with money.”
* “Budget failed.”
* “Overspending detected” without encouragement.

## 2.2 Game Feel Without Becoming Childish

The app should feel game-inspired, not childish.

Use terms like:

* Recovery Run
* Stability Level
* Debt Boss
* Safe Spend Shield
* Weekly Missions
* Spending Damage
* Recovery XP
* Milestones
* Recovery Timeline

Do not overload every label with game language. Important financial data must remain clear.

Example:

Good:

> Debt Boss HP: R184,000 remaining

Bad:

> Evil Money Monster Rage Meter: 64% Doom Remaining

## 2.3 Consistency Over Decoration

Every screen must feel like it belongs to the same app.

The agent should avoid adding random colors, unrelated icons, inconsistent card styles, or different visual systems per screen.

All screens should share:

* Same background treatment.
* Same card surfaces.
* Same border radius scale.
* Same progress bar style.
* Same chart styling.
* Same typography hierarchy.
* Same spacing rhythm.
* Same emotional tone.

## 2.4 Progress Everywhere

The app should visually reward progress.

Prefer progress bars, rings, timelines, checklists, and cards over plain text lists.

Use visual progress for:

* Debt reduction.
* Budget remaining.
* Spending category usage.
* Weekly missions.
* Savings goals.
* Stability score.
* Monthly recovery.
* Habit streaks.

## 2.5 No Theme Switching

There should be one consistent visual style.

Do not implement dark mode and light mode variants.

The entire app should use the **Recovery Arcade** theme.

---

# 3. Recovery Arcade Visual Identity

## 3.1 Mood

The app should feel:

* Energetic.
* Focused.
* Encouraging.
* Slightly futuristic.
* Game-like.
* Safe.
* Mature.
* Polished.

It should not feel:

* Chaotic.
* Casino-like.
* Crypto-trading-like.
* Overly neon.
* Harsh.
* Childish.
* Corporate.

## 3.2 Color Palette

Use this palette consistently.

| Role             |                      Color | Usage                               |
| ---------------- | -------------------------: | ----------------------------------- |
| App Background   |                  `#111827` | Main screen background              |
| Deep Surface     |                  `#1F2937` | Larger layout sections              |
| Card Surface     |                  `#273449` | Primary card background             |
| Elevated Surface |                  `#334155` | Highlighted cards / nested elements |
| Primary          |                  `#38BDF8` | Main actions, progress highlights   |
| Primary Soft     |                  `#7DD3FC` | Soft glows, secondary highlights    |
| Accent           |                  `#FBBF24` | XP, achievements, milestones        |
| Success          |                  `#34D399` | Completed missions, positive status |
| Warning          |                  `#F59E0B` | Caution, approaching limit          |
| Danger           |                  `#F87171` | Over budget / debt danger state     |
| Main Text        |                  `#F9FAFB` | Primary readable text               |
| Muted Text       |                  `#CBD5E1` | Secondary text                      |
| Subtle Text      |                  `#94A3B8` | Hints, metadata, labels             |
| Border           |                  `#334155` | Card borders and dividers           |
| Glow Border      | `rgba(56, 189, 248, 0.35)` | Highlighted borders                 |

## 3.3 Color Usage Rules

Primary blue should indicate energy, progress, and current focus.

Accent gold should indicate rewards, XP, badges, and milestones.

Success green should indicate completion and stability.

Warning amber should indicate risk, not failure.

Danger red should be used carefully and sparingly. It should never dominate a screen.

Avoid pure black backgrounds. Use deep navy/slate instead.

Avoid pure white cards. Use dark surfaces with high-contrast text.

## 3.4 Typography

Typography should be readable and modern.

Use a clear hierarchy:

| Element             | Style                                 |
| ------------------- | ------------------------------------- |
| Screen title        | Large, bold, high contrast            |
| Section title       | Medium, semi-bold, uppercase optional |
| Metric number       | Large, bold, highly readable          |
| Labels              | Small, muted, uppercase optional      |
| Helper text         | Small to medium, supportive tone      |
| Error/recovery text | Clear, calm, non-condemning           |

Recommended behavior:

* Use large numbers for financial metrics.
* Use short descriptions under cards.
* Avoid long paragraphs on dashboard screens.
* Use supportive microcopy sparingly.

## 3.5 Shape and Spacing

Use:

* Rounded cards.
* Soft borders.
* Subtle shadows.
* Subtle glow accents.
* Compact but breathable spacing.
* Large tappable buttons.

Suggested shape scale:

| Element      |       Radius |
| ------------ | -----------: |
| Small badges | 999px / pill |
| Buttons      |  14px - 18px |
| Cards        |  20px - 28px |
| Large panels |  28px - 32px |

Avoid sharp rectangular cards unless intentionally used for small HUD-like labels.

---

# 4. Core App Language

The UI should use consistent Recovery Arcade labels.

| Current/Bland Concept | Recovery Arcade Label  |
| --------------------- | ---------------------- |
| Financial health      | Stability Level        |
| Budget remaining      | Safe Spend Shield      |
| Debt balance          | Debt Boss HP           |
| Expenses              | Spending Damage        |
| Tasks                 | Missions               |
| Savings goal          | Resource Vault         |
| Monthly summary       | Recovery Run Summary   |
| Progress              | XP / Recovery Progress |
| Habit streak          | Recovery Streak        |
| Overspending          | Shield Damage          |
| Corrective action     | Recovery Action        |

Use these terms where they improve the experience, but keep enough financial clarity so the user always knows what the number means.

Example:

Good:

> Safe Spend Shield — R420 left today

Bad:

> Shield: 420 units

---

# 5. Dashboard Experience

The dashboard is the most important screen. It should not look like a text summary. It should feel like the user’s financial recovery command center.

## 5.1 Dashboard Layout Order

Recommended dashboard order:

1. Greeting and recovery run status.
2. Stability Level card.
3. Today’s / This Week’s Missions.
4. Safe Spend Shield.
5. Debt Boss HP.
6. Spending Damage chart.
7. Recovery Timeline or Monthly Trend.
8. Recent Activity.

## 5.2 Dashboard Example Structure

```text
MINI BUDGET
Recovery Run: Month 2

[Stability Level Card]
LVL 3: Regaining Control
71%
+24 XP this week

[Today's Missions]
✓ Log all spending
○ Stay under R150 impulse spend
○ Review one debt account

[Safe Spend Shield]
R420 left today
Shield stable

[Debt Boss]
HP Remaining: 64%
R184,000 remaining

[Spending Damage]
Food        70%
Transport   30%
Wants       60%
Subscriptions 40%
```

## 5.3 Dashboard Rules

* Do not show raw text blocks where cards would work better.
* Do not show long tables on the dashboard.
* Important numbers should be large.
* Every card should have a clear purpose.
* Every visual should help the user understand what action to take next.
* Avoid showing too many financial totals at once.
* Use progressive disclosure where possible.

---

# 6. Required Reusable Components

The agent should create or refactor toward reusable UI components where appropriate.

## 6.1 AppShell

Purpose:

Provide consistent page structure, background, safe areas, and spacing.

Requirements:

* Uses Recovery Arcade background.
* Applies consistent horizontal padding.
* Handles mobile safe spacing.
* Provides consistent screen width behavior.
* Keeps all screens visually unified.

## 6.2 ScreenHeader

Purpose:

Provide title, subtitle, and optional status chip.

Example:

```text
Mini Budget
Recovery Run: Month 2
```

## 6.3 MetricCard

Purpose:

Display a financial metric in a styled card.

Examples:

* Income this month.
* Spent this month.
* Remaining safe spend.
* Debt paid.
* Savings added.

Required fields:

* Label.
* Value.
* Optional helper text.
* Optional trend.
* Optional icon.
* Optional status color.

## 6.4 ProgressCard

Purpose:

Display progress toward a goal.

Examples:

* Stability Level.
* Safe Spend Shield.
* Debt Boss HP.
* Resource Vault.

Required fields:

* Title.
* Main value.
* Percentage.
* Progress bar.
* Helper text.
* Status color.

## 6.5 MissionCard

Purpose:

Display checklist-style goals.

Examples:

* Daily missions.
* Weekly missions.
* Recovery actions.

Requirements:

* Completed items use success color.
* Incomplete items are visible but not shameful.
* Failed or missed items should use recovery language.

## 6.6 DebtBossCard

Purpose:

Make debt reduction feel like a recoverable challenge.

Required display:

* Debt name or total debt.
* HP remaining percentage.
* Amount remaining.
* Amount paid this month.
* Optional weakness/action prompt.

Example:

```text
Debt Boss
HP Remaining: 64%
R184,000 left
Weakness: Extra payments
```

## 6.7 SafeSpendShieldCard

Purpose:

Show how much the user can safely spend without breaking their plan.

Required display:

* Safe spend remaining.
* Progress / shield bar.
* Status message.

Examples:

* “Shield stable.”
* “Shield weakening. Slow down today.”
* “Shield broken. Recovery action available.”

## 6.8 SpendingDamageChart

Purpose:

Show category spending without feeling like a spreadsheet.

Preferred forms:

* Horizontal bar chart.
* Donut chart.
* Card-based category bars.

Each category should show:

* Category name.
* Amount spent.
* Percentage of budget used.
* Status color.

## 6.9 RecoveryTimeline

Purpose:

Show longer-term progression.

Example stages:

* Awareness.
* Stabilising.
* Control.
* Momentum.

Use this for emotional motivation, not strict financial scoring only.

## 6.10 EmptyStateCard

Purpose:

Make empty screens feel friendly and actionable.

Bad:

> No data found.

Good:

> No activity logged yet. Start today’s run by adding your first spend.

---

# 7. Chart and Graph Guidelines

Charts should feel like game HUD elements, not generic business charts.

## 7.1 Chart Styling Rules

* Use dark card backgrounds.
* Use rounded chart containers.
* Use soft grid lines or no grid lines.
* Use clear labels.
* Use primary/accent/success/warning/danger colors consistently.
* Avoid too many chart colors at once.
* Avoid tiny unreadable chart text.
* Avoid complex charts on mobile.

## 7.2 Recommended Charts

### Stability Trend Line Chart

Purpose:

Show whether the user is improving over time.

Label:

> Recovery Trend

### Spending Damage Bar Chart

Purpose:

Show category pressure.

Label:

> Spending Damage

### Spending Loadout Donut Chart

Purpose:

Show category distribution.

Label:

> Spending Loadout

### Debt Boss HP Bar

Purpose:

Show debt remaining as enemy HP.

Label:

> Debt Boss HP

### Recovery XP Progress Bar

Purpose:

Show effort and consistency.

Label:

> Recovery XP

---

# 8. Copywriting Rules

The app must be motivating without lying to the user.

## 8.1 Tone

Use a tone that is:

* Supportive.
* Direct.
* Calm.
* Motivating.
* Non-judgmental.
* Action-oriented.

Avoid:

* Shame.
* Sarcasm.
* Panic.
* Overly cute language.
* Fake celebration for serious problems.

## 8.2 Examples

### Overspending

Bad:

> You exceeded your budget.

Good:

> Shield broken. You can still recover this week.

### Debt Payment

Bad:

> Payment recorded.

Good:

> Debt Boss hit for R1,200. Progress gained.

### Low Spending Day

Bad:

> You spent R0.

Good:

> Clean day. Your recovery streak is stronger.

### Missing Data

Bad:

> No transactions found.

Good:

> No activity logged yet. Start today’s run by adding your first spend.

### Bad Month

Bad:

> Monthly budget failed.

Good:

> This recovery run was difficult. Review the damage and choose one action for next month.

---

# 9. Screen-by-Screen UX Direction

## 9.1 Home / Dashboard

Must become the most polished screen.

Required improvements:

* Strong header.
* Stability Level card.
* Mission card.
* Safe Spend Shield.
* Debt Boss HP.
* Spending Damage chart.
* Recent activity with styled rows/cards.

Avoid plain stacked text.

## 9.2 Transactions / Expense Logging

The expense flow should feel fast and low-friction.

Required improvements:

* Clear add expense button.
* Category selection should be visually pleasant.
* Recent transactions should be cards or compact styled rows.
* Use icons or category chips if available.
* After adding an expense, show encouraging feedback.

Example feedback:

> Spend logged. Shield still stable.

or

> Spend logged. Shield weakened, but recovery is still possible.

## 9.3 Budget Categories

Budget categories should not look like a table.

Use cards with:

* Category name.
* Amount spent.
* Budget limit.
* Progress bar.
* Status label.

Example statuses:

* Stable.
* Watch closely.
* Shield damage.
* Over limit.

## 9.4 Debt Screen

The debt screen should strongly use the Debt Boss concept.

Required elements:

* Total debt boss HP.
* Individual debt cards.
* Payment progress.
* Next recommended action.
* Milestones.

Example milestone:

> First R5,000 cleared.

## 9.5 Savings / Goals Screen

Savings should feel like building resources.

Possible label:

> Resource Vault

Required elements:

* Goal progress cards.
* Milestone markers.
* Monthly contribution trend.
* Encouraging helper text.

## 9.6 Reports / Insights

Reports should not become a spreadsheet.

Use:

* Summary cards.
* Trend charts.
* Category bars.
* Short written insights.

Example insight:

> Food spending caused the most shield damage this month. Reducing it by R300 next month would improve your Stability Level.

## 9.7 Settings

Settings should be clean and simple.

Do not over-gamify settings.

Keep:

* App preferences.
* Data management.
* Account options.
* Budget configuration.

Use the same Recovery Arcade card style.

---

# 10. Interaction and Motion Guidelines

Use subtle motion if the framework/app allows it.

Good motion examples:

* Progress bars animate when entering screen.
* Mission completion checks animate softly.
* Cards slightly scale or glow on press.
* Achievement banner slides in.
* Debt Boss HP decreases after payment.

Avoid:

* Excessive bouncing.
* Long animations.
* Flashing neon effects.
* Distracting background animation.
* Motion that makes financial stress worse.

Motion should make progress feel satisfying, not chaotic.

---

# 11. Accessibility Requirements

The UI must remain usable and readable.

Requirements:

* Maintain strong text contrast.
* Do not rely on color alone for status.
* Use labels with icons/status text.
* Ensure tap targets are large enough.
* Avoid tiny chart labels.
* Avoid excessive glow behind text.
* Keep financial numbers clear.

Status examples should include both color and text:

* Stable
* Watch closely
* Shield broken
* Recovery available

---

# 12. Implementation Instructions for Agent

## 12.1 Scope

The agent should improve the existing UI/UX without breaking existing app features.

The agent should not rewrite business logic unless required for display formatting.

The agent should not remove existing functionality.

The agent should not introduce inconsistent design systems.

## 12.2 First Step: Current UI Audit

Before changing screens, the agent must document the current UI state.

Create or update a UI audit file with:

* Existing screens.
* Current components used.
* Current visual inconsistencies.
* Repeated UI patterns that should become reusable components.
* Screens with plain text/table-like presentation.
* Screens needing charts/progress visuals.

Suggested file:

```text
/docs/ui-ux/UI_AUDIT.md
```

## 12.3 Design Token Setup

The agent should centralize colors, spacing, radius, and text styles where possible.

Suggested file:

```text
/docs/ui-ux/RECOVERY_ARCADE_TOKENS.md
```

or implementation equivalent depending on the app stack.

At minimum, document:

* Colors.
* Text styles.
* Radius scale.
* Spacing scale.
* Component surfaces.
* Status colors.

## 12.4 Component Refactor

The agent should create reusable components before styling every screen manually.

Priority components:

1. AppShell
2. ScreenHeader
3. MetricCard
4. ProgressCard
5. MissionCard
6. SafeSpendShieldCard
7. DebtBossCard
8. CategoryDamageCard
9. ChartCard
10. EmptyStateCard

## 12.5 Screen Upgrade Order

Upgrade screens in this order:

1. Dashboard / Home
2. Transactions / Expense logging
3. Budget categories
4. Debt screen
5. Savings / goals
6. Reports / insights
7. Settings

The dashboard should establish the visual language for the rest of the app.

---

# 13. Testing Requirements

The agent must preserve existing tests and add tests where appropriate.

## 13.1 Feature Tests

Add or update feature tests for:

* Dashboard still loads.
* Transactions still display.
* Budget categories still display.
* Debt summary still displays.
* Savings/goals still display.
* Empty states display when there is no data.

## 13.2 Unit Tests

Add or update unit tests for formatting helpers if introduced:

* Currency formatting.
* Percentage calculation.
* Progress state calculation.
* Budget status calculation.
* Debt HP percentage calculation.
* Stability level calculation, if applicable.

## 13.3 Visual Safety Checks

The agent should manually verify:

* No unreadable text.
* No broken layouts on small Android screens.
* No inconsistent colors.
* No overflowing financial values.
* No charts with unreadable labels.
* No old bland text-only sections left on major screens.

---

# 14. Documentation Requirements

Every UI/UX change must be documented.

Create or update:

```text
/docs/ui-ux/CHANGELOG.md
```

Each entry should include:

* Date.
* Screen/component changed.
* What was changed.
* Why it was changed.
* Tests added/updated.
* Known limitations.

Example:

```markdown
## 2026-05-01 — Dashboard Recovery Arcade Pass

### Changed
- Replaced plain monthly summary with Stability Level, Safe Spend Shield, and Debt Boss cards.
- Added Spending Damage category bars.
- Added Weekly Missions card.

### Why
- Dashboard previously felt like a plain document.
- New layout supports the Recovery Arcade product direction.

### Tests
- Added dashboard render test.
- Added progress percentage unit tests.

### Notes
- Chart animations still need refinement.
```

---

# 15. Agent Checklist

Before finishing a task, the agent must confirm:

* [ ] The screen uses the Recovery Arcade visual theme.
* [ ] Colors come from the approved palette.
* [ ] Components are reusable where possible.
* [ ] The screen does not look like a plain document.
* [ ] Important financial numbers are clear.
* [ ] Progress is shown visually where useful.
* [ ] Copy is supportive and non-condemning.
* [ ] Empty states are styled and helpful.
* [ ] Tests were added or updated.
* [ ] Changes were documented.
* [ ] No unrelated features were changed.

---

# 16. Example Component Copy Library

Use these copy patterns across the app.

## Positive Progress

* “Progress gained.”
* “Stability improved.”
* “Small win recorded.”
* “Recovery streak stronger.”
* “You are rebuilding steadily.”

## Warning State

* “Shield weakening. Slow down today.”
* “Watch this category closely.”
* “This area is causing pressure.”
* “Recovery action recommended.”

## Danger State

* “Shield broken. Recovery is still possible.”
* “This category exceeded the safe range.”
* “This run needs adjustment.”

## Empty State

* “No activity logged yet. Start today’s run by adding your first spend.”
* “No missions yet. Add one small action to begin recovery.”
* “No debt accounts added yet. Add your first Debt Boss to start tracking progress.”

## Debt Progress

* “Debt Boss hit for {amount}. Progress gained.”
* “HP reduced. Keep attacking steadily.”
* “Next weakness: extra payment.”

## Budget Progress

* “Shield stable.”
* “Safe range maintained.”
* “Spending pressure rising.”
* “Recovery action available.”

---

# 17. Definition of Done

A UI/UX task is complete only when:

1. The updated screen/component follows the Recovery Arcade theme.
2. The user experience is clearer and more motivating than before.
3. The UI is consistent with the approved color, spacing, and card system.
4. The change does not break existing app functionality.
5. Tests are updated or added where relevant.
6. The change is documented in the UI/UX changelog.
7. Any limitations or future improvements are clearly listed.

---

# 18. Final Direction Summary

Mini Budget should feel like:

> A personal financial recovery game where every small responsible action helps the user regain control.

The user is not the problem.

The enemies are:

* Debt.
* Avoidance.
* Impulse spending.
* Lack of visibility.
* Discouragement.

The app should help the user fight those enemies with:

* Missions.
* Progress.
* Stability levels.
* Debt boss HP.
* Safe spend shields.
* Recovery streaks.
* Clear next actions.

The app should be energetic, but not overwhelming.

It should be honest, but never shaming.

It should be visually polished, consistent, and motivating.

