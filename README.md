# Compose Playground
[Jetpack Compose playground](https://foso.github.io/Jetpack-Compose-Playground/)

## 개발시 문제사항

### BadgeBox 구현시 발생문제
1. compose version - `1.0.0-beta09` 사용중이었으나 BadgeBox가 material 1.1.1 이상에서 작동

```kotlin
implementation "androidx.compose.material:material:$compose_version"
```

2. Compose 버전을 1.1.1 로 올리니 KotlinCompile 에러 발생
- Kotlin Version 1.6.10 으로 업그레이드

```groovy
buildscript {
  ext {
    compose_version = '1.1.1'
    kotlin_version = "1.6.10"
  }
  ...
}
```

3. 위의 방법대로 순차적으로 진행하니 컴파일 에러 수정

## Compose UI

### Animation

[**Crossfade**](app/src/main/java/com/ys/composeplayground/ui/animation/Crossfade.kt)
- Composable 전환시 Crossfade 애니메이션 효과를 적용
- animationSpec 매개 변수를 사용하면 Composable 간에 전환하는 데 사용할 애니메이션을 설정할 수 있습니다.

참고
[Canvas](https://foso.github.io/Jetpack-Compose-Playground/foundation/canvas/)


### Foundation

[**BaseTextField**](app/src/main/java/com/ys/composeplayground/ui/foundation/BaseTextField.kt)
- [TextField](https://foso.github.io/Jetpack-Compose-Playground/material/textfield/)를 사용해서 text 표시
- 기존의 EditText 와 유사

참고
[BaseTextField](https://foso.github.io/Jetpack-Compose-Playground/foundation/basetextfield/)


[**Canvas**](app/src/main/java/com/ys/composeplayground/ui/foundation/Canvas.kt)
- drawRect, drawCircle, drawLine, drawArc 등으로 캔버스에 도형을 그리기

참고
[Canvas](https://foso.github.io/Jetpack-Compose-Playground/foundation/canvas/)


[**Image**](app/src/main/java/com/ys/composeplayground/ui/foundation/ImageResource.kt)
- painterResource 로 이미지 리소스 로드
- Image 의 painter 에 넣어서 사용
- 기존의 ImageView 와 유사

참고
[Image](https://foso.github.io/Jetpack-Compose-Playground/foundation/image/)


[**LazyColumn**](app/src/main/java/com/ys/composeplayground/ui/foundation/LazyColumn.kt)
- LazyColumn은 현재 보이는 항목을 구성하고 배치하는 세로 스크롤 리스트
- 기존 Android View 시스템의 Recyclerview와 유사
- LazyListScope.items 확장함수를 사용해서 리스트 세팅
    - items -> 리스트 데이터를 세팅
    - itemContent -> 아이템 하나를 전달받아 뷰를 그리는 역할

참고
[LazyColumn](https://foso.github.io/Jetpack-Compose-Playground/foundation/lazycolumn/)


[**LazyRow**](app/src/main/java/com/ys/composeplayground/ui/foundation/LazyColumn.kt)
- LazyRow 현재 보이는 항목을 구성하고 배치하는 가로 스크롤 리스트
- 기존 Android View 시스템의 Recyclerview와 유사
- LazyListScope.items 확장함수를 사용해서 리스트 세팅
  - items -> 리스트 데이터를 세팅
  - itemContent -> 아이템 하나를 전달받아 뷰를 그리는 역할

참고
- [LazyColumn](https://foso.github.io/Jetpack-Compose-Playground/foundation/lazyrow/)
- [Offical Docs](https://developer.android.com/reference/kotlin/androidx/compose/foundation/lazy/package-summary#lazyrow)
- [Capturable](https://github.com/PatilShreyas/Capturable)
  - JetpackCompose 캡쳐 라이브러리

# [Composables - Component](https://www.composables.com/components)

---

# 컴포넌트 정리

## [FlowRow](./app/src/main/java/com/ys/composeplayground/ui/common/SettingsDialog.kt)
- 가로 영역에 맞게 컴포넌트를 줄바꿈해주는 Row
- 뱃지, 키워드 등을 가로로 나열할때 사용하면 유용 할것 같습니다.

# CompositionLocals

`package androidx.compose.ui.platform`

## CompositionLocals
- 컴포지션로컬은 플랫폼 접근성 서비스와의 커뮤니케이션을 제공합니다.

### LocalUriHandler
```kotlin
interface UriHandler {
    /**
     * Open given URL in browser
     */
    fun openUri(uri: String)
}
```
- 플랫폼별 URL 처리를 제공하는 인터페이스입니다.

**사용 예**
```kotlin
val uriHandler = LocalUriHandler.current
CommonButton(
    onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
) {
    Text(text = stringResource(R.string.feature_settings_privacy_policy))
}
```

## staticCompositionLocalOf
- CompositionLocalProvider를 사용하여 제공할 수 있는 CompositionLocal 키를 생성합니다.
- staticCompositionLocalOf의 읽기는 컴포저에 의해 추적되지 않으며 CompositionLocalProvider 호출에 제공된 값을 변경하면 컴포지션에서 로컬 값이 사용된 위치만 재구성되는 것이 아니라 전체 콘텐츠가 재구성됩니다. 
- 그러나 제공된 값이 변경될 가능성이 거의 없거나 변경되지 않을 경우에는 이러한 추적 기능의 부족으로 인해 정적 컴포지션 로컬 오브가 더 효율적입니다. 
- 예를 들어 안드로이드 컨텍스트, 폰트 로더 또는 이와 유사한 공유 값은 CompositionLocalProvider의 콘텐츠에 있는 컴포넌트에 대해 변경될 가능성이 거의 없으므로 `staticCompositionLocalOf` 사용을 고려해야 합니다. 
- 색상이나 값과 같은 다른 테마는 변경되거나 애니메이션이 적용될 수 있으므로 `compositionLocalOf`를 사용해야 합니다.

```kotlin
fun <T> staticCompositionLocalOf(defaultFactory: () -> T): ProvidableCompositionLocal<T> =
    StaticProvidableCompositionLocal(defaultFactory)
```

### Typography 사용 예시
```kotlin
// CompositionLocal for MyTypography
val LocalMyTypography = staticCompositionLocalOf { MyTypography() }

@Composable
fun MyTheme(
    typography: MyTypography = MyTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMyTypography provides typography) {
        content()
    }
}
```
- staticCompositionLocalOf - 특정 타입의 데이터 보유
  - 여기서는 MyTypography 타입을 보유하고 있고 MyTypography() 로 **기본값**을 지정
  - 이 기본값은 CompositionLocalProvider를 통해 제공된 값이 없을 때 사용됩니다.
- MyTypography() 인스턴스를 2번 생성하는것 같은 부분에 대해 해석
  - 기본값 설정: `staticCompositionLocalOf { MyTypography() }`는 CompositionLocal의 기본값을 설정하는 역할을 합니다.
  - 우선 순위: CompositionLocalProvider를 통해 전달된 값이 있으면, 해당 값이 우선적으로 사용되며, 기본값은 사용되지 않습니다.
  - 안전성: 기본값을 설정해 둠으로써, 특정 CompositionLocal이 실제로 제공되지 않는 경우에도 안전하게 기본 인스턴스를 사용할 수 있습니다.

## compositionLocalOf
- CompositionLocalProvider를 사용하여 제공할 수 있는 CompositionLocal 키를 생성합니다. 
- 재구성 중에 제공된 값을 변경하면 CompositionLocal.current를 사용하여 값을 읽은 CompositionLocalProvider의 내용이 무효화됩니다.
- compositionLocalOf는 CompositionLocalProvider에 대한 호출에 사용할 수 있는 ProvidableCompositionLocal을 생성합니다. 
- MutableList와 List와 유사하게, 키가 ProvidableCompositionLocal이 아닌 CompositionLocal로 공개되면 CompositionLocal.current를 사용하여 읽을 수는 있지만 다시 제공되지는 않습니다.
- 
```kotlin
fun <T> compositionLocalOf(
    policy: SnapshotMutationPolicy<T> =
        structuralEqualityPolicy(),
    defaultFactory: () -> T
): ProvidableCompositionLocal<T> = DynamicProvidableCompositionLocal(policy, defaultFactory)
```
## AndroidCompositionLocals.android
- Android 구성. 구성은 UI를 구성하는 방법을 결정하는 데 유용합니다.

### LocalContext
- Android 애플리케이션에서 사용할 수 있는 컨텍스트를 제공합니다.

```kotlin
/**
 * Provides a [Context] that can be used by Android applications.
 */
val LocalContext = staticCompositionLocalOf<Context> {
    noLocalProvidedFor("LocalContext")
}
```

```kotlin
val context = LocalContext.current
```