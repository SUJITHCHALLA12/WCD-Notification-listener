# WCD — reconstructed project

The files in the GitHub repo (`WCD-Notification-listener`) were uploaded flat
into the repository root, so the project could never build. This is the same
code, reorganized into a standard Android Studio project layout.

## What was fixed

1. **Module layout** — `settings.gradle.kts` declares `include(":app")`, so all
   sources now live under `app/src/main/java/com/pentester/wcd/…`, matching
   their `package` declarations. The manifest moved to `app/src/main/`.
2. **Gradle files** — the root `build.gradle.kts` now only declares plugin
   versions (`apply false`); the module build file moved to `app/build.gradle.kts`.
   Wrapper + version catalog moved to the standard `gradle/` directory.
3. **Missing `PinManager`** — `PinActivity` imports
   `com.pentester.wcd.security.PinManager`, which was never in the repo. A new
   implementation was added (`security/PinManager.kt`): SHA-256-hashed PIN in
   private SharedPreferences, first-launch setup flow.
4. **Missing `res/`** — every resource referenced by code or the manifest was
   recreated: 6 layouts (`activity_home`, `activity_chat`, `activity_pin`,
   `activity_developer_info`, `item_chat`, `item_message_sent/received`),
   strings, colors, `Theme.WCD`, message-bubble drawables, the animated chat
   background, `neon_pulse` animation, bottom-nav menu, and adaptive launcher
   icons. Layouts were rebuilt from the view IDs the Kotlin code actually uses,
   so view binding will resolve.

## Before you build

1. **Add `app/google-services.json`** — download it again from the Firebase
   console (project settings → your Android app). The `google-services` Gradle
   plugin hard-fails without it, which is why the file was intentionally left
   out (it's a secret; `.gitignore` now excludes it).
2. Open the folder in Android Studio and let Gradle sync. First sync downloads
   AGP 8.6.0, Kotlin 1.9.22, and all catalog dependencies — needs internet.
3. `local.properties` (with your `sdk.dir`) is intentionally NOT included;
   Android Studio generates it on first open.

## Honest caveats

- The layouts are faithful reconstructions from the view IDs in code, not your
  original XML — expect to restyle them.
- `WCDDatabase` declares the SQLCipher dependency but doesn't actually wire
  `SupportFactory` with a passphrase; the DB is currently unencrypted despite
  the dependency. Either remove the dependency or add passphrase support.
- This was assembled without an Android SDK on hand, so the final check
  (Gradle sync + assembleDebug) still needs to happen in Android Studio.
