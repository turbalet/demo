package kz.stud.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "Book")
public class Book {

    @Id
    private long isbn;
    private String title;
    private String description;
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date published;


}
