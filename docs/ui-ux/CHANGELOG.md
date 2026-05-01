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
