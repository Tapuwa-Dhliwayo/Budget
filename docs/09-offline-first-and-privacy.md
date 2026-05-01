# Offline-First and Privacy

## 1. Purpose

Mini Budget is local-first by design.

The user should be able to manage their finances privately without requiring an account, internet connection, bank integration, or cloud service.

The system should answer:

```txt
Can I trust this app with my personal financial recovery data?
```

## 2. Product Role

Offline-first and privacy support the app’s core identity.

Mini Budget is about personal responsibility and user ownership.

The user owns their data.
The user controls their backups.
The app should work without internet.

## 3. Core Privacy Promise

```txt
Your financial recovery data stays in your hands.
```

This should guide all technical decisions.

## 4. Required Offline Capabilities

The app should support:

* Full local usage.
* Expense tracking offline.
* Category management offline.
* User details offline.
* Weekly allowance offline.
* Debt tracking offline.
* Net worth snapshots offline.
* Extra income tracking offline.
* Goals offline.
* Gamification offline.
* Reports generated locally where possible.

## 5. No Required Account

The app must not require an account for core functionality.

If optional sync is ever added, local-only mode must remain fully supported.

## 6. Features Not Included Initially

Do not add:

* Bank syncing.
* Shared household budgets.
* Automatic transaction imports.
* Cloud AI analysis.
* Required cloud account.
* Social sharing.
* Public leaderboards.

These features conflict with the personal, private, local-first focus unless explicitly reconsidered later.

## 7. Backup and Restore

The app should eventually support:

* Export backup.
* Import backup.
* Manual backup reminder.
* Backup verification if possible.
* Clear warning before overwrite.

Backups should be user-controlled.

## 8. Export Formats

Suggested export formats:

* App backup file for restore.
* CSV for expenses.
* CSV for debts.
* CSV for goals.
* PDF reports.

CSV/PDF exports are for user readability and reporting.
App backup files are for restoring app state.

## 9. Security

Suggested security features:

* PIN lock.
* Biometric unlock.
* Optional app auto-lock.
* Optional local encryption.
* Hide sensitive values on app switcher if supported.

Security should be optional but easy to enable.

## 10. Data Deletion

The user should be able to delete:

* Individual expenses.
* Categories where safe.
* Debts.
* Goals.
* Extra income entries.
* Net worth assets.
* All app data.

Destructive actions should require confirmation.

## 11. Data Migration Rules

As future features add new storage fields or tables, migrations must be safe.

Rules:

* Do not destroy existing data.
* Provide defaults for new fields.
* Document schema changes.
* Add migration tests where practical.
* Update change log.
* Update baseline/storage notes.

## 12. Optional Future Cloud Sync

Cloud sync may be considered later only for syncing between the user’s own devices.

Rules for future sync:

* Must be optional.
* Must not replace local-first mode.
* Must clearly explain what data is synced.
* Must provide a way to disable sync.
* Must not add bank syncing by default.
* Must not require cloud AI analysis.

## 13. Privacy-Friendly UX

Good:

```txt
Your data is stored on this device. Create a backup regularly so you stay in control.
```

Good:

```txt
Cloud sync is optional. You can continue using Mini Budget fully offline.
```

Avoid:

```txt
Sign in to continue.
```

Avoid for core features:

```txt
Internet connection required.
```

## 14. Backup Reminder Tone

Good:

```txt
You have not created a backup recently. A quick backup helps protect your recovery progress.
```

Bad:

```txt
Your data may be lost because you ignored backups.
```

## 15. Edge Cases

Handle:

* No backup exists.
* Backup import fails.
* Backup file is from older app version.
* Backup file is corrupted.
* User cancels import.
* User imports over existing data.
* User forgets PIN.
* Biometrics unavailable.
* Storage permission denied.
* Device has no internet.

## 16. Future Enhancements

* Encrypted backups.
* Scheduled backup reminders.
* Local-only privacy mode label.
* Optional sync conflict resolution.
* Backup health check.
* PDF monthly recovery report.
* Secure app lock settings.

## 17. Implementation Status

Status on 2026-05-01: first foundation implemented.

Included:

* Data Ownership screen accessed from More.
* Local-first privacy explanation.
* Local database backup copy creation.
* Backup freshness status using last backup date and path.
* Backup reminder guidance after no backup or stale backup.
* Backup-created recovery XP hook.
* Repository tests for backup status, freshness, and metadata.

Moved to post-1.0 roadmap:

* Restore/import flow with clear overwrite warning.
* User-selected export destination.
* Encrypted backups.
* Backup verification.
* CSV/PDF readable exports.
* PIN/biometric app lock.
* Secure app switcher mode.
* Migration tests and UI workflow tests.

## 18. V1.0 Closure

Status on 2026-05-01: closed for the Mini Budget v1.0 foundation.

The v1.0 scope delivered the Data Ownership screen, local-first privacy messaging, local backup copy creation, backup freshness tracking, and backup XP hook.

Remaining improvements are now tracked in:

* `TASK-020` for restore/import, backup verification, user-selected export destinations, and backup health.
* `TASK-021` for PIN, biometric unlock, auto-lock, and sensitive-value privacy controls.
* `TASK-022` for readable exports and reports.
* `TASK-012` for migration and workflow test hardening.

Bugs found by testing should be logged as bugfix work and do not reopen Task 09.
