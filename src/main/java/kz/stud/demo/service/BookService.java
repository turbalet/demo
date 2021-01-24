package kz.stud.demo.service;

import kz.stud.demo.model.Book;
import kz.stud.demo.repository.BookRepository;
import kz.stud.demo.service.interfaces.IBookService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;


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
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book book) throws Exception {
        if(!bookRepository.existsById(book.getIsbn())){
            throw new Exception("Book doesn't exist!");
        }
        bookRepository.save(book);
    }

    @Override
    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }
}
