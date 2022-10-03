import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * launch : 쓰레드 차단없이 새 코루틴을 시작하고, 결과로 Job 이라는 걸 반환 하는 코루틴 빌더
 */

fun main() = runBlocking<Unit>{

    //launch 를 사요아면 쓰레드 차단없이 동작됨
//    launch {
//        delay(500)      //일시중단함수 Thread.sleep 과 유사. 쓰레드를 차단하지 않고서 일시 중단 시키게됨. 코루틴내에서 다른 일시중단 함수를 수행하게됨
//        println("World!")
////        Thread.sleep(500) //기존방식. 해당 쓰레드가 블로킹
//    }
//    println("Hello")

    val job1: Job = launch {
        val elapsedTime = measureTimeMillis{
            delay(150)
        }
        println("async task-1 $elapsedTime ms")
    }
    job1.cancel()

    val job2: Job = launch(start= CoroutineStart.LAZY) {
        val elapsedTime = measureTimeMillis{
            delay(100)
        }
        println("async task-2 $elapsedTime ms")
    }

    println("start task-2")
    job2.start()

}