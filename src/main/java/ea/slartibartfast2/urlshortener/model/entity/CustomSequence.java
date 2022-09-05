package ea.slartibartfast2.urlshortener.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customSequences")
public class CustomSequence {

    @Id
    private String id;
    private long seq;
}