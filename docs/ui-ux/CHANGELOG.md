# UI/UX Changelog

## 2026-05-02 — Base Navigation Fix and Base Screen Upgrade

### Changed
- Replaced default bottom navigation wiring with explicit tab-root navigation so Base child screens are cleared when switching tabs.
- Added toolbar Up navigation for non-tab destinations, including Base child screens.
- Upgraded Base hub and child screens with arcade panels, chips, progress/status styling, and stronger Recovery Arcade copy.
- Added explicit Damage Report toolbar configuration when opened from Base.

### Why
- Base child screens previously had no clean way back and could remain visually selected when switching bottom tabs.
- Base screens should feel like recovery command modules, not plain report pages.

### Tests
- `./gradlew assembleDebug` passed.
- `./gradlew test` passed.

## 2026-05-02 — Loadout and Bosses Arcade Upgrade

### Changed
- Rebuilt Loadout category rows as shield cards with readiness chips, ranked labels, dynamic shield meters, and cap guidance.
- Reworked the Loadout intro into an armory-style panel.
- Rebuilt Debt Boss cards as battle cards with threat chips, HP metrics, damage percentage, progress meters, battle intel, warnings, and compact action buttons.

### Why
- Loadout and Bosses are core game-metaphor screens and should carry the arcade HUD visual language as strongly as the main dashboard.
- The new cards give users scan-friendly status without changing financial logic.

### Tests
- `./gradlew assembleDebug` passed.
- `./gradlew test` passed.

## 2026-05-02 — Damage Log Gamified Pass

### Changed
- Updated the Damage Log top panel into a Mission Scope control area with a stronger damage summary and run-rank chip.
- Reworked log cards into arcade-style damage entries with severity labels, XP markers, auto-damage badges, and dynamic stroke colors.
- Improved empty-state presentation so the screen does not fall back to plain text.

### Why
- The Damage Log should feel like an honest tactical recovery log, not a bank transaction list.
- Entry severity gives spending logs a quick scan hierarchy without shaming the user.

### Tests
- `./gradlew assembleDebug` passed.
- `./gradlew test` passed.

## 2026-05-02 — Main HUD Graph and Gamified Reports

### Changed
- Replaced Recovery Run text rows with a 7-day arcade-style spending bar graph.
- Replaced Damage Report text rows with category cards, progress meters, status labels, and shield remaining/breach summaries.
- Replaced Pressure Zones text rows with ranked gamified alert cards and recovery-oriented guidance.

### Why
- The dashboard sections now communicate patterns visually instead of reading like text reports.
- Keeps the main HUD closer to the supplied arcade prototype while preserving the same local financial data.

### Tests
- `./gradlew assembleDebug` passed.
- `./gradlew test` passed.

## 2026-05-02 — Main HUD Safe Spend Shield Upgrade

### Changed
- Rebuilt the Dashboard Safe Spend Shield section from a multiline text block into a HUD card.
- Added a large remaining-shield metric, status chip, circular shield percentage indicator, compact spend intel panel, and separate guidance callout.

### Why
- The first Recovery Arcade pass changed language, but this section still read like a spreadsheet summary.
- The main HUD now surfaces weekly allowance status visually and makes the next useful spending signal easier to scan.

### Tests
- `./gradlew assembleDebug` passed.

## 2026-05-02 — Recovery Arcade Stitch Prototype Integration

### Changed
- Adapted the supplied Google Stitch prototype direction into native Android resources and screens.
- Updated bottom navigation labels/icons to HUD, Log, Loadout, Bosses, and Base.
- Expanded Recovery Arcade typography and reinforced dark HUD chrome.
- Reworked screen and dialog copy around Safe Spend Shield, Damage Log, Spending Loadout, Weekly Debrief, Bonus Credits, Resource Vaults, Net Worth Meter, Data Vault, and Recovery XP.
- Updated generated Kotlin UI text and status colors to use Recovery Arcade tokens instead of leftover light-theme tokens.

### Why
- Moves the app further away from a styled spreadsheet and toward a cohesive financial recovery game HUD.
- Keeps the language practical and non-shaming while making recurring workflows feel more motivating.

### Tests
- `./gradlew assembleDebug` passed.
- `./gradlew test` passed.

## 2026-05-01 — UI/UX Audit + Token Baseline

### Changed
- Added a baseline UI audit document.
- Documented the Recovery Arcade token palette and component sizing guidance.

### Why
- Establishes a single visual identity target before screen-by-screen styling.
- Creates a reference for consistent colors/shape/spacing.

### Tests
- None (documentation only).

### Notes
- Build/tests cannot currently be executed in this environment due to missing access to Google Maven/AGP artifacts.

## 2026-05-01 — Recovery Arcade Theme Foundations (App + Dashboard)

### Changed
- Added Recovery Arcade color tokens to Android resources.
- Added base Recovery Arcade theme (`Theme.BudgetTracker.RecoveryArcade`) and set `Theme.BudgetTracker` to inherit from it.
- Updated `activity_main.xml` surfaces for toolbar + bottom nav to match the dark “Recovery Arcade” background.
- Updated `fragment_dashboard.xml` to use Recovery Arcade surfaces, section typography, and supportive empty-state copy.

### Why
- Establishes a single cohesive visual system (dark HUD-like surfaces, high contrast text) before restyling other screens.
- Dashboard sets the tone for “recovery command center” language (trend + damage + pressure).

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- No functional logic was changed; layout + theme resources only.

## 2026-05-01 — Recovery Arcade Pass (Expenses + Categories)

### Changed
- Updated Expenses list and Category list backgrounds to the Recovery Arcade dark canvas.
- Restyled expense/category rows to use the Recovery Arcade card surface, border, and text colors.
- Updated filter panels and recurring badges to match the same surface + border language.

### Why
- Expenses and Categories are high-traffic screens; they must feel cohesive with the new dashboard theme.
- Reduces “light theme leftovers” that made screens feel disconnected.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- No screen logic changed; layout/drawables only.

## 2026-05-01 — Recovery Arcade Pass (Debt + Goals)

### Changed
- Updated Debt Boss and Goals screens to the Recovery Arcade canvas and text palette.
- Adjusted Goals section naming to align with Recovery Arcade language (Resource Vault).

### Why
- Debt and Goals are core “recovery loop” motivators; they should feel cohesive with the dashboard HUD aesthetic.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- This pass is layout-only; no repository/model logic was changed.

## 2026-05-01 — Recovery Arcade Pass (Analytics + More)

### Changed
- Updated Analytics screen header, summary card surfaces, and empty state copy to match Recovery Arcade.
- Updated Analytics category spending rows to match Recovery Arcade card surface + text colors.
- Updated More screen canvas + section headers + button text/icon colors for consistent dark HUD styling.

### Why
- Analytics and More act as navigation + insight hubs; they must match the same visual language to avoid “screen-to-screen theme shifts”.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- This pass is layout-only.

## 2026-05-01 — Recovery Arcade Cleanup (Removing Light Theme Leftovers)

### Changed
- Converted remaining screens (Data Ownership, Extra Income, Gamification, Profile, Net Worth, Add Expense, Expenses list panels) to Recovery Arcade background and text palette.
- Updated shared fragment header toolbar to Recovery Arcade surfaces/text.
- Updated key dialogs (weekly allowance/review, edit budget, category) to Recovery Arcade surfaces/text.

### Why
- Eliminates cross-screen theme shifts and ensures the app reads as one cohesive Recovery Arcade HUD.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- Focused on resource and layout references; underlying feature logic remains unchanged.

## 2026-05-01 — Recovery Arcade Component Unification (Buttons + Forms)

### Changed
- Added Recovery Arcade component styles for buttons (`RaButton.*`) and applied them across high-traffic screens.
- Added Recovery Arcade TextInput styles to improve contrast (outlined stroke + hint colors) on dark surfaces.

### Why
- Prevents “random Material defaults” from appearing across dialogs and forms.
- Makes outlined/text buttons consistent and readable on dark surfaces.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

### Notes
- This pass is primarily theme/styles; it should not alter existing flows.

## 2026-05-01 — Recovery Arcade Component Unification (FAB + Progress)

### Changed
- Added a Recovery Arcade FAB style (`RaFab`) and applied it via theme so all screens use consistent primary-blue FABs.
- Normalized the Recovery Level progress indicator to use `ra_primary` on dark track.

### Why
- Keeps primary actions (FABs) visually consistent and easy to spot.
- Ensures progress indicators match the Recovery Arcade palette.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

## 2026-05-01 — Recovery Arcade Finish (Normalization + Agent Guidance)

### Changed
- Normalized Analytics category progress bars to use `ra_primary` (general progress, not budget-status).
- Added a concise style summary for future changes.
- Updated `AGENTS.md` to require reading `docs/UI_UX_Guideline.md` for any UI/UX work.

### Why
- Locks in consistent HUD-style visuals and prevents agents from reintroducing light-theme tokens.

### Tests
- Not run in this environment (Gradle dependency resolution blocked).

## 2026-05-03 — Profile budgeting cycle + dual-theme toggle + Debt Boss empty-state pass

### Changed
- Added Profile controls for budget cycle start day (1-28) and theme selection (Neon Arcade / Soft Recovery).
- Added runtime theme application on app launch from user preference.
- Upgraded Debt Boss empty state to a styled, game-like arena prompt.

### Why
- User needs payday-aligned budgeting windows and a softer alternative visual mode.
- Debt Boss empty state should still feel motivating and game-inspired.

### Tests
- Attempted unit test run via Gradle (blocked by missing Android SDK in CI container).

### Notes
- Budget-cycle update currently confirms retroactive date window in profile save flow.
