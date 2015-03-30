package wallet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Spring MongoDB configuration file
 * 
 */
@Configuration
public class SpringMongoConfig{

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		
		
		MongoClient mongoClient = new MongoClient("ds049160.mongolab.com:49160");
		DB db = mongoClient.getDB("cmpe273");
		boolean auth = db.authenticate("admin", "admin".toCharArray());
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient,"cmpe273");
		return mongoTemplate;
		
	}
		
}