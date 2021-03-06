package com.example.lesson17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
// both of these tells the compiler that the file is a configuration file and spring security is enables for the
// application
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests() // tells which requests should be authorized.
                .antMatchers("/")
                .access("hasAuthority('USER') or hasAuthority('ADMIN')")
                .antMatchers("/admin").access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                .and() // Adds additional authentication rules. Use this to combine rules.
                .formLogin().loginPage("/login").permitAll() //formLogin()- Indicates that the application should show a login form.
                // .formLogin().loginPage("/login").permitAll() - indicates that you are expecting a login form.
                .and().httpBasic(); // User can avoid a login prompt by putting his/her login details in the
        // request. Used for testing.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    // configure() overrides the default configure method, configures users who can access the application.
        // By default, Spring Boot will provide a new random password assigned to the user "user: when it
        // starts up, if you do not include this method.

        PasswordEncoder p = new BCryptPasswordEncoder();

        auth.inMemoryAuthentication().
                withUser("josmy").password(p.encode("password")).authorities("ADMIN").
                and().
                withUser("user").password(p.encode("pass")).authorities("USER").
                and().
                passwordEncoder(new BCryptPasswordEncoder());

//                password("begreat").roles("ADMIN").
//                and().
//                withUser("user").password("password").roles("USER");
    }

//    @SuppressWarnings("deprecation")
//    @Bean
//    public static NoOpPasswordEncoder passwordEncoder(){
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }
}
