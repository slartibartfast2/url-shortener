package ea.slartibartfast2.urlshortener.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ea.slartibartfast2.urlshortener.repository")
public class MongoDBConfig {

}
