import kotlinx.coroutines.runBlocking


fun main() {

    //runBlocking 은 코루틴을 생성해주는 코루틴 빌더
    //runBlocking 에 있는 코드가 다 완료되면 다음라인의 코드가 실행됨.
    //일반적으로는 잘 사용하지 않음. 코드 특정 라이브러리나 프레임워크에서 지원해 주지 않는경우. 스프링 MVC 스타일로 작성해야 되기 때문에 그러한 목적으로 사용
    runBlocking {
        println("Hello")
        println(Thread.currentThread().name)
    }

    println("World")
    println(Thread.currentThread().name)

}

// -Dkotlinx.coroutines.debug 옵션을 주어야 @countine 이 결과에 나옴