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
  - `activity/` - Activity 기반 데모
  - `album/` - 앨범 UI
  - `animation/` - 애니메이션 데모 (Crossfade, Lottie, GIF/WebP, AnimatedVectorDrawable, 30+ 예제)
  - `canvas/` - Custom canvas drawing examples
  - `clone/` - UI 클론 구현 (Capturable 등)
  - `common/` - Shared UI components and navigation
  - `custom/` - Custom composables (Filter, Timeline, OrderStatus)
  - `dialog/` - 다이얼로그 데모
  - `foundation/` - Basic Compose components (LazyColumn, Canvas, Text, Image, etc.)
  - `grid/` - 그리드/칩 컴포넌트
  - `keyboard/` - 키보드 핸들링 데모
  - `lazycolumn/` - LazyColumn 데모 (Netflix 툴바 등)
  - `material/` - Material Design components (Button, Card, Dialog, etc.)
  - `modifier/` - Modifier demonstrations (Lookahead)
  - `navigation/` - 네비게이션 데모
  - `sample/` - Full-screen sample apps (Payment, Photo, Drawing)
  - `scroll/` - 스크롤 데모
  - `snackbar/` - Snackbar 데모
  - `stat/` - 통계 화면
  - `theme/` - Theme definitions (Color, Typography, Shape)

### Key Technologies

- **Compose BOM**: `2025.12.01` - Material3, Foundation, UI
- **Kotlin**: 2.2.21
- **Kotlin Compose Plugin**: 2.2.21 (Kotlin 2.0+ 이후 별도 플러그인)
- **AGP**: 8.13.2
- **Coroutines**: 1.7.3
- **Hilt**: 2.57.2
- **Lottie**: 6.7.1 for animations
- **Coil**: 2.7.0 for image loading (GIF 지원 포함)
- **DataStore**: 1.2.0 with Protobuf
- **Accompanist**: System UI controller, Placeholder (0.36.0)

### Build Configuration

- **Min SDK**: 26
- **Target SDK**: 36
- **Compile SDK**: 36
- **Java**: 17 (toolchain)
- **Protobuf**: Configured to generate lite Java and Kotlin classes (plugin 0.9.6)

## Adding New Demos

1. Create your composable or Activity in the appropriate `ui/` subdirectory
2. Add the demo to the relevant category in `Demos.kt`:
   - For composables: `ComposableDemo("Name") { YourComposable() }`
   - For activities: `ActivityDemo("Name", YourActivity::class)`
   - Add to existing category or create new `DemoCategory`
3. If creating a new Activity, add it to `AndroidManifest.xml`

## Theme System

The app uses a custom theme system (`ComposePlaygroundTheme` in `ui/theme/`) based on **Material3**:
- Material3 color scheme (`darkColorScheme`, `lightColorScheme`)
- Custom typography with `MyTypography` and tokens
- Color definitions in `Color.kt`
- Material shapes in `Shape.kt`
- `LocalTypography` CompositionLocal for custom typography access

## Known Configuration Notes

- Kotlin 2.0+ 이후 Compose 컴파일러 플러그인이 Kotlin 플러그인(`org.jetbrains.kotlin.plugin.compose`)으로 분리됨
- Compose BOM 버전 업데이트 시 호환 Kotlin 버전 확인 필요
- Material에서 Material3로 마이그레이션 완료

## Testing

Test files are minimal:
- Unit tests: `app/src/test/java/` (kotlinx-coroutines-test, TestScope 활용)
- Instrumented tests: `app/src/androidTest/java/`
- UI tests use Compose testing libraries (included in dependencies)
