package cm.deepdream.pointage.web.util;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cm.deepdream.pointage.exceptions.PointageException;
import cm.deepdream.pointage.model.Utilisateur;

@Service
public class AuthentificationService implements UserDetailsService{	
	private Logger logger = Logger.getLogger(AuthentificationService.class.getName()) ;
	@LoadBalanced 
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private LoginAttemptService LoginAttemptService ;
	@Autowired
    private HttpServletRequest request ;
 

    
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    	try {
    		String ip = getClientIP();
    		if (LoginAttemptService.isBlocked(ip)) {
    			throw new RuntimeException("blocked") ;
    		}
    		
    		Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
    		
    		if(utilisateur == null) {
    			throw new PointageException ("Echec ! Utilisateur invalide") ;
    		}
    		
    		UserDetails details = User.withUsername(utilisateur.getLogin()).password(utilisateur.getMotDePasse()).authorities(utilisateur.getProfil().getLibelle()).build();
    		return details ;
    	}catch(PointageException pex) {
    		logger.log(Level.SEVERE, pex.getMessage(), pex); ;
    		return null ;
    	}catch(Exception ex) {
    		logger.log(Level.SEVERE, ex.getMessage(), ex); ;
    		throw ex ;
    	}
    }
    
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
