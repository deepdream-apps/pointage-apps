package cm.deepdream.pointage.server.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger ;
@Component
@Order(1)
public class AbsenceRunner implements CommandLineRunner{
	private final Logger logger = Logger.getLogger(AbsenceRunner.class.getName()) ;
	@Autowired
	private Environment env ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("Starting ...AbsenceRunner  : ") ;
		String time = env.getProperty("app.absence.process.execution_time") ;
		long frequency = Long.parseLong(env.getProperty("app.absence.process.execution_frequency")) ;
		LocalTime executionTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")) ;
		
		long delay = (executionTime.getMinute()*60 + executionTime.getSecond()) - (LocalTime.now().getMinute()*60 + LocalTime.now().getSecond()) ;
       
		Runnable task = () -> {
            logger.info("Running...task1 -  : "+LocalDateTime.now()) ;
            rest.postForEntity("http://pointage-zuul/pointage-server/ws/absence/enregistrement-absences", LocalDate.now(), LocalDate.class);	
        };

        // init Delay = delay, repeat the task every frequency seconds
        ScheduledFuture<?> scheduledFuture = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(task, delay, frequency, TimeUnit.SECONDS);

	}
}
