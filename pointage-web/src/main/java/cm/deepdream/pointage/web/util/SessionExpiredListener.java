package cm.deepdream.pointage.web.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cm.deepdream.pointage.model.Utilisateur;

@Component
public class SessionExpiredListener implements ApplicationListener<SessionDestroyedEvent>{
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        for (SecurityContext securityContext : event.getSecurityContexts()) {
            Authentication authentication = securityContext.getAuthentication(); 
            String username = authentication.getPrincipal().toString() ;
            // do custom event handling
            rest.put("http://pointage-zuul/pointage-server/ws/session/terminer-session/{login}", Utilisateur.class, username) ;
        }
    }
}
