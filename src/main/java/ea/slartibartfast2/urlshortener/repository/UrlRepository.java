package ea.slartibartfast2.urlshortener.repository;

import ea.slartibartfast2.urlshortener.model.entity.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, Long> {

}