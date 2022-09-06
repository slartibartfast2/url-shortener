package ea.slartibartfast2.urlshortener.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;

@AllArgsConstructor(staticName = "of")
public class BasicCountingSemaphore implements SessionCallback<List<Object>> {

    private String id;
    private long windowSliceStart;
    private String semaphoreName;
    private long currentMs;

    @Override
    public <K, V> List<Object> execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
        var operations = (RedisTemplate<Object, Object>) redisOperations;
        var zSetOperations = operations.boundZSetOps(semaphoreName);

        operations.multi();
        zSetOperations.removeRangeByScore(-1, windowSliceStart);
        zSetOperations.add(id, currentMs);
        zSetOperations.rank(id);
        return operations.exec();
    }
}
