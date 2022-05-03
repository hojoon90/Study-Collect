package factory.example;

import factory.example.framework.Factory;
import factory.example.framework.Product;
import factory.example.idcard.IDCardFactory;

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
