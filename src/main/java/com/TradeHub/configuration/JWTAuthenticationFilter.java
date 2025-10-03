package com.TradeHub.configuration;

import com.TradeHub.repository.TokenRepository;
import com.TradeHub.service.JWTService;
import com.TradeHub.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserService userService;


    private final TokenRepository tokenRepository;

    /* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Checks if the Authorization header contains a valid JWT token.
     * If the token is valid and the user is not already authenticated,
     * it will authenticate the user and set the authentication in the SecurityContext.
     * If the token is invalid or the user is already authenticated, it will simply call the next filter in the filter chain.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if there is an error with the filter

    /* <<<<<<<<<<  f94748b9-f550-40a4-b134-5855aba714a1  >>>>>>>>>>> */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(!StringUtils.hasText(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail=jwtService.extractUserName(jwt);

        if(StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(token -> !token.isExpired() && !token.isRevoked())
                    .orElse(false);
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);

                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request,response);

    }
}



