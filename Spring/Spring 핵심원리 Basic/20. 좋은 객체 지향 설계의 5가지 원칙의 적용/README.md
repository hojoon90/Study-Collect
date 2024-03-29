# 좋은 객체 지향 설계의 5가지 원칙의 적용

지금까지 진행한 프로젝트에서는 3가지의 원칙이 적용되었음
* SRP
* DIP
* OCP

**SRP (단일 책임 원칙)**
> 한 클래스는 하나의 책임만 가져야한다는 원칙
* 클라이언트 객체는 기존에 구현객체를 직접 생성하고 연결하여 실행하는 다양한 책임을 갖고 있었음.
* 그렇기에 역할을 분리해주어 실행에 대한 책임만 지도록 변경해줌.
* 구현 객체의 생성, 연결을 AppConfig에서 처리하도록 변경
* SRP 원칙을 따르게 됨.

**DIP (의존관계 역전 원칙)**
> 추상화에 의존해야 하며 구현체에 의존해서는 안된다. 의존성 주입은 이 원칙을 따르는 방법중 하나이다.
* 클라이언트 객체에서 새로운 할인정책 적용 시 기존엔 코드 변경이 필요했다.
* 클라이언트(OrderServiceImpl)가 인터페이스만 의존하는것 같았지만 구체화 클래스 역시 함께 의존했기 때문.
* 그렇기에 처음엔 구체화 클래스만 지워 인터페이스에 의존하도록 변경함.
* 그러나 인터페이스만으로는 클라이언트 코드를 실행할 수 없었음.
* 그래서 AppConfig가 구현클래스를 대신 생성하도록 하고, 클라이언트 코드는 해당 구현 클래스를 의존받아서 처리하도록 했다.

**OCP**
> 확장에는 열려있고 변경에는 닫혀있다.
* 클라이언트 코드는 DIP 원칙을 지킴
* 앱을 사용 영역(클라이언트 코드)과 구성 영역(AppConfig)으로 분리함.
* 기능 확장 시 구성 영역에서 코드 변경만 진행해주면 사용 영역을 별도로 변경할 필요가 없어진다.
* 즉 소프트웨어 요소를 확장해도 사용 영역의 변경은 닫혀있게 된다.