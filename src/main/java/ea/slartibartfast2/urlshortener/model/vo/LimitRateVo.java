package ea.slartibartfast2.urlshortener.model.vo;

import ea.slartibartfast2.urlshortener.model.WindowTimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LimitRateVo {

    private String key;
    private long limit;
    private int windowSize;
    private WindowTimeUnit windowTimeUnit;
}
