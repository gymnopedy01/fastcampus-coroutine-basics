import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

// 코루틴에서 리액티브 스타일로 쓸수있도록 만들어진 API
// 코루틴의 서스펜드 함수는 모노처럼 단일 값을 비동기로 반환하지만
//flow 를 사용하게 되면 리액터의 플럭스처럼 여러값을 반환할수 있게됨
fun main() = runBlocking<Unit> {

    val flow = simple()
    flow.collect { value -> println(value) }

}

fun simple(): Flow<Int> = flow {
    println("Flow started")

    for (i in 1..3) {
        delay(100)
        emit(i)
    }

}