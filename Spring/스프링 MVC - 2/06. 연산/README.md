# 연산

타임리프에서 사용하는 연산자에 대해 정리한다.  
연산자는 기본 문법들과 크게 다르지 않으나, html 태그와 부등호 표시가 동일하기 때문에 해당 부분만 유의해서 사용하면 된다.

```java
@GetMapping("operation")
public String operation(Model model){
    model.addAttribute("nullData", null);
    model.addAttribute("data", "Spring!");

    return "basic/operation";
}
```
연산을 위한 컨트롤러 생성. null데이터 일때 처리도 보여주기 위해 null도 모델에 넣어준다.

```html
<ul>
    <li>산술 연산
        <ul>
            <li>10 + 2 = <span th:text="10 + 2"></span></li>
            <li>10 % 2 == 0 = <span th:text="10 % 2 == 0"></span></li>
        </ul>
    </li>
    <li>비교 연산
        <ul>
            <li>1 > 10 = <span th:text="1 &gt; 10"></span></li>
            <li>1 gt 10 = <span th:text="1 gt 10"></span></li>
            <li>1 >= 10 = <span th:text="1 >= 10"></span></li>
            <li>1 ge 10 = <span th:text="1 ge 10"></span></li>
            <li>1 == 10 = <span th:text="1 == 10"></span></li>
            <li>1 != 10 = <span th:text="1 != 10"></span></li>
        </ul>
    </li>
    <li>조건식
        <ul>
            <li>(10 % 2 == 0)? '짝수':'홀수' = <span th:text="(10 % 2 == 0)? '짝수':'홀수'"></span></li>
        </ul>
    </li>
    <li>Elvis 연산자
        <ul>
            <li> ${data}?:'데이터가 없습니다.' = <span th:text="${data}?:'데이터가 없습니다.'"></span></li>
            <li> ${nullData}?:'데이터가 없습니다.' = <span th:text="${nullData}?: '데이터가 없습니다.'"></span></li>
        </ul>
    </li>
    <li>No-Operation
        <ul>
            <li> ${data}?: _ = <span th:text="${data}?: _">데이터가 없습니다.</span></li>
            <li> ${nullData}?: _ = <span th:text="${nullData}?: _">데이터가 없습니다.</span></li>
        </ul>
    </li>
</ul>
```
* 비교연산 부분을 보면 연산자를 사용하기 위해 &gt; 혹은 gt 로 값을 쓴 것을 볼 수있다. 부등호와 태그의 충돌을 피하기 위해서이다.
* gt,ge,lt,le 등으로 사용하면 된다.
* 조건식의 경우 기본 자바 3항연산자와 비슷하다.
* 엘비스 연산자의 경우 데이터가 있을 경우 true 부분을 생략한다.
  * 물음표 뒤에 값이 없는것을 확인할 수 있다.
* No-Operation의 경우 연산을 수행하지 않는다.
  * 엘비스 연산자를 통해 데이터가 있으면 데이터가 출력된다.
  * 반대로 데이터가 없을 경우 No-Operation(_) 을 실행한다.
  * 이렇게 되면 thymeleaf 렌더링이 되지 않는것처럼 실행된다. 출력 시 옆에 디폴트 데이터가 출력된다.