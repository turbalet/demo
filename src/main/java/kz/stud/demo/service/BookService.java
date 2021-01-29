package kz.stud.demo.service;

import kz.stud.demo.model.Book;
import kz.stud.demo.repository.BookRepository;
import kz.stud.demo.service.interfaces.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book addBook(Book book) throws Exception{
        if(bookRepository.findById(book.getIsbn()).isPresent()){
            throw new Exception("Book with this isbn already exists");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) throws Exception {
        if(!bookRepository.findById(book.getIsbn()).isPresent()){
            throw new Exception("Book doesn't exist!");
        }
       return bookRepository.save(book);
    }

    @Override
    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }
}
