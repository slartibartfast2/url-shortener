package ea.slartibartfast2.urlshortener.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object for GET method")
public class UrlRequestCountResponse {

    @Schema(description = "Request window")
    private String window;

    @Schema(description = "Request count in this specific window")
    private int count;
}
