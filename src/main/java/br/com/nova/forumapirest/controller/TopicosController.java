package br.com.nova.forumapirest.controller;

import br.com.nova.forumapirest.controller.dto.TopicoDTO;
import br.com.nova.forumapirest.modelo.Curso;
import br.com.nova.forumapirest.modelo.Topico;
import br.com.nova.forumapirest.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @RequestMapping("/topicos")
    public List<TopicoDTO> lista(String nomeCurso){
        if(nomeCurso == null) {
            return TopicoDTO.converter(topicoRepository.findAll());
        }else{
            return TopicoDTO.converter(topicoRepository.findByCursoNome(nomeCurso));
        }

    }
}
