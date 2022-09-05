package ea.slartibartfast2.urlshortener.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Schema(description = "Request object for POST method")
public class LongUrlRequest {

    @Schema(required = true, description = "Url to convert to short")
    private String longUrl;

    @Schema(description = "Expiration datetime of url")
    private Date expiresDate;
}