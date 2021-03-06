# Factory Method 패턴

### Factory Method패턴이란
위의 3장에서 이야기 했던 Template Method를 인스턴스 생성에 적용한 것이 바로 Factory Method이다.\
Factory Method는 인스턴스 만드는 방법은 상위클래스에서 결정하지만, 구체적인 클래스의 이름까지는 정하지 않는다.\
구체적인 내용은 모두 하위 클래스에서 결정한다.\
설명만으로는 이해가 힘드니 바로 예제를 보며 확인하자.
### 인스턴스 생성의 framework 부분
```java
public abstract class Product {
    public abstract void use();
}
```
```java
public abstract class Factory {
    public final Product create(String owner){
        Product p = createProduct(owner);
        registerProduct(p);
        return p;
    }
    protected abstract Product createProduct(String owner);
    protected abstract void registerProduct(Product product);
}
```
추상 클래스 Product와 Factory이다. 여기서 Factory클래스를 유심히 보자.\
Template Method를 사용하여 추상메소드들을 어떻게 사용하겠다 정도의 흐름만 작성되어있다.\
위 코드에서는 createProduct메소드로 Product 클래스를 갖는 변수 p를 만들고, registerProduct 메소드로 상품을 등록해주고 있다.\
여기서 핵심은 바로 

> Product p = createProduct(owner);

이부분 이다. new 를 사용하여 실제 인스턴스를 생성하는 대신에, createProduct 메소드를 호출하여 직접적인 인스턴스 호출을 하위 클래스에 맡겨두고 있다.\
이는 상위 클래스에서 구체적인 클래스 이름을 몰라도 인스턴스 생성이 가능해지기 때문에, 실제 구현 클래스에 의존하지 않게 되는 것이다.\
아래 코드는 위 추상 클래스들을 상속한 하위 클래스들이다.
### 구체적인 구현이 되어있는 하위 클래스 부분
```java
public class IDCard extends Product {
    private String owner;
    IDCard(String owner){
        System.out.println(owner+"의 카드를 만듭니다.");
        this.owner = owner;
    }
    @Override
    public void use() {
        System.out.println(owner+"의 카드를 사용합니다.");
    }
    public String getOwner(){
        return owner;
    }
}
```
```java
public class IDCardFactory extends Factory {
    private List owners = new ArrayList();

    @Override
    protected Product createProduct(String owner) {
        return new IDCard(owner);
    }

    @Override
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner());
    }
    public List getOwners(){
        return owners;
    }
}
```
Factory 메소드를 상속받아 구현된 IDCardFactory 중 createProduct 메소드 부분을 보자. new 를 사용하여 Product 클래스를 상속받아 구현된\
IDCard 인스턴스를 새로 생성하고, 그걸 return 하고 있다. 위에서 설명한 것 처럼, 하위 클래스에서 직접적인 구현을 하였기 때문에,\
Factory 클래스는 메소드 호출 하나만으로 새로운 인스턴스 생성이 가능한 것이다.\
마지막으로 Main 클래스를 보자.
```java
public class Main {
    public static void main(String[] args){
        Factory factory = new IDCardFactory();
        Product p1 = factory.create("홍길동");
        Product p2 = factory.create("고길동");
        Product p3 = factory.create("윤길동");

        p1.use();
        p2.use();
        p3.use();

        System.out.println(((IDCardFactory)factory).getOwners());
    }

}
```
factory 변수에 new 를 사용하여 IDCardFactory 클래스 인스턴스를 생성해 주었고, create 메소드를 이용하여 위의 IDCard 를 생성해주고 있다.\
출력 결과는 다음과 같다
> 홍길동의 카드를 만듭니다. \
> 고길동의 카드를 만듭니다. \
> 윤길동의 카드를 만듭니다. \
> 홍길동의 카드를 사용합니다. \
> 고길동의 카드를 사용합니다. \
> 윤길동의 카드를 사용합니다.\
> [홍길동, 고길동, 윤길동]

### Factory Method 구현 방법
1. 추상 메소드를 이용하여 구현 (위의 예제 코드)
2. 디폴트 구현을 만들어두어 하위 클래스에서 구현하지 않았을 때 사용.
3. 예외를 throw하여 구현하도록 알림. (단 이땐 Exception을 별도로 작성해야 함.)

### Factory Method는 왜 사용할까?
1. 상위 클래스는 결국 하위 클래스에서 생성되는 객체에 대한 정확한 타입을 몰라도 된다.
2. 상세 구현이 결국 하위 클래스에서 이루어지므로 확장에 용이해진다.
3. 상위 클래스를 상속받아 구현되므로 동일한 프로그래밍이 가능하다.
4. 객체간의 결합도가 낮아져 실제 구현 클래스에 의존하지 않게 된다.