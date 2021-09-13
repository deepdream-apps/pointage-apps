package cm.deepdream.pointage.server.util;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber ;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class WhatsAppSender {
	private Logger logger = Logger.getLogger(WhatsAppSender.class.getName()) ;
	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer ;
	@Autowired
	private Environment env ;


	public void sendMessage(String to, String templateFile, String attachment, Map<String, Object> templateModel)
			 throws IOException, TemplateException, MessagingException{
		logger.info("Sending WhatsApp message to "+to) ;
		
		Template freemarkerTemplate = freemarkerConfigurer.createConfiguration().getTemplate(templateFile) ;
	    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel) ;
	    
	    Twilio.init(env.getProperty("app.twilio.account.sid"), env.getProperty("app.twilio.auth.token")) ;
	    List<URI> attachments = new ArrayList<URI>() ;
	    attachments.add(URI.create(attachment)) ;
	    Message message1 = Message.creator(new PhoneNumber( "whatsapp:"+to), new PhoneNumber("whatsapp:+14155238886"), htmlBody).create() ;
	    Message message2 = Message.creator(new PhoneNumber( "whatsapp:"+to), new PhoneNumber("whatsapp:+14155238886"), attachments).create() ;
	    logger.info("Messages sent  "+ message1.getSid() +"  "+ message2.getSid()) ;
	}
	
}
