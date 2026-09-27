\# Smart Pantry Manager



A Java Android application for a student cooking for one. It tracks pantry

ingredients and suggests meals only when every required ingredient is

available in sufficient quantity.



\## Features



\- Add, view, edit and delete pantry ingredients.

\- Validate ingredient names, quantities and supported units.

\- Persist pantry records in a local SQLite database.

\- Seed 20 recipes with 70 ingredient requirements.

\- Suggest recipes using strict ingredient and quantity matching.

\- Display recipe ingredients and preparation steps.

\- Save a preference for alphabetical or newest-first pantry ordering.



\## Technology



\- Java application code and XML layouts

\- Five Activities with explicit Intent navigation

\- ListView with a custom PantryAdapter

\- SQLiteOpenHelper for database creation and upgrades

\- SharedPreferences for the sorting preference

\- Gradle build configuration using Kotlin DSL



\## Database choice



SQLite suits this application because pantry data and recipes are stored

locally and can be used without an account, server or runtime internet

connection. SQLiteOpenHelper manages database creation and version upgrades.



The database contains:

\- pantry\_items: each pantry entry's ID, name, quantity and unit

\- recipes: recipe ID, name and preparation steps

\- recipe\_ingredients: required ingredients linked to a recipe by recipe\_id



Version 2 added recipe tables while retaining pantry records.

Version 3 introduced the recipe seed data. Seeding runs during database

creation or the relevant upgrade, rather than on every app launch.



Closing the app preserves its records. Clearing app storage removes local

data; reopening then creates a fresh database.



\## Setup and running



1\. Clone this repository or extract the complete source project.

2\. In Android Studio, open the project root containing settings.gradle.kts.

3\. Allow Gradle sync and dependency downloads to complete.

4\. Install Android SDK Platform 37 when requested.

5\. Allow the configured Gradle JVM toolchain 25 to be provisioned.

6\. Select an emulator or connected Android device running API 24 or newer.

7\. Select the app run configuration and click Run.



The project uses Android Gradle Plugin 9.4.1. Use an Android Studio version

compatible with that plugin and retain the included Gradle wrapper.



Development checks were performed on a Pixel 6 emulator running Android 15,

API 35. The initial build requires internet access to download dependencies.



\## Using the app



\- Tap Add ingredient to create a pantry entry.

\- Tap an existing pantry row to edit it.

\- Long-press a row to delete it after confirmation.

\- Open the toolbar menu for Suggested recipes or Settings.

\- Tap a suggested recipe to view its ingredients and preparation steps.

\- Use Android Back navigation to return to the previous screen.



\## Matching rules



Ingredient names are trimmed, lowercased and normalised for repeated spaces.

An explicit alias list handles supported variations such as bananas/banana

and tomatoes/tomato. It is not a general natural-language matching system.



Kilograms convert to grams, and litres convert to millilitres.

Mass, volume and item counts are never converted into one another.



Duplicate pantry entries remain separately editable. Matching totals their

quantities when their normalised names and measurement types agree.



Every recipe requirement must be satisfied. Missing ingredients, insufficient

amounts and incompatible units exclude the recipe. Listed water, oil and

other ingredients are never assumed to be available.



Recipes are evaluated independently. Suggestions do not guarantee enough

ingredients to prepare every suggested recipe together. Viewing recipes

does not deduct pantry quantities.



\## Ingredient conventions



\- Supported units: g, kg, ml, l and each

\- Bread: each means one slice

\- Rice: dry weight

\- Tuna, chickpeas, kidney beans and sweetcorn: drained, ready-to-eat tinned

&#x20; ingredients

\- Other each quantities: individual items



\## Manual verification



Development checks covered CRUD, validation, persistence after restart,

empty-state behaviour, navigation, recipe details and saved sorting settings.



Matching checks covered missing and insufficient ingredients, exact amounts,

duplicate totals, compatible conversions, incompatible units, supported

name variations and required cooking water.



Recipe counts were checked after relaunch to confirm seeds did not duplicate.

These are manual development checks, not a claim of exhaustive automated

test coverage.



\## Scope



The app does not provide cloud synchronisation, expiry alerts, nutritional

calculations or automatic ingredient deduction.



\## Repository



https://github.com/ValentinoKN/SmartPantryManager

