package com.security.securityImpl.security.config;

import com.security.securityImpl.security.service.JWTService;
import com.security.securityImpl.security.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



    @Component
    public class JwtFilter extends OncePerRequestFilter {

        @Autowired
        JWTService jwtService;

        @Autowired
        MyUserDetailsService myUserDetailsService;

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final Integer userId; // ✅ Use Integer for userId

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userId = jwtService.extractUserId(jwt); // ✅ Extract userId from token

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.myUserDetailsService.loadUserByUserId(userId); // ✅ Load by ID
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//
//        }
//
//        jwt = authHeader.substring(7);
//
////        userEmail = jwtService.extractUsername(jwt);
//userId = jwtService.extractUserId(jwt);
////        userEmail = jwtService.extractUsername(jwt);
//
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(userEmail);
//            if (jwtService.validateToken(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//
//    }
//
//}


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//
//        String authHeader = request.getHeader("Authorization");
//        String accessToken = null;
//        String username = null;
//
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            accessToken=authHeader.substring(7);
//            username= jwtService.extractUsername(accessToken);
//        }
//
//        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
//
//
////                UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
////                if(jwtService.isTokenValid(jwt, userDetails)){
////                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
////                            userDetails,
////                            null,
////                            userDetails.getAuthorities()
////                    );
////                    authenticationToken.setDetails(
////                            new WebAuthenticationDetailsSource().buildDetails(request)
////                    );
////
////                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
////                }
////            }
////            filterChain.doFilter(request, response);
////
////        }
//            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
//
//
////                    applicationContext.getBean(MyUserDetailsService.class).loadUser;
//
//            if(jwtService.validateToken(accessToken,userDetails)){
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
//                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        filterChain.doFilter(request,response);
//
//
//    }
