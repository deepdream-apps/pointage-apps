package cm.deepdream.pointage.web.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import cm.deepdream.pointage.web.util.AuthentificationService;

@Configuration
@EnableWebSecurity
public class PointageWebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private AuthentificationService authentificationService ;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authentificationService) ;
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
          	.authorizeRequests()
          		.antMatchers("/**").permitAll()
          		.antMatchers("/login").permitAll()
          		.antMatchers("/disconnected").permitAll()
          		.antMatchers("/css/**").permitAll()
          		.antMatchers("/images/**").permitAll()
          		.antMatchers("/frontend/**").permitAll()
          		.antMatchers("/js/**").permitAll()
          		.antMatchers("/less/**").permitAll()
          		.antMatchers("/vendors/**").permitAll()
          		.antMatchers("/error/**").permitAll()
          		.antMatchers("/ws/**").permitAll()
          		.antMatchers("/forgot-password**").permitAll()
          		.anyRequest().authenticated()
          	.and()
          		.sessionManagement()
          		.invalidSessionUrl("/login")
          	.and()
          		.formLogin()
          			.loginPage("/login")
          			.loginProcessingUrl("/perform_login")
          			.defaultSuccessUrl("/dashboard")
          			.failureUrl("/login-echec") 
          	.and()
          		.logout()
          			.logoutUrl("/perform_logout")
          			.logoutSuccessUrl("/disconnected")
          			.invalidateHttpSession(true)
          			.deleteCookies("JSESSIONID") ;

    }
    
}
