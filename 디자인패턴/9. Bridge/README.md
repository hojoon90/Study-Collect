# Bridge 패턴

### Bridge 패턴이란
Bridge 패턴은 단어 뜻대로 두 개 사이를 연결해주는 패턴이다. Bridge패턴이 다리역할을 하는 두 곳은 **기능의 클래스 계층**과 **구현의 클래스 계층**이다.\
일단 기능의 클래스 계층과 구현의 클래스 계층이 어떤것인지 부터 알아보자.

### 기능의 클래스 계층과 구현의 클래스 계층

우리가 새로운 기능을 추가하는 경우를 생각해보자. 기존에 Temp라는 클래스가 존재하는데 여기에 새로운 기능을 추가하고 싶을 땐 \
우리는 Temp의 하위 클래스로 TempAdd 라는 클래스를 만든다. 간단한 표로 보면 아래와 같다\
* Temp
  * TempAdd

이 TempAdd는 기능을 추가하기 위해 만든 계층이다. Temp클래스를 상속받게 되는 것이다.\
상위클래스는 기본적인 기능을 가지고 있고, 하위 클래스에서 새로운 기능을 추가한다.\
새로운 기능을 추가하고 싶을 경우 클래스 계층안에서 자신이 원하는 기능과 가장 유사한 클래스를 찾아서 하위 클래스를 만든 후,\
목적한 기능을 추가한 새로운 클래스를 만드는 것이 **기능의 클래스 계층**이다. 간단하게 코드로 보면 아래와 같다.
```java
public class Temp {  // 상위클래스
    public void showName(){
        System.out.println("이름을 나타냅니다.");
    }
    
    public void showPhoneNum(){
        System.out.println("전화번호를 나타냅니다.");
    }
}
```
```java
public class TempAdd extends Temp{  // 하위클래스
    public void showAdress(){
        System.out.println("주소를 나타냅니다.");
    }
    
    public void showAllData(){
        showName();
        showPhoneNum();
        showAdress();
    }
}
```
\
이번엔 구현의 클래스계층에 대해 알아보자. 구현의 클래스는 우리가 앞에서부터 많이 봐왔던 추상클래스를 떠올리면 쉽다.\
