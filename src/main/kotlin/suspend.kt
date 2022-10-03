import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//suspend 키워드가 붙게되면 일시중단이 가능한 함수
// suspend 함수는 일반함수를 마음껏 호출 할수있지만, 일반함수에선 suspend함수를 바로 호출할 수 없다.
suspend fun main() {
    doSomething()
}

fun printHello() = println("hello")

//corouinteScope를 사용하면 현재 쓰레드가 차단하지 않고 코루틴이 동작

suspend fun doSomething() = coroutineScope {

    launch {
        delay(200)
        printHello()
    }

    launch {
        printHello()
    }
}
