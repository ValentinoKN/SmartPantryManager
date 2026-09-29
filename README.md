# Smart Pantry Manager

This is my Mobile App Development 700 project. It is aimed at a student cooking for one who wants to use what is already in the kitchen. The app keeps track of ingredients and only suggests a meal when every required ingredient is available in enough quantity.

## What it does

You can add ingredients, view them, tap to edit them and hold an entry to delete it. Each entry has a name, quantity and unit. The form checks the input before saving it.

Suggested recipes are checked against the current pantry. Tapping a recipe shows its required amounts and preparation steps. Settings lets you choose alphabetical or newest-first pantry order, and remembers the choice after the app closes.

The interface uses a simple deep-red colour scheme, with light and dark themes following the phone setting.

## How it is built

The application code is Java, with XML layouts and five Activities. Intents move between screens and pass record IDs. PantryAdapter connects the pantry records to the ListView. The Gradle files use Kotlin DSL for build configuration; the application code is still Java.

I chose local SQLite storage through SQLiteOpenHelper because this app does not need a server, login or internet connection during normal use. Closing the app does not remove the pantry. Clearing app storage does remove its local data.

There are three tables: pantry_items, recipes and recipe_ingredients. A recipe can have several requirements linked through recipe_id. The app seeds 20 recipes with 70 ingredient requirements. Database upgrades add the tables and seed data while keeping existing pantry records. Normal launches do not seed them again.

SharedPreferences holds the sorting setting separately from the ingredient records.

## How matching works

Names are checked without differences in capital letters or extra spaces. A small alias list handles supported variations such as bananas/banana and tomatoes/tomato. It does not guess every possible ingredient name.

Kilograms convert to grams and litres convert to millilitres. Mass, volume and item counts stay separate. Duplicate pantry entries remain separately editable, but compatible quantities are added together when checking recipes.

Every requirement must pass. Missing ingredients, insufficient amounts and incompatible units exclude the recipe. Water and oil are not assumed to be available when a recipe requires them.

Each suggestion is checked independently. The list means you can choose one of those meals, not necessarily cook all of them together. Opening a recipe does not deduct ingredients.

## Ingredient units

The supported units are g, kg, ml, l and each. Bread is counted in slices. Rice is recorded by dry weight. Tuna, chickpeas, kidney beans and sweetcorn mean drained, ready-to-eat tinned ingredients. Other each quantities count individual items.

## Open and run the project

1. Clone or download this repository and open its root folder in Android Studio. This is the folder containing settings.gradle.kts.
2. Let Gradle sync and download the required dependencies. Install Android SDK Platform 37 if prompted.
3. Use Android Studio compatible with Android Gradle Plugin 9.4.1. Keep the included Gradle 9.6.0 wrapper and allow its configured JVM toolchain 25 to be provisioned.
4. Choose an emulator or connected Android phone running Android 7.0, API 24, or newer.
5. Select the app run configuration and click Run.

The initial setup needs internet access for downloads. Development checks were carried out on a Pixel 6 emulator running Android 15, API 35. The SDK used to compile the app is API 37, which is separate from the minimum device version.

## Build an APK for a phone

In Android Studio's terminal, from the project root, run:

```powershell
.\gradlew.bat assembleDebug
```

If a separate PowerShell window uses an older Java installation, select the Android Studio runtime for that session first:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

The installable file is app/build/outputs/apk/debug/app-debug.apk. This is automatically signed for development and can be used for personal testing. It is not a Play Store release build.

Copy the APK to an Android phone, open it in the phone's file manager and allow installation from that source if prompted. The phone starts with its own empty pantry; emulator records are not included in the APK. For a USB-connected phone with USB debugging enabled, adb install -r followed by the APK path installs or updates the app without intentionally clearing its records.

## Checks completed during development

Manual checks covered adding, listing, editing, deleting, invalid input, persistence, empty states, navigation and the saved sorting setting. Matching checks covered missing ingredients, insufficient and exact quantities, compatible conversions, incompatible units, duplicate totals, supported name variations and required cooking water. Seed counts stayed at 20 recipes and 70 requirements after relaunch.

These are manual development checks, not a claim of exhaustive automated test coverage. The app has no expiry alerts, nutritional calculations, cloud sync or automatic ingredient deduction.

## Project details

Valentino Naidoo, student number 402103668. Mobile App Development 700, 2026, semester 2.

Repository: https://github.com/ValentinoKN/SmartPantryManager
