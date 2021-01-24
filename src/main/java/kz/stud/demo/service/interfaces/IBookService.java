package kz.stud.demo.service.interfaces;

import kz.stud.demo.model.Book;

import java.util.List;

public interface IBookService {

    Book getBookById(Long id);
    List<Book> getBooks();
    void addBook(Book book);
    void updateBook(Book book) throws Exception;
    void removeBook(Long id);

}
