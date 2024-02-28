# Error와 Exception 정리

자바는 프로그램에 문제가 발생했을 때 크게 오류(Error)와 예외(Exception) 두가지 케이스로 문제점을 사용자에게 전달해준다.  
**에러**는 주로 JVM에서 발생하며, **예외**는 주로 애플리케이션 단에서 발생한다.

## Error
* 시스템이 대부분 비정상적인 조건일 경우 발생.
* 일반적으로 애플리케이션 단에서 복구할 수 없는 에러들을 나타낸다. 
* 이는 개발자가 처리할 수 없으며 예측하기도 쉽지 않은 부분이다.
* 단 ThreadDeath 의 경우 정상 조건이지만 Error의 하위 클래스로 들어간다.
  * 대부분 애플리케이션이 예외를 처리해선 안되기 때문이다.

## Exception
* 입력값의 처리가 불가능한 경우, 혹은 정상적인 프로그램의 흐름에 어긋나는 경우 발생.
* 개발자의 실수등으로 예외적인 상황이 발생했을 때 나타난다.
* 프로그램이 종료될 정도의 심각한 문제는 아니며, 개발자가 처리할 수 있고 예측 역시 가능하다.
* 예외는 크게 Checked Exception 과 Unchecked Exception 으로 구분된다.

### Checked Exception
* RuntimeException을 상속받지 않는 예외들을 나타냄
* Checked Exception의 경우 예외를 처리해주지 않으면 컴파일 에러가 발생한다.
* 즉 컴파일 시점에 예외를 확인함.
* 명시적으로 예외를 처리해주어야 하는 예외이다.
* IOException 혹은 ClassNotFoundException등이 있다.
* 예외 발생 시 롤백처리가 되지 않는다.
  * 즉 예외 발생 전까지의 데이터는 처리된다.


### Unchecked Exception
* RuntimeException을 상속받는 모든 예외들을 나타냄
* JVM의 정상적인 작동 중에 발생할 수 있는 예외들.
* 명시적으로 예외처리를 해주지 않아도 프로그램 동작에는 이상이 없다.
* 런타임 시점에 예외를 확인함.
* NullPointException, IndexOutOfBoundException 등이 있다.
* 예외가 발생하면 롤백 처리를 한다.
  * 데이터가 처리되고 있더라도 예외 발생 전으로 되돌아간다.



## 참조 URL
https://docs.oracle.com/javase/6/docs/api/java/lang/Error.html  
https://docs.oracle.com/javase/6/docs/api/java/lang/Exception.html  
https://hahahoho5915.tistory.com/67
https://devlog-wjdrbs96.tistory.com/351