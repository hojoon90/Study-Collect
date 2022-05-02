# Factory Method 패턴

### Factory Method패턴이란
위의 3장에서 이야기 했던 Template Method를 인스턴스 생성에 적용한 것이 바로 Factory Method이다.\
Factory Method는 인스턴스 만드는 방법은 상위클래스에서 결정하지만, 구체적인 클래스의 이름까지는 정하지 않는다.\
구체적인 내용은 모두 하위 클래스에서 결정한다.\
설명만으로는 이해가 힘드니 바로 예제를 보며 확인하자.
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
```java
public class IDCard extends Product {
    private String owner;
    private int token;
    IDCard(String owner, int token){
        System.out.println(owner+"<"+token+">"+"의 카드를 만듭니다.");
        this.owner = owner;
        this.token = token;
    }
    @Override
    public void use() {
        System.out.println(owner+"<"+token+">"+"의 카드를 사용합니다.");
    }
    public String getOwner(){
        return owner;
    }
    public int getToken(){
        return token;
    }
}
```
```java
public class IDCardFactory extends Factory {
    private List owners = new ArrayList();

    @Override
    protected Product createProduct(String owner, int token) {
        return new IDCard(owner, token);
    }

    @Override
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner()+"_"+((IDCard)product).getToken());
    }
    public List getOwners(){
        return owners;
    }
}
```