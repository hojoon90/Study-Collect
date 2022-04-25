package iterator.example.service;

import iterator.example.domain.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookShelf implements Aggregate{

    private List<Book> books;

    public BookShelf() {
        this.books = new ArrayList<>();
    }

    public Book getBookAt(int index){
        return books.get(index);
    }

    public void appendBook(Book book){
        this.books.add(book);
    }

    public int getLength(){
        return this.books.size();
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }
}
