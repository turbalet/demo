package kz.stud.demo.controller;

import kz.stud.demo.model.Book;
import kz.stud.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addBook(@RequestBody Book book){
        try {
            bookService.addBook(book);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(@RequestBody Book book){

        try {
            bookService.updateBook(book);
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeBook(@PathVariable("id") Long id){
        try {
            bookService.removeBook(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.status(200).body("Book was removed");
    }
}
