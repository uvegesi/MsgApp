package edu.progmatic.messageapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").
                password("password").roles("USER").build());
        manager.createUser(User.withUsername("admin").
                password("password").roles("ADMIN").build());
        return manager;
    }

     */

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http.addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll()
                //.loginProcessingUrl("/login")
                .defaultSuccessUrl("/messages", true)
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .and()
                .authorizeRequests()
                .antMatchers("/home", "/registration").permitAll()
                .antMatchers("/messages").permitAll()
                .antMatchers("/statistics").hasRole("ADMIN")
                .antMatchers("/messages/delete").hasRole("ADMIN")
                    //ez a felső sor a crfs elleni vedelem, hogy csak admin tudja csinalni
                .anyRequest().authenticated();

    }
}
