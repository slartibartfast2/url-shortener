package ea.slartibartfast2.urlshortener.controller;

import ea.slartibartfast2.urlshortener.model.dto.UrlRequestCountResponse;
import ea.slartibartfast2.urlshortener.service.RequestCounterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RequestCountController {

    private final RequestCounterService requestCounterService;

    @Operation(method = "Redirect", description = "Finds original url from short url and redirects")
    @GetMapping(value = "/request/window/{windowInMinute}")
    public UrlRequestCountResponse getAndRedirect(@PathVariable Integer windowInMinute) {
        return requestCounterService.retrieveRequestCount(windowInMinute);
    }
}
