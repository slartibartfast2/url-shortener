package ea.slartibartfast2.urlshortener.configuration.interceptor;

import ea.slartibartfast2.urlshortener.configuration.annotation.RequestCounter;
import ea.slartibartfast2.urlshortener.service.RequestCounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class RequestCountAspect {

    private final RequestCounterService requestCounterService;

    @AfterReturning("@annotation(requestCounter)")
    public void after(final RequestCounter requestCounter) {
        String key = requestCounter.operationName() + ":request-count";
        requestCounterService.incrementRequestCountByWindow(key, 5);
    }
}
