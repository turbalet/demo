package kz.stud.demo.service;

import kz.stud.demo.model.Book;
import kz.stud.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;



    @Test
    void getBookById() {
        Book book = new Book(1234L, "Title", "desc", "Author", new Date());
        when(bookRepository.findById(1234L)).thenReturn(Optional.of(book));

        Book expected = bookService.getBookById(book.getIsbn());

        assertNotNull(expected);
    }

    @Test
    void getBooks() {
        List<Book> list = new ArrayList<>();

        list.add(new Book(1234L, "Title", "desc", "Author", new Date()));
        list.add(new Book(12345L, "Title2", "Desc2", "Author2", new Date()));

        given(bookRepository.findAll()).willReturn(list);

        List<Book> books = bookService.getBooks();
        assertEquals(books, list);
    }

    @Test
    void addBook() throws Exception {

        Book book = new Book(444444L, "Title", "desc", "Author", new Date());
        given(bookRepository.findById(book.getIsbn())).willReturn(Optional.empty());
        given(bookRepository.save(book)).willReturn(book);
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
    }

    @Test
    void updateBook() throws Exception {
        Book book = new Book(1L, "Title", "desc", "Author", new Date());
        given(bookRepository.save(book)).willReturn(book);
        given(bookRepository.findById(book.getIsbn())).willReturn(Optional.of(book));
        Book expected = bookService.updateBook(book);
        assertNotNull(expected);
    }

    @Test
    void deleteBook(){
        long isbn = 1234;
        bookService.removeBook(isbn);
        verify(bookRepository, times(1)).deleteById(isbn);
    }
}