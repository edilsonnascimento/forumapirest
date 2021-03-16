package br.com.nova.forumapirest.config.validacao.security;

import br.com.nova.forumapirest.modelo.Usuario;
import br.com.nova.forumapirest.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    private TokenLocalService tokenService;

    private UsuarioRepository usuarioRepository;

    public AutenticacaoViaTokenFilter(TokenLocalService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recupearToken(httpServletRequest);

        //valido conforme chave mestre(secret)
        if(tokenService.isTokenValido(token)){
            autenticarCliente(token);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token); //recebe o id do usuário que está no token.
        Usuario usuario = usuarioRepository.findById(idUsuario).get(); // recebe do repositorio objeto inserido na contrução do objeto AutenticacaoViaTokenFilter.
        UsernamePasswordAuthenticationToken autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //tem as informações do usuário autenticado.
        SecurityContextHolder.getContext().setAuthentication(autentication); //usário está autenticado.
    }

    private String recupearToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }

        return token.substring(7, token.length());
    }
}
