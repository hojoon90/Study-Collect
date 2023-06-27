# 스프링 컨트롤러 통합 및 코드 단순화

### 컨트롤러 통합
만들어둔 컨트롤러들을 통합해두자.

```java
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        ...
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        ...
    }

    @RequestMapping
    public ModelAndView members() {
        ...
    }

}

```
컨트롤러들을 하나의 클래스에 통합했다.
* 기존 컨트롤러에서 중복된 URL 인 "/springmvc/v2/members" 를 컨트롤러 클래스 상단에 분리해줌.
* @RequestMapping 애노테이션을 통해 해당 패스로 접근시 컨트롤러가 호출.
* 각 메소드에도 해당 에노테이션을 달아주어 접근할 수 있도록 처리
* 경로가 없을 경우 클래스에 적어놓은 패스를 통해 해당 메소드가 호출된다.

### 코드 단순화
코드를 좀 더 단순화 시켜보자.
```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam("username") String userName, @RequestParam("age") int age, Model model) {

        Member member = new Member(userName, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

    @GetMapping
    public String members(Model model) {

        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }


}
```
* @RequestMapping -> 각 호출 메서드에 맞는 값으로 변경. 이렇게 되면 지정한 메서드와 다른 메서드로 호출 시 400에러 발생.
* save에서 HttpServletRequest, HttpServletResponse 대신 파라미터를 직접 받아오게 처리.
* 기존 ModelAndView 객체를 이용하여 데이터를 담아 반환.
  * Model 객체의 addAttribute를 이용해 데이터를 전달하고 논리 이름을 리턴하도록 변경.

해당 작업들로 인해 코드가 더욱 깔끔해졌다.