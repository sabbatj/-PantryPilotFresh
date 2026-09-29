# PantryPilotFresh

A native Android pantry management application built with Java and SQLite. PantryPilotFresh helps users manage ingredients, track quantities and expiry dates, and discover recipes they can prepare with the ingredients they have.

## Features

- Add, view, edit and delete pantry ingredients.
- Manage ingredient quantities and expiry dates.
- View available recipes using strict ingredient matching.
- Exclude expired or insufficient ingredients from recipe suggestions.
- View recipe details.
- Configure expiry reminder preferences.
- Save pantry data locally using SQLite.

## Tech Stack

- **Language:** Java
- **Platform:** Android
- **Database:** SQLite
- **Build Tool:** Gradle
- **Version Control:** Git and GitHub

## Database and Recipe Matching

PantryPilotFresh uses SQLite to store pantry data locally. SQLite is lightweight, supported by Android and does not require an internet connection or external database server.

The recipe-matching algorithm checks whether every required ingredient is available in sufficient quantity. Recipes are only suggested when all requirements are met, and expired ingredients are excluded.

## Getting Started

### Requirements

- Git
- Android SDK
- Compatible JDK
- Android emulator or physical Android device
- Android Studio or VS Code

### 1. Clone the Repository

On Windows, macOS or Linux, open a terminal and run:

```bash
git clone https://github.com/sabbatj/PantryPilotFresh.git
cd PantryPilotFresh
```

### 2. Open the Project

**Android Studio**

1. Open Android Studio.
2. Select **Open** and choose the `PantryPilotFresh` folder.
3. Allow Gradle to synchronise.
4. Start an Android emulator or connect an Android device.
5. Click **Run**.

**Visual Studio Code**

1. Install VS Code and the Extension Pack for Java.
2. Install and configure the Android SDK and a compatible JDK.
3. Open the `PantryPilotFresh` folder in VS Code.
4. Open the integrated terminal.
5. Use the commands below to build and install the application.

**Visual Studio**

Full Visual Studio is not a native Java Android development environment. You can open the project folder and use its integrated terminal with an independently installed Android SDK and JDK. For editing and running this project, use Android Studio or VS Code.

### 3. Build the Application

**macOS and Linux**

```bash
chmod +x gradlew
./gradlew assembleDebug
```

**Windows (PowerShell)**

```powershell
.\gradlew.bat assembleDebug
```

The generated APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### 4. Run on an Emulator or Android Device

Start an Android emulator or connect a device with USB debugging enabled.

Check that the device is connected:

```bash
adb devices
```

**macOS and Linux**

```bash
./gradlew installDebug
```

**Windows (PowerShell)**

```powershell
.\gradlew.bat installDebug
```

You can also run the application directly from Android Studio.

## Project Structure

```text
app/src/main/
├── AndroidManifest.xml
├── java/com/pantrypilot/app/
│   ├── DatabaseHelper.java
│   ├── IngredientActivity.java
│   ├── MainActivity.java
│   ├── MatchingUtils.java
│   └── RecipeDetailActivity.java
└── res/
    ├── drawable/
    ├── drawable-nodpi/
    └── values/
```

## Testing

The application can be verified by:

1. Adding, editing and deleting pantry ingredients.
2. Checking recipe suggestions when ingredient quantities change.
3. Confirming expired ingredients do not satisfy recipe requirements.
4. Closing and reopening the app to verify data persistence.

Build the project using:

**macOS and Linux**

```bash
./gradlew assembleDebug
```

**Windows**

```powershell
.\gradlew.bat assembleDebug
```

## Academic Information

Developed by **Juandre Sabbat** for Mobile App Development 700 as part of the BSc in Information Technology at Richfield Graduate Institute of Technology.

## References

- [Android Developers](https://developer.android.com/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [Gradle Documentation](https://docs.gradle.org/)
