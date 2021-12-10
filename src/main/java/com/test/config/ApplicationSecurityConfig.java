package com.test.config;

import com.test.auth.ApplicationUserService;
import com.test.jwt.JwtTokenVerifier;
import com.test.jwt.JwtUsernameAndPassowrdAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
//This below annotation is related to "PreAuthorize" annotation in rest classes
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                //If you are only creating a service that is used by non-browser clients,
                //you will likely want to disable CSRF protection
                //For using login page that redirect to index page after login csrf must be disabled
                .csrf().disable()
                //Below line is for active Stateless policy in JWT
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Below line is for JWT
                .and().addFilter(new JwtUsernameAndPassowrdAuthenticationFilter(authenticationManager()))
                //Below line is for verify token after login
                .addFilterAfter(new JwtTokenVerifier(),JwtUsernameAndPassowrdAuthenticationFilter.class)
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
                .authenticated();

        /* Below codes is for non-JWT Authentication *//*
                .and()
                //httpBasic used for login without Spring Security login form
//                .httpBasic();
                //formLogin use for default Spring Security login form
                .formLogin()
                .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses",true)
                    //Default parameter is password
                    .passwordParameter("password")
                    //Default parameter is username
                    .usernameParameter("username")
                .and()
                //Remember me checkbox . default is 2 week
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("sampleKey")
                    //Default parameter is remember-me
                    .rememberMeParameter("remember-me")
                .and()
                .logout().logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                    .clearAuthentication(true).invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");
              *//* Upper codes is for non-JWT Authentication */

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(applicationUserService);

        return daoAuthenticationProvider;
    }

    // Below method is for define directly users
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService(){
//        UserDetails mostafa = User.builder()
//                .username("mostafa")
//                .password(passwordEncoder.encode("123"))
////                .roles(ApplicationUserRole.ADMIN.name())
//                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails karimi = User.builder()
//                .username("karimi")
//                .password(passwordEncoder.encode("123456"))
////                .roles(ApplicationUserRole.USER.name())
//                .authorities(ApplicationUserRole.USER.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(mostafa,karimi);
//    }
}
