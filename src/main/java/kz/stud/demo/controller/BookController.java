package kz.stud.demo.controller;

import kz.stud.demo.model.Book;
import kz.stud.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public List<Book> books() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public Book bookById(@PathVariable("id") Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<?> addBook(Book book){
        try {
            bookService.addBook(book);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateBook(Book book){

        try {
            bookService.updateBook(book);
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Book was updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeBook(@PathVariable("id") Long id){
        try {
            bookService.removeBook(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.status(200).body("Book was removed");
    }
}
