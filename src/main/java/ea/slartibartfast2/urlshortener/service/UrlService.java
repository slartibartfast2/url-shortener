package ea.slartibartfast2.urlshortener.service;

import ea.slartibartfast2.urlshortener.exception.UrlNotFoundException;
import ea.slartibartfast2.urlshortener.model.dto.LongUrlRequest;
import ea.slartibartfast2.urlshortener.model.entity.Url;
import ea.slartibartfast2.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlConverter urlConverter;

    public String convertToShortUrl(LongUrlRequest request) {
        var url = new Url();
        url.setLongUrl(request.getLongUrl());
        url.setExpiresDate(request.getExpiresDate());
        url.setCreatedDate(new Date());
        var entity = urlRepository.save(url);

        return urlConverter.encode(entity.getId());
    }

    @Cacheable(value = "urls", key = "#shortUrl", sync = true)
    public String getOriginalUrl(String shortUrl) {
        var id = urlConverter.decodeUrlId(shortUrl);
        var url = urlRepository.findById(id)
                               .orElseThrow(() -> new UrlNotFoundException("There is no url with " + shortUrl));

        if (url.getExpiresDate().toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDateTime().isBefore(LocalDateTime.now())) {
            urlRepository.delete(url);
            throw new UrlNotFoundException("Link expired!");
        }

        return url.getLongUrl();
    }
}
