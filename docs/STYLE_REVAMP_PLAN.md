# Style Revamp Plan

## Direction
- Move the app toward a clean finance-tool interface: quiet neutral surfaces, strong hierarchy, compact controls, and clear totals.
- Use the updated Expenses screen as the reference pattern for the rest of the app.
- Keep the app uniformly light; dark mode is intentionally not a separate design target right now.
- Keep visual decisions practical for repeated use: consistent spacing, 8dp cards, restrained borders, and semantic color.

## Design System Baseline
- **Canvas:** `surface_canvas` for screens.
- **Panels:** `surface_panel` with a subtle border for controls, summaries, and grouped content.
- **Text:** `ink_primary` for important values, `ink_secondary` for body labels, and `ink_tertiary` only for lower-emphasis metadata.
- **Accent:** use the neutral `accent_blue` token sparingly for selected states and recurring/monthly metadata.
- **Cards:** 8dp radius, low or no elevation, clear stroke, no nested card stacks.
- **Numbers:** currency must use `CurrencyUtils`; percentages must use `PercentageUtils`.

## Rollout Order
1. **Dashboard**
   - Status: initial revamp applied.
   - Aligned budget progress, generated rows, and text hierarchy with the Expenses screen.

2. **Analytics**
   - Status: initial revamp applied.
   - Converted summary cards and text hierarchy to the shared panel/card language.

3. **Categories**
   - Status: initial revamp applied.
   - Modernized category rows and category creation dialogs.

4. **Profile and Settings**
   - Status: initial revamp applied.
   - Grouped personal data into a clean panel and standardized button styling.

5. **Forms and Dialogs**
   - Status: initial revamp applied.
   - Standardized input treatment across expense, category, profile, and budget forms.
   - Ensure edit flows preserve existing values and show concise validation.

## Next Functional Improvements
- Expand recurring expenses from a monthly marker into recurrence rules, such as monthly, weekly, custom interval, next due date, and paused status.
- Add budget planning workflows around recurring monthly commitments.
- Introduce more deliberate expense review flows: duplicate detection, quick edit, bulk delete, and better empty states.
- Add export/import after the data model stabilizes.

## Expense Screen Follow-Ups
- Replace manual `LinearLayout` inflation with `RecyclerView` when expense volume grows or section behavior becomes more complex.
- Add persisted filter preference if users repeatedly return to the same expense view.
- Consider a dedicated recurring-expense management screen if recurrence rules expand beyond a monthly marker.

## QA Checklist
- Run `./gradlew test`.
- Check day/week/monthly recurring filtering with empty, small, and multi-page datasets.
- Verify text does not wrap awkwardly on narrow screens.
- Confirm long-press delete and tap-to-edit remain discoverable through native behavior and consistent row affordances.
