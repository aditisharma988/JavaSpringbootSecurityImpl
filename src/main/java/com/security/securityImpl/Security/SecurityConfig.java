package com.security.securityImpl.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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


        @Bean
        public UserDetailsService userDetailsService(){


        //first user
            UserDetails user1 = User
                    .withDefaultPasswordEncoder()
                    .username("aditii")
                    .password("Adi@123")
                    .roles("USER")
                    .build();

            //second user
            UserDetails user2 = User
                    .withDefaultPasswordEncoder()
                    .username("mahi")
                    .password("Mahi@123")
                    .roles("USER")
                    .build();




        return  new InMemoryUserDetailsManager(user1 , user2);
        }

}