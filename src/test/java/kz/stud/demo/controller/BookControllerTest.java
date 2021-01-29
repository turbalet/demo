package kz.stud.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.stud.demo.model.Book;
import kz.stud.demo.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;


    @Test
    void books() throws Exception {
        List<Book> list = new ArrayList<>();
        list.add(new Book(1234L, "Title", "desc", "Author", new Date()));
        list.add(new Book(12345L, "Title2", "Desc2", "Author2", new Date()));
        given(bookService.getBooks()).willReturn(list);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(list.size())));
    }

    @Test
    void bookById() throws Exception {
        Book book = new Book();
        book.setIsbn(1L);
        book.setAuthor("author");
        given(bookService.getBookById(book.getIsbn())).willReturn(book);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", Matchers.is(book.getAuthor())));
    }

    @Test
    void addBook() throws Exception {
        Book book = new Book(124L, "Title", "Desc", "Author", null);
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", Matchers.is(book.getAuthor())));
    }

    @Test
    void updateBook() throws Exception {
        Book book = new Book(1L, "title", "desc", "author", null);
        given(bookService.getBookById(book.getIsbn())).willReturn(book);
        given(bookService.updateBook(book)).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is(book.getTitle())));
    }

    @Test
    void removeBook() throws Exception {
        Book book = new Book(1L, "title", "desc", "author", null);
        given(bookService.getBookById(book.getIsbn())).willReturn(book);
        doNothing().when(bookService).removeBook(book.getIsbn());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", book.getIsbn()))
                .andExpect(status().isOk());
    }
}