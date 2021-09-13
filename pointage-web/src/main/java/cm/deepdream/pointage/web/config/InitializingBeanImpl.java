package cm.deepdream.pointage.web.config;
import java.util.logging.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class InitializingBeanImpl implements InitializingBean{
	private Logger logger = Logger.getLogger(InitializingBeanImpl.class.getName()) ;
	
	public void afterPropertiesSet() throws Exception {
		
	}

}
