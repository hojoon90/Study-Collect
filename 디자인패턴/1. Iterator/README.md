# Iterator 패턴

### Iterator 패턴이란
우리가 기본적인 for문을 실행할 때, 다음과 같이 코드를 사용한다.
```java
String[] arr = {"apple", "banana", "grape", "mango", "orange", "kiwi"};
for(int i = 0; i < arr.length; i++){
    System.out.println(arr[i]);
}
```
i -> 하나씩 증가하면서 배열을 순차적으로 검색하는 역할을 한다.\
바로 이 i의 역할을 추상화하여 일반화 한 것이 **Iterator패턴**이다.\
즉, 많은 데이터를 순차적으로 지정하여 전체를 검색할 수 있도록 해주는 패턴을 말한다.\
\
옳은 예시인지는 모르겠지만, 배열을 위치를 가리키는 일종의 커서(Cursor)라고 생각한다.\
\
작성한 예시 코드를 함께 올려본다.

### 간단한 코드 설명
```java
public interface Iterator {
    public abstract boolean hasNext();
    public abstract Object next();
}
```
코드 중 Iterator는 추상메소드로서 hasNext()와 next()를 가진다.\
hasNext()는 다음 요소가 있는지 확인하여 true, false를 리턴해주는 메소드이며, next()는 리스트 혹은 배열의 현재 요소를 반환해준다.\
이 뿐만 아니라, 다음 next() 호출 시, 다음 요소를 반환할 수 있게 준비해준다.\
간단하게 이야기해 현재 것을 반환해줌과 동시에 다음 위치에서 대기하고 있는 것이다.
```java
@Override
public Iterator iterator() {
    return new BookShelfIterator(this);
}
```
현재 올린 예시코드에서 Iterator는 BookShelf 클래스에서 iterator라는 메소드로 선언되어있고,\
Iterator의 구현체인 BookShelfIterator 클래스의 인스턴스를 생성하여 반환해준다.
```java
@Override
public boolean hasNext() {
    if (index < bookShelf.getLength()){
        return true;
    }else{
        return false;
    }
}

@Override
public Object next() {
    Book book = bookShelf.getBookAt(index);
    index++;
    return book;
}
```
BookShelfIterator는 Iterator의 구현체로서, hasNext()는 위의 Iterator에서 이야기한 대로 다음 요소(여기서는 BookShelf 길이와 index를 체크)\
가 있는지 확인하는 코드가 구현되어있고, next()는 요소(여기`는 Book)를 반환해줌과 동시에 index를 1 증가시켜 다음 요소를 가리키도록 한다.

### 왜 Iterator패턴을 쓸까.
Iterator패턴을 쓰는 이유는 간단하다. 구현된 클래스와 분리하여 **단독적으로 하나씩 배열의 요소를 셀 수 있기 때문**이다.\
```java
Iterator iterator = bookShelf.iterator();

while (iterator.hasNext()){
    Book book = (Book) iterator.next();
    System.out.println(book.getName());
}
```
위의 코드에서 while문을 보면 iterator의 hasNext()와 next()메소드만을 사용한 것을 볼 수 있다.\
BookShelf 클래스안에서 List 대신 Vector 혹은 배열을 사용해도, Iterator만 제대로 구현되어 있다면 안의 요소를 찾는데 아무 문제가 없어진다.\
이는 클래스가 부품처럼 사용 될 수 있으며, 수정할 부분도 작아진다는 것을 의미한다.

