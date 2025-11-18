# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ComposePlayground is a Jetpack Compose demonstration and experimentation repository showcasing various Compose UI components, animations, and custom implementations. The app is organized as a demo catalog with a navigation system that allows browsing through different categories of composable demonstrations.

**Package structure:** `com.ys.composeplayground`

## Common Development Commands

### Build and Run
```bash
# Build debug APK
./gradlew assembleDebug

# Install debug APK to connected device
./gradlew installDebug

# Build and install
./gradlew assembleDebug installDebug

# Clean build
./gradlew clean
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run unit tests for a specific package
./gradlew test --tests "com.ys.composeplayground.ui.*"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run a specific instrumented test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.ys.composeplayground.ExampleInstrumentedTest
```

## Architecture

### Demo System

The app uses a hierarchical demo catalog system defined in `Demos.kt`:

- **Demo sealed class**: Base class for all demo types
  - `ActivityDemo<T>`: Launches a separate Activity
  - `ComposableDemo`: Displays a composable inline
  - `DemoCategory`: Groups related demos together

- **Navigation**: The `Navigator` class (in `ui/common/Navigator.kt`) manages the demo navigation stack and integrates with Android's OnBackPressedDispatcher. It's saved/restored across configuration changes using `rememberSaveable`.

- **Main Entry**: `MainActivity` sets up the navigation system and wraps the app in `ComposePlaygroundTheme`. The demo catalog (`AllDemosCategory`) contains all demos organized into categories like Animation, Foundation, Material, etc.

### Core Architecture

**Dependency Injection**: Uses Hilt (`@HiltAndroidApp` on Application class, `@AndroidEntryPoint` on Activities)

**Data Layer** (mostly commented out but present):
- DataStore with Protocol Buffers for preferences
- Repository pattern with `UserDataRepository`
- Proto definitions in `core/datastore/proto/`
- Dispatchers and CoroutineScopes configured via Hilt modules

**UI Organization**:
- `ui/` - Organized by feature/component type:
  - `animation/` - Crossfade, Lottie examples
  - `foundation/` - Basic Compose components (LazyColumn, Canvas, Text, Image, etc.)
  - `material/` - Material Design components (Button, Card, Dialog, etc.)
  - `custom/` - Custom composables (Filter, Timeline, OrderStatus)
  - `canvas/` - Custom canvas drawing examples
  - `modifier/` - Modifier demonstrations (Lookahead)
  - `sample/` - Full-screen sample apps (Payment, Photo, Drawing)
  - `theme/` - Theme definitions (Color, Typography, Shape)
  - `common/` - Shared UI components and navigation

### Key Technologies

- **Compose BOM**: `2024.09.00` - Material, Material3, Foundation, UI
- **Kotlin**: 1.9.20
- **AGP**: 8.2.2
- **Coroutines**: 1.7.3
- **Hilt**: 2.51.1
- **Lottie**: 6.4.1 for animations
- **Coil**: 2.6.0 for image loading
- **DataStore**: 1.1.1 with Protobuf
- **Accompanist**: System UI controller (0.23.1)

### Build Configuration

- **Min SDK**: 26
- **Target SDK**: 35
- **Compile SDK**: 35
- **Java**: 17 (toolchain)
- **Protobuf**: Configured to generate lite Java and Kotlin classes

## Adding New Demos

1. Create your composable or Activity in the appropriate `ui/` subdirectory
2. Add the demo to the relevant category in `Demos.kt`:
   - For composables: `ComposableDemo("Name") { YourComposable() }`
   - For activities: `ActivityDemo("Name", YourActivity::class)`
   - Add to existing category or create new `DemoCategory`
3. If creating a new Activity, add it to `AndroidManifest.xml`

## Theme System

The app uses a custom theme system (`ComposePlaygroundTheme` in `ui/theme/`):
- Custom typography with `MyTypography` and tokens
- Color definitions in `Color.kt`
- Material shapes in `Shape.kt`
- `LocalMyTypography` CompositionLocal for custom typography access

## Known Configuration Notes

From the README, there were version compatibility issues during development:
- BadgeBox requires Material 1.1.1+
- Compose version updates require corresponding Kotlin version updates
- Current versions are stable: Compose 1.5.4, Kotlin 1.9.20

## Testing

Test files are minimal:
- Unit tests: `app/src/test/java/`
- Instrumented tests: `app/src/androidTest/java/`
- UI tests use Compose testing libraries (included in dependencies)
