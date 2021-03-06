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
[LazyColumn](https://foso.github.io/Jetpack-Compose-Playground/foundation/lazyrow/)
[Offical Docs](https://developer.android.com/reference/kotlin/androidx/compose/foundation/lazy/package-summary#lazyrow)