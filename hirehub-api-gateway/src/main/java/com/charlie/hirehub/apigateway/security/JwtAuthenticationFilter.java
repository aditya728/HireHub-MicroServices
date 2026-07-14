//package com.charlie.hirehub.apigateway.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//
//    private static final Logger logger =
//            LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//        logger.info("JwtAuthenticationFilter executed");
//
//        // 1. Check if the request has a JWT Token
//        String authHeader = request.getHeader("Authorization");
//
//        if(authHeader==null || !authHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 2. Extract the JWT
//        String jwt = authHeader.substring(7);
//
//        // 3. Extract the username, role & userId using the JWT
//        String email = jwtService.extractUsername(jwt);
//        Long userId = jwtService.extractUserId(jwt);
//        String role = jwtService.extractRole(jwt);
//
//        // 4. Load username and validate the JWT
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            if(jwtService.isTokenValid(jwt, userDetails)){
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//
//                SecurityContextHolder.getContext()
//                        .setAuthentication(authenticationToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
