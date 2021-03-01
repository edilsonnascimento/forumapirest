package br.com.nova.forumapirest.controller;

import br.com.nova.forumapirest.controller.dto.TopicoDTO;
import br.com.nova.forumapirest.controller.form.TopicoForm;
import br.com.nova.forumapirest.modelo.Topico;
import br.com.nova.forumapirest.repository.CursoRepository;
import br.com.nova.forumapirest.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GeneratedValue
    public List<TopicoDTO> lista(String nomeCurso){
        if(nomeCurso == null) {
            return TopicoDTO.converter(topicoRepository.findAll());
        }else{
            return TopicoDTO.converter(topicoRepository.findByCursoNome(nomeCurso));
        }
    }

    @PostMapping
    public void cadastrar(@RequestBody TopicoForm topicoForm){
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);
    }
}
