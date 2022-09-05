package ea.slartibartfast2.urlshortener.service;

import ea.slartibartfast2.urlshortener.model.dto.UrlRequestCountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestCounterService {

    private final RedisTemplate<String, Object> redisObjectTemplate;

    public void incrementRequestCountByWindow(String key, int windowInMinute) {
        long currentMs = System.currentTimeMillis();
        int windowInMillis = windowInMinute*60*1000;
        redisObjectTemplate.executePipelined((RedisCallback<Object>) connection -> {
            long windowSliceStart = (currentMs/windowInMillis) * windowInMillis;
            String hashKey = windowInMinute + ":" + key;
            connection.zAdd("known:".getBytes(), 0, hashKey.getBytes());
            connection.hIncrBy(("count:" + hashKey).getBytes(), ("" + windowSliceStart).getBytes(), 1);
            log.info("{} window slice request count incremented by 1", Date.from(Instant.ofEpochMilli(windowSliceStart)));
            return null;
        });
    }

    public UrlRequestCountResponse retrieveRequestCount(int windowInMinute) {
        long currentMs = System.currentTimeMillis();
        int windowInMillis = windowInMinute*60*1000;
        long windowSliceStart = (currentMs/windowInMillis) * windowInMillis;
        log.info("Request count will be fetched for window slice with starting {}", Date.from(Instant.ofEpochMilli(windowSliceStart)));
        BoundHashOperations hashOps = redisObjectTemplate.boundHashOps("count:5:url-shortener:request-count");
        Object requestCount = Optional.ofNullable(hashOps.get("" + windowSliceStart)).orElse(0);
        return UrlRequestCountResponse.builder().count((int) requestCount).window("" + windowInMinute).build();
    }
}
