package ea.slartibartfast2.urlshortener.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "urls")
public class Url {

    @Id
    private Long id;

    private String longUrl;
    private Date createdDate;
    private Date expiresDate;
}
