package com.test.config;

import com.sun.research.ws.wadl.HTTPMethods;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
//This below annotation is related to "PreAuthorize" annotation in rest classes
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig( PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                //If you are only creating a service that is used by non-browser clients,
                //you will likely want to disable CSRF protection
                //For using login page that redirect to index page after login csrf must be disabled
                .csrf().disable()
                //Our recommendation is to use csrf protection for any request that could be processed
                //by a browser by normal user.
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.ADMIN.name())

//                 When we are using "PreAuthorize" annotation, we don't need to ".antMatcher"
//                .antMatchers(HttpMethod.DELETE,"/management/api/**")
//                .hasAnyAuthority(ApplicationUserPermission.STUDENT_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST,"/management/api/**")
//                .hasAnyAuthority(ApplicationUserPermission.STUDENT_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/management/api/**")
//                .hasAnyAuthority(ApplicationUserPermission.STUDENT_WRITE.getPermission())
//                .antMatchers("/management/api/**")
//                .hasAnyRole(ApplicationUserRole.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                //httpBasic used for login without Spring Security login form
//                .httpBasic();
                //formLogin use for default Spring Security login form
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses",true)
                .and()
                //Remember me checkbox . default is 2 week
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("sampleKey")
                .and()
                .logout().logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                    .clearAuthentication(true).invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails mostafa = User.builder()
                .username("mostafa")
                .password(passwordEncoder.encode("123"))
//                .roles(ApplicationUserRole.ADMIN.name())
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails karimi = User.builder()
                .username("karimi")
                .password(passwordEncoder.encode("123456"))
//                .roles(ApplicationUserRole.USER.name())
                .authorities(ApplicationUserRole.USER.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(mostafa,karimi);
    }
}
