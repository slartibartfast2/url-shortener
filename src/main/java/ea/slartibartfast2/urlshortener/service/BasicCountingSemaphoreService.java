package ea.slartibartfast2.urlshortener.service;

import ea.slartibartfast2.urlshortener.model.WindowTimeUnit;
import ea.slartibartfast2.urlshortener.model.vo.LimitRateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicCountingSemaphoreService {

    private final RedisTemplate<String, Object> redisObjectTemplate;

    public String limitExceeded(LimitRateVo limitRateVo) {
        var currentMs = System.currentTimeMillis();
        var uuid = UUID.randomUUID().toString();
        var semaphoreName = prepareHashKey(limitRateVo);
        var windowInMillis = calculateWindowTimeAsMillis(limitRateVo.getWindowSize(), limitRateVo.getWindowTimeUnit());
        long windowSliceStart = (currentMs/windowInMillis) * windowInMillis;

        List<Object> objects = redisObjectTemplate.execute(BasicCountingSemaphore.of(uuid, windowSliceStart, semaphoreName, currentMs));
        Long newRank = (Long) getLast(objects);
        if (newRank < limitRateVo.getLimit()) {
            log.info("rate limit not exceeded, {} inserted to control set, current count: {}", uuid, newRank);
            return uuid;
        }

        redisObjectTemplate.boundZSetOps(semaphoreName).remove(uuid);
        log.warn("Rate limit exceeded! limit: {}, current count: {}, last added transaction was removed", limitRateVo.getLimit(), newRank);
        return null;
    }

    public void release(String uuid, LimitRateVo limitRateVo) {
        var semaphoreName = prepareHashKey(limitRateVo);
        redisObjectTemplate.boundZSetOps(semaphoreName).remove(uuid);
        log.info("{} removed from counting semaphore", uuid);
    }

    private <T> T getLast(List<T> list) {
        return list.stream().reduce((first, second) -> second).orElse(null);
    }

    private int calculateWindowTimeAsMillis(int windowSize, WindowTimeUnit windowTimeUnit) {
        switch (windowTimeUnit) {
            case HOUR: return windowSize*60*60*1000;
            case MINUTE: return windowSize*60*1000;
            case SECOND: return windowSize*1000;
            default: return -1;
        }
    }

    private String prepareHashKey(LimitRateVo limitRateVo) {
        return String.join(":",
                           String.valueOf(limitRateVo.getWindowSize()),
                           limitRateVo.getWindowTimeUnit().name(),
                           limitRateVo.getKey());
    }



}
