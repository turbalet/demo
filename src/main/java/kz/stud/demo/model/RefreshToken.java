package kz.stud.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "tokens")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    private long id;

    private String token;

    private Instant createdDate;

}
