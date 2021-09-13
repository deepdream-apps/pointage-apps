package cm.deepdream.pointage.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
@EntityScan("cm.deepdream.pointage.model") 
@EnableJpaRepositories("cm.deepdream.pointage.server.dao")
@EnableEurekaClient
public class PointageServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointageServerApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate() ;
		return restTemplate ;
	}

}
