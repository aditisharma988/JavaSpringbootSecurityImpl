package com.security.securityImpl.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity

                .csrf(customizer -> customizer.disable())

//                .csrf(AbstractHttpConfigurer::disable)



                .authorizeHttpRequests(request -> request
//                .requestMatchers("/register", "/login", "/**","/api/**"  ,
                                .requestMatchers("/api/register", "/api/login" ,"/api/regenerate-otp" ,"/api/verify-account" ,"/api/forgot-password","/api/reset-password","/api/registerWithPhoneNumber","/api/loginWithPhoneNumber","/api/logout" ,"/imageOrFile/save" ,"/imageOrFile/saveToFileSystem" ,"/imageOrFile/downloadToFiles/{fileName}","/imageOrFile/getAllFiles")

                        .permitAll()
                        .anyRequest().authenticated())

                //for form login enabling and gives response in postman as form
                //httpSecurity.formLogin(Customizer.withDefaults());

                //for postman correct reposnse when doing login/logout
                .httpBasic(Customizer.withDefaults())

                //to make application stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

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


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();


    }

}