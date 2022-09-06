package ea.slartibartfast2.urlshortener.configuration.interceptor;

import ea.slartibartfast2.urlshortener.configuration.annotation.RateLimiter;
import ea.slartibartfast2.urlshortener.exception.RateLimitExceededException;
import ea.slartibartfast2.urlshortener.model.vo.LimitRateVo;
import ea.slartibartfast2.urlshortener.service.BasicCountingSemaphoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class RateLimiterAspect {

    private final BasicCountingSemaphoreService basicCountingSemaphoreService;

    @Around("@annotation(rateLimitAnnotation)")
    public Object around(final ProceedingJoinPoint joinPoint, RateLimiter rateLimitAnnotation) throws Throwable {
        var key = rateLimitAnnotation.operationName() + ":semaphore";
        var uuid = "";
        Object result;
        LimitRateVo limitRateVo = LimitRateVo.builder()
                                             .key(key)
                                             .windowSize(rateLimitAnnotation.windowSize())
                                             .limit(rateLimitAnnotation.limit())
                                             .windowTimeUnit(rateLimitAnnotation.windowTimeUnit())
                                             .build();
        try {
            uuid = basicCountingSemaphoreService.limitExceeded(limitRateVo);
            if (uuid == null) {
                throw new RateLimitExceededException("rate limit exceeded for this operation!");
            }

            result = joinPoint.proceed();
        } finally {
            basicCountingSemaphoreService.release(uuid, limitRateVo);
        }

        return result;
    }
}
