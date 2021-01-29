package kz.stud.demo.service.interfaces;

import kz.stud.demo.model.Book;

import java.util.List;

public interface IBookService {

    Book getBookById(Long id);
    List<Book> getBooks();
    Book addBook(Book book) throws Exception;
    Book updateBook(Book book) throws Exception;
    void removeBook(Long id);

}
