package com.example.progettoFinale.security;


import com.example.progettoFinale.entities.Dipendente;
import com.example.progettoFinale.exceptions.UnauthorizedEx;
import com.example.progettoFinale.services.DipendentiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private DipendentiService dipendentiService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedEx("Inserisci correttamente il token!");
        String accessToken = authHeader.substring(7);
        System.out.println("Token: " + accessToken);
        jwtTools.verifyToken(accessToken);
        String currenteIdDipendenti = jwtTools.extractIDfromToken(accessToken);
        Dipendente currentUtente = this.dipendentiService.findByID(UUID.fromString(currenteIdDipendenti));
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
