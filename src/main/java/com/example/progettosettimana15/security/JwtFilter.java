package com.example.progettosettimana15.security;

import com.example.progettosettimana15.entities.Utente;
import com.example.progettosettimana15.exceptions.UnauthorizedException;
import com.example.progettosettimana15.repository.UtenteRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTtools jwTtools;
    private final UtenteRepository utenteRepository;
    private final HandlerExceptionResolver exceptionResolver;

    public JwtFilter(JWTtools jwTtools, UtenteRepository utenteRepository, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver){
        this.jwTtools = jwTtools;
        this.utenteRepository = utenteRepository;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try {
            String autHeader = request.getHeader("Authorization");
            if (autHeader == null || !autHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException("Header Authorization mancante o non nel formato richiesto");

            }
            String accesToken = autHeader.substring(7); // visto l'altro giorno toglie i 7 carratteri quindi elimina Bearer
            Claims claims = jwTtools.verifyToken(accesToken);

            UUID utenteId = UUID.fromString(claims.getSubject());
            Utente utente = utenteRepository.findById(utenteId)
                    .orElseThrow(()-> new UnauthorizedException("L'utente assiociato al token non esiste"));

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("Role_ " + utente.getRuolo().name()));
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(utente, null, authorities));

            filterChain.doFilter(request, response);
        }catch (Exception e){
            exceptionResolver.resolveException(request,response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        boolean isLogin = path.equals("/api/auth/login");
        boolean isRegistrazione = path.equals("/api/utenti") && request.getMethod().equals("POST");
        return isLogin || isRegistrazione;
    }


}
