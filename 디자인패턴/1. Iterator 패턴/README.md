# Iterator 패턴

## Iterator 패턴이란

우리가 기본적인 for문을 실행할 때, 다음과 같이 코드를 사용.
```
arr = {'apple', 'banana', 'grape', 'mango', 'orange', 'kiwi'}
for(int i = 0; i < arr.length; i++){
    System.out.println(arr[i]);
}
```
i -> 하나씩 증가하면서 배열을 순차적으로 검색하는 역할을 함.\
바로 이 i의 역할을 추상화하여 일반화 한 것이 **Iterator패턴**.\
즉, 많은 데이터를 순차적으로 지정하여 전체를 검색할 수 있도록 해주는 패턴.\
\
옳은 예시인지는 모르겠지만, 배열을 위치를 가리키는 일종의 커서(Cursor)라고 생각함.\
\
작성한 예시 코드를 함께 올림.
