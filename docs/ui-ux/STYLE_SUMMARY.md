# Recovery Arcade Style Summary

This repository uses a **single** visual style direction: **Recovery Arcade**.

## Theme

- App theme: `Theme.BudgetTracker.RecoveryArcade` (wired as `Theme.BudgetTracker`)
- No theme switching (no light/dark variants)

## Core Tokens

- Canvas: `@color/ra_bg`
- Primary surface/card: `@color/ra_surface_card` + `@color/ra_border`
- Deep surface (chrome): `@color/ra_surface_deep`
- Primary action: `@color/ra_primary`
- Rewards/XP accent: `@color/ra_accent`
- Text: `@color/ra_text`, muted: `@color/ra_text_muted`, subtle: `@color/ra_text_subtle`

## Reusable Styles

- Text
  - `@style/RaText.ScreenTitle`
  - `@style/RaText.SectionTitle`
  - `@style/RaText.Body`
  - `@style/RaText.Muted`
- Buttons
  - `@style/RaButton.Outlined`
  - `@style/RaButton.Text`
  - `@style/RaButton.Filled`
- Forms
  - `@style/RaTextInputLayout`
  - `@style/RaEditText`
- FAB
  - `@style/RaFab` (applied via theme)

## Guidance

- Avoid introducing `surface_*` / `ink_*` light-theme tokens into layouts.
- Use `budget_good/budget_warning/budget_over` only when you are representing **status**.
- Use `ra_primary`/`ra_accent` for progress indicators that represent **general progress / XP**.

