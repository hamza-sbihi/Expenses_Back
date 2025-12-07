package com.example.expensesb.Config;

import com.example.expensesb.Service.JwtService;
import com.example.expensesb.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    JwtService jwtService;
    MyUserDetailsService myUserDetailsService;

    public JWTFilter(JwtService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader == null ? null : authorizationHeader.replace("Bearer ","");
        String username = token == null ? null :jwtService.extractUsername(token);

        if(token!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails myUserDetails = myUserDetailsService.loadUserByUsername(username);

            if(jwtService.validateToken(token,myUserDetails)){
                System.out.println("JWT Filter triggered");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
