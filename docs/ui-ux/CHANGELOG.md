# UI/UX Changelog

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
