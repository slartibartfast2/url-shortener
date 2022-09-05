package ea.slartibartfast2.urlshortener.controller;

import ea.slartibartfast2.urlshortener.model.dto.LongUrlRequest;
import ea.slartibartfast2.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private final UrlService urlService;

    @Operation(method = "Create short url", description = "Converts long url to short url")
    @PostMapping("create-short")
    public String convertToShortUrl(@RequestBody LongUrlRequest request) {
        return urlService.convertToShortUrl(request);
    }

    @Operation(method = "Redirect", description = "Finds original url from short url and redirects")
    @GetMapping(value = "{shortUrl}")
    public ResponseEntity<Void> getAndRedirect(@PathVariable String shortUrl) {
        var url = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                             .location(URI.create(url))
                             .build();
    }
}
