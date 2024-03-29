# IoC, DI 그리고 컨테이너

### 제어의 역전 IoC
* 말 그대로 제어를 다른 코드 혹은 프로그램이 해주는 것을 제어의 역전이라고 함.
* 기존은 구현 객체들이 프로그램의 흐름을 조종하였다. 필요할경우 new를 생성한다던지...
* 하지만 AppConfig와 같은 제어해주는 코드가 생기면 클라이언트 코드는 실행에 대한 역할만 담당한다.
* 제어는 모두 AppConfig가 가지는 것이다.
* 우리가 만든 소스에서는 OrderServiceImpl도 AppConfig가 생성하므로 만약 AppConfig가 OrderServiceImpl을 사용하지 않을수도 있다.
* 하지만 OrderServiceImpl은 그런거에 상관없이 자신의 코드만 실행할 뿐임.

### 프레임워크와 라이브러리
* 프레임워크는 내가 작성한 코드를 제어하고 대신 실행해준다. 
  * JUnit의 경우도 어노테이션(@)등을 걸고 개발자가 필요한 코드만 작성하면 JUnit이 알아서 실행해줌.
* 라이브러리는 내가 작성한 코드가 직접 제어의 흐름을 담당하는 것이다.
  * JSON 변환 객체를 불러다 쓰는 것과 같은것들이 모두 라이브러리임.

### 의존 관계 주입
* 우리가 지금까지 만든 코드들은 모두 인터페이스에 의존하고 있다. 어떤 구현객체가 넘어올지 모르는 것.
* 의존 관계는 정적인 의존 관계와 앱 실행시 결정되는 동적 의존 관계 두가지를 분리해서 생각해야 함.

#### 정적 의존 관계
* 간단하게 클래스 안의 import 된 객체들만 보고 의존 관계를 파악하는 것.
* 굳이 앱을 실행하지 않아도 분석 가능
* 정적 의존 관계 만으로는 실제 어떤 객체가 실행 될 지 까지는 알 수 없다.
  * 인터페이스만 사용할 경우 실제 구현 객체를 모르게 됨.

#### 동적 의존 관계
* 앱 실행 시점에서 실제 생성된 객체의 참조가 연결 된 의존 관계.
* 앱을 실행 했을 때 알 수 있음.


* 앱 실행 시점에서 외부에서 객체를 생성하고 각 클라이언트 코드에 전달하여 의존 관계가 연결되는 것을 의존관계 주입이라고 한다.
* AppConfig가 객체를 생성하여 클라이언트에 주입해주는 것과 똑같음.
* 의존 관계 주입을 사용할 경우 클라이언트 코드 변경 없이 호출하는 대상의 타입 인스턴스 변경 가능
  * FixDiscountPolicy -> RateDiscountPolicy로 쉽게 변경 가능
* 의존 관계 주입을 사용할 경우 정적 의존 관계는 변경할 필요 없이 동적 의존 관계만 변경이 가능하다.

### 컨테이너
* AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해주는 것을 IoC 컨테이너 혹은 DI 컨테이너라고 함.
* 최근에는 DI 컨테이너라고 많이 부름
* 어셈블러, 오브젝트 팩토리 등으로도 부름.