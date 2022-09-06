package ea.slartibartfast2.urlshortener.configuration.annotation;

import ea.slartibartfast2.urlshortener.model.WindowTimeUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {

    int windowSize() default 5;

    WindowTimeUnit windowTimeUnit() default WindowTimeUnit.MINUTE;

    long limit() default 10000L;

    String operationName();
}
