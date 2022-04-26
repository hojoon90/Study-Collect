package iterator.example;



import iterator.example.domain.Book;
import iterator.example.service.BookShelf;
import iterator.example.service.Iterator;

public class Main {

    public static void main (String[] args){

        BookShelf bookShelf = new BookShelf();

        bookShelf.appendBook(new Book("1984"));
        bookShelf.appendBook(new Book("The Alchemist"));
        bookShelf.appendBook(new Book("The Little Prince"));
        bookShelf.appendBook(new Book("Bible"));
        bookShelf.appendBook(new Book("Clean Code"));

        //list 변경 후 추가 분
        bookShelf.appendBook(new Book("Head First Java"));
        bookShelf.appendBook(new Book("Harry Potter"));
        bookShelf.appendBook(new Book("What men Live By"));

        Iterator iterator = bookShelf.iterator();

        while (iterator.hasNext()){
            Book book = (Book) iterator.next();
            System.out.println(book.getName());
        }


    }

}
