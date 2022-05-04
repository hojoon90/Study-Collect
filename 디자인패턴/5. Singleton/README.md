# Singleton 패턴

### Singleton 패턴이란
인스턴스를 단 **한개**만 만드는 패턴이다. 우리는 클래스의 인스턴스가 단 한개만 필요할 경우가 종종 있다.\
예를 들면 DB Connection Pool 과 같이 프로그램 실행 시 최초 한번만 생성되어 해당 리소스를 여러곳에서 공유할 때 사용된다.\
지정한 클래스의 인스턴스가 절대로 1개밖에 존재하지 않는 것을 보증하고 싶을때,\
인스턴스가 1개밖에 존재하지 않는 것을 프로그램상으로 표현하고 싶을때 바로 Singleton 패턴을 사용한다.\
바로 예제코드를 보자

### Singleton 생성

```java
public class Singleton {
    private static Singleton singleton = new Singleton();

    private Singleton() {
        System.out.println("인스턴스를 생성하였습니다.");
    }

    public static Singleton getInstance() {
        return singleton;
    }
}
```
위의 Singleton 클래스는 인스턴스를 1개밖에 만들 수 없으며 static 으로 선언된 singleton 변수에 저장되어 있다.\
해당 변수에 접근할 수 있는 방법은 static 으로 선언되어있는 getInstance()를 통해서만 접근이 가능하다.\
Singleton 인스턴스가 생성 될 시 생성자 안에 있는 "인스턴스를 생성하였습니다."를 출력하며, 이는 최초 1회만 출력된다.\
여기서 중요한 것은 Singleton() 생성자가 **private**로 선언되어있다는 것이다.\
private 으로 선언 시 외부에서 호출은 불가능하며, 이는 곧 인스턴스가 1개만 생성될 수 있도록 보증하는 것이라고 할 수 있다.\
다음 Main 메소드를 보자.

### Singleton 실행
```java
public class Main(){
    public static void main(String[] args){
        System.out.println("Start.");
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        if(s1 == s2){
            System.out.println("s1과 s2는 같은 인스턴스 입니다.");
        }else{
            System.out.println("s1과 s2는 다른 인스턴스 입니다.");
        }
        System.out.println("End.");
    }
}
```
Main 클래스는 Singleton 클래스를 각각 s1과 s2변수에 담는데, Singleton 클래스 안에 있는 getInstance를 통해 인스턴스를 받아오고 있다.\
if문에서는 두 변수가 같을 경우 서로 같다고 출력하며, 다를경우는 서로 다르다고 출력하게 된다.\
실제 실행을 해보면 다음과 같이 출력된다.
> Start.\
> 인스턴스를 생성하였습니다.\
> s1과 s2는 같은 인스턴스 입니다.\
> End.

Singleton.getInstance를 통해 가져온 인스턴스는 결국 서로 같다는 것을 나타낸다.\
인스턴스를 생성하는 시점을 한번 보자. Start가 출력된 후 "인스턴스를 생성하였습니다."라고 출력이 된다.\
이 시점은 s1에서 Singleton.getInstance()를 호출하였을 때의 시점이며, 이후 s2에서 Singleton.getInstance()를 호출하였을 때는\
인스턴스가 생성되었다는 메세지가 출력되지 않는다. 즉 최초 호출시에 한번 인스턴스가 생성되고, 이후 이 인스턴스가 그대로 유지된다고 볼 수 있는 것이다.

### Singleton 을 사용하는 이유?
Singleton 패턴은 결국 인스턴스의 중복생성을 막고 리소스 확보를 위해서이다. 위에서 이야기한 DB Connection Pool 을 예시로 생각해보자.\
DB 조회를 위해 커넥션을 맺을 떄, 최초 호출시엔 커넥션 풀 인스턴스를 생성하여 DB에 접근할 것이다. 결국 커넥션 풀이 생성되면 커넥션 풀 안에서\
DB 와의 접속을 맺어주고 끊어주는 행위가 이루어져야 하는데, Singleton 으로 생성되지 않으면 DB에 접근할때마다 새로운 Pool 이 생겨나게 될 것이다.\
새로운 Pool 이 생길 때마다 리소스가 사용될 것이고, DB 접근량이 더욱 많아지면 결국 리소스가 부족해지는 현상이 발생하게 될 것이다.\
이를 막기 위해선 Singleton으로 유일한 커넥션 풀을 만들어주고, 해당 풀 안에서 커넥션 관리가 되는 것이 가장 이상적이라고 할 수 있다.