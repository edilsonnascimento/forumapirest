package br.com.nova.forumapirest.config.validacao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    private TokenLocalService tokenService;

    public AutenticacaoViaTokenFilter(TokenLocalService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recupearToken(httpServletRequest);

        boolean valido = tokenService.isTokenValido(token);

        System.out.println(valido);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String recupearToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }

        return token.substring(7, token.length());
    }
}
