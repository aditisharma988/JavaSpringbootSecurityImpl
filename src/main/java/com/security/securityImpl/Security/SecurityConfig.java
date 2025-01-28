package com.security.securityImpl.Security;

import com.security.securityImpl.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity

                .csrf(customizer -> customizer.disable())
//                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(request -> request.anyRequest().authenticated())

                //for form login enabling and gives response in postman as form
//        httpSecurity.formLogin(Customizer.withDefaults());

                //for postman correct reposnse when doing login/logout
                .httpBasic(Customizer.withDefaults())

                //to make application stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }


    //to customize authetication using database
    @Bean
    AuthenticationProvider authenticationProvider(){

        //for db
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;

    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        //first user
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("aditii")
//                .password("Adi@123")
//                .roles("USER")
//                .build();
//
//        //second user
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("mahi")
//                .password("Mahi@123")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

}