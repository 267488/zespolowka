/*
 * Copyright 2014 Michael J. Simons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pz.twojaszkola.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import pz.twojaszkola.user.CustomFailureHandler;
import pz.twojaszkola.user.CustomSuccessHandler;


/**
 * @author Michael J. Simons, 2014-02-19
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({"default", "prod"})
public class SecurityConfig {

    
    /**
     * When using Spring Boot Dev Tools,
     * {@code SecurityProperties.BASIC_AUTH_ORDER - 20} will already be used for
     * the h2 web console if that hasn't been explicitly disabled.
     */
    
    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 20)
    @ConditionalOnBean(SecurityConfig.class)
    protected static class ApplicationWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        
        @Autowired
        @Qualifier("customUserDetailsService")
        private UserDetailsService userDetailsService;
        
        @Autowired
        private CustomSuccessHandler customSuccessHandler;
        
        @Autowired
        private CustomFailureHandler customFailureHandler;
        
	@Override
	protected void configure(final HttpSecurity http) throws Exception {	  
            http
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/","sign-up.html").permitAll()
                    .antMatchers("/profil-szkola.html","/ustawienia-szkola.html","/szkoly-szkola.html").hasAuthority("SZKOLA")
                    .antMatchers("/admin.html","/admin.html/*").hasAuthority("ADMIN")
                    .antMatchers("/profil-uczen.html","/ustawienia-uczen.html","/szkoly-uczen.html","/index.html").hasAuthority("UCZEN")
                    .and()
                    .formLogin()
                    .loginPage("/").successHandler(customSuccessHandler).failureHandler(customFailureHandler)
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                    .csrf()
                    .disable()
                    
                    
                    ;
            
	}
        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth    
                    .userDetailsService(userDetailsService)
                    ;
                
                
    }
                
}                  
    
}

