# 스프링 MVC - 시작하기

스프링이 제공하는 컨트롤러는 애노테이션 기반 -> 유연하고 실용적.

### @RequestMapping
* 스프링에서 만든 유연하고, 실용적인 컨트롤러 애노테이션.
* 핸들러 매핑과 핸들러 어댑터에서 가장 1순위에 있던 RequestMappingHandlerMapping과 RequestMappingHandlerAdapter로 처리.
* 애노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터.

### Controller 생성
스프링 방식을 이용한 컨트롤러 작성
```java
@Controller
public class SpringMemberFormControllerV1 {
    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }
}
```
* @Controller: 스프링에서 자동으로 빈으로 등록(내부에 @Component 존재). 스프링 MVC에서 애노테이션 기반 컨트롤러
* @RequestMapping: 요청정보 매핑. 애노테이션 기반 동작.

### 나머지 컨트롤러 생성
기존 리스트 조회 및 저장 컨트롤러도 만들어준다.
```java
@Controller
public class SpringMemberListControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {

        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }
}

@Controller
public class SpringMemberSaveControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);

        return mv;
    }
}
```
ModelAndView 의 경우 addObject()를 통해 모델 데이터를 추가해줌.

