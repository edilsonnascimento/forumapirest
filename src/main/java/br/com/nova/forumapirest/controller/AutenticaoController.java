package br.com.nova.forumapirest.controller;

import br.com.nova.forumapirest.config.validacao.security.TokenLocalService;
import br.com.nova.forumapirest.controller.dto.TokenDto;
import br.com.nova.forumapirest.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenLocalService tokenService;


    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            //faz a autenticação
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            //Gera o token
            String token = tokenService.gerarToken(authentication);
            //Retorna token para o cliente.
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
