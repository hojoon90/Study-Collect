# 요청 매핑 - API

요청 매핑의 API 예시를 만들어보자.

회원에 대한 처리를 하는 컨트롤러를 만드는 것으로 예시 진행.\
총 5가지 기능을 만든다.
* 회원 목록 조회 GET /mapping/users
* 회원 추가 POST /mapping/users
* 회원 조회 GET /mapping/users/{userId}
* 회원 수정 PUT /mapping/users/{userId}
* 회원 삭제 DELETE /mapping/users/{userId}
```java
@RestController
//클래스 레벨에 @RequestMapping을 사용하면 하위 메소드에서 해당 정보를 조합하여 사용.
@RequestMapping("/mapping/users")
public class MappingClassController {

    @GetMapping
    public String user(){
        return "get Users";
    }

    @PostMapping
    public String addUser(){
        return "post User";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId){
        return "get userId = " + userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId){
        return "update userId = " + userId;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId){
        return "delete userId = " + userId;
    }

}
```