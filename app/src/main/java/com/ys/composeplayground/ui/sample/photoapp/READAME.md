# [PhotoApp](https://github.com/andkulikov/compose-photoapp/tree/master)

## SideEffect
- 현재 컴포지션이 성공적으로 완료되고 변경 사항을 적용할 때 실행할 효과를 예약합니다.
- `SideEffect`를 사용하면 스냅샷으로 백업되지 않는 컴포넌트로 관리되는 오브젝트에 부작용을 적용하여 현재 컴포넌트 작업이 실패할 경우 해당 오브젝트가 일관되지 않은 상태로 남지 않도록 할 수 있습니다. 
- 이펙트는 항상 컴포넌트의 적용 디스패처에서 실행되며, 적용자는 자신이나 다른 적용자, 컴포넌트 트리에 변경 사항을 적용하거나 RememberObserver 이벤트 콜백을 실행하는 것과 동시에 실행되지 않습니다. `SideEffect`는 항상 RememberObserver 이벤트 콜백 이후에 실행됩니다.
- `SideEffect`는 모든 재구성 후에 실행됩니다. 
- 잠재적으로 많은 재구성에 걸쳐 진행 중인 작업을 시작하려면 `LaunchedEffect`를 참조하세요. 
- 이벤트 구독 또는 기타 객체 수명 주기를 관리하려면 `DisposableEffect`를 참조하세요.

## produceState
- 정의된 데이터 소스 없이 시간이 지남에 따라 값을 생성하는 관찰 가능한 스냅샷 State를 반환합니다. 
- produceState가 컴포넌트에 진입할 때 producer가 시작되고 produceState가 컴포넌트를 떠나면 취소됩니다. producer는 반환된 State에 새 값을 설정하려면 ProduceStateScope.value를 사용해야 합니다. 
- 반환된 State는 값을 병합합니다. ProduceStateScope.value를 사용하여 이전 값과 동일한 값을 설정하는 경우 변경 사항을 관찰할 수 없으며, 여러 값이 빠르게 연속적으로 설정된 경우 관찰자는 최신 값만 볼 수 있습니다. 
- 예를 들어, 외부 데이터의 일시 중단 또는 일시 중단되지 않은 소스를 관찰하는 데 produceState를 사용할 수 있습니다:

## Layout Composable  
- 0개 이상의 레이아웃 자식을 측정하고 위치를 지정하는 데 사용할 수 있습니다.
- 이 레이아웃의 측정, 레이아웃 및 내재적 측정 동작은 측정정책 인스턴스에 의해 정의됩니다.

## rememberSaveable
- init에서 생성된 값을 기억합니다.
- remember와 비슷하게 작동하지만 저장된 값은 저장된 인스턴스 상태 메커니즘을 사용하여 액티비티 또는 프로세스 재생성 후에도 살아남습니다(예: Android 애플리케이션에서 화면이 회전될 때 발생).