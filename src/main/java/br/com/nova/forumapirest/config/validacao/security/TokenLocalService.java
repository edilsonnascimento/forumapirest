package br.com.nova.forumapirest.config.validacao.security;

import br.com.nova.forumapirest.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenLocalService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {

        Usuario  logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API FORUM DA ALURA")        //Quem está gerando o token
                .setSubject(logado.getId().toString())  //Dono do token
                .setIssuedAt(hoje)                      //Data de geração do Token
                .setExpiration(dataExpiracao)           //Data de expiração
                .signWith(SignatureAlgorithm.HS256, secret) //Criptografar senha
                .compact();                             //Transforma tudo em uma string.
    }

    public boolean isTokenValido(String token) {

        try {
            Jwts.parser()                             //Faz discriptografa o token e valida
                    .setSigningKey(this.secret)       //Chave para descriptografar.
                    .parseClaimsJws(token);           //Devolve um objeto com o token válido senão retorna uma Exception
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
