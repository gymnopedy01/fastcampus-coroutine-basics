# 1. 코루틴

- `코루틴(Coroutine)은` 코틀린에서 비동기-논블로킹 프로그래밍을 명령형 스타일로 작성할 수 있도록 도와주는 라이브러리이다
- 코루틴은 멀티 플랫폼을 지원하여 코틀린을 사용하는 안드로이드, 서버등 여러 환경에서 사용할 수 있다
- 코루틴은 `일시 중단 가능한 함수(suspend function)` 를 통해 스레드가 실행을 잠시 중단했다가 중단한 지점부터 다시 `재개(resume)` 할 수 있다

## 코루틴을 사용한 구조적 동시성 예시

``` kotlin
//getApi1, getApi2 를 async 빌더로 비동기적으로호출
// await이라는걸 이용해서 apiResult를 반환
suspend fun combineApi() = coroutineScope {
    val response1 = async {getApi1()}
    val response2 = async {getApi2()}
    
    return ApiResult (
        response1.await()
        response2.await()
    )
}
```

# 2.스프링 WebFlux의 코루틴 지원

- 스프링 WebFlux 공식문서의 코틀린 예제들을 보면 모두 코루틴 기반의 예제를 소개하고 있다

- 스프링 MVC, 스프링 WebFlux 모두 코루틴을 지원하여 의존성만 추가하면 바로 사용가능
- 아래 의존성을 build.gradle.kts 에 추가하면 코루틴을 사용할 수 있다

``` kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${version}")
}
```

## 리액티브가 코루틴으로 변환되는 방식

``` kotlin
//Mono -> suspend
fun handler(): Mono<Void> -> suspend fun handler()

//Flux -> Flow
fun handler(): Flux<T> -> fun handler(): Flow<T>
```

- Mono는 suspend 함수로 변환
- Flux는 Flow로 변환

## 코루틴을 적용한 컨트롤러 코드

``` kotlin

    @RestController
    class UserController(
        private val userService : UserServce,
        private val userDetailServe: UserDetailService
    ) {
        @GetMapping("/{id}")
        suspend fun get(@PathVariable id: Long) :User{
            return userService.getById(id)
        }
        
        @GetMapping("/users")
        suspend fun gets() = withContext(Dispatchers.IO) {
            val userDeffered = async { userService.gets() }
            val userDetailDeffered = async { userDetailService.gets() }
            return UserList(usersDeffered.await(), userDetailDeffered.await())
        }
    }
    
```

## 코틀린을 사용한 WebClient

```kotlin
    val client = WebCliennt.create("https://example.org")
val result = client.get()
    .uri("/persons/{id}", id)
    .retrieve()
    .awaitBody<Person>()
```

- 기존 리액티브 코드를 코루틴으로 변환하고 싶다면 awaitXXX 시작하는 확장 함수를 사용하면 즉시 코루틴으로 변환할 수 있다.

# Spring Data R2DBC 의 ReactiveCrudRepository 에서 코루틴 적용

```kotlin
interface ContentReactiveRepository : ReactiveCrudRepository<Content, Long> {
    fun findByUserId(userId: Long): Mono<Content>
    fun findAllByUserId(userId: Long): Flux<Content>
}
class ContentService(
    val repository: ContentReactiveRepository
) {
    fun findByUserIdMono(userId: Long): Mono<Content> {
        return repository.findByUserId(userId)
    }
    suspend fun findByUserId(userId: Long): Content {
        return repository.findByUserId(userId).awaitSingle()
    }

}
```

## CoroutineCrudRepository 를 사용하면 awaitXXX 코드 없이 사용가능

```kotlin
interface ContentCoroutineRepository : CoroutineCrudRepository<Content, Long> {
    suspend fun findByUserId(userId: Long): Content?
    fun findAllByUserId(userId: Long): Flow<Content>
}

class ContentService(
    val repository: ContentCoroutineRepository
) {
    suspend fun findByUserId(userId: Long): Content {
        return repository.findByUserId(userId)
    }
}
```