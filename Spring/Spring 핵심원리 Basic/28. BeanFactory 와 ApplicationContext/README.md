# BeanFactory 와 ApplicationContext

BeanFactory 와 ApplicationContext 모두 스프링 컨테이너.

**BeanFactory**
* 스프링 컨테이너의 최상위 인터페이스
* 스프링 빈을 관리하고 조회
* getBean 제공
* 대부분의 기능은 여기서 나옴

**ApplicationContext**
* BeanFactory 기능을 모두 상속받아 제공
* BeanFactory와의 차이는 수많은 부가기능을 제공. 아래는 상속받는 클래스
  * MessageSource: 국제화 기능. 각 나라에 맞는 언어를 출력할 수 있게 해줌.
  * EnvironmentCapable: 환경 설정. local, dev, prod 등의 세팅을 구분해서 처리
  * ApplicationEventPublisher: 이벤트를 발행하고 구독하는 모델을 지원
  * ResourcePatternResolver: 리소스등을 편리하게 조회.

결국 ApplicationContext는 'BeanFactory + 부가기능'을 제공\
그렇기에 ApplicationContext만을 사용함. 