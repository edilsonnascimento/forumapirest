package br.com.nova.forumapirest.controller;

import br.com.nova.forumapirest.controller.dto.TopicoDTO;
import br.com.nova.forumapirest.controller.dto.TopicoDetalhadoDTO;
import br.com.nova.forumapirest.controller.form.TopicoAtualizacaoForm;
import br.com.nova.forumapirest.controller.form.TopicoForm;
import br.com.nova.forumapirest.modelo.Topico;
import br.com.nova.forumapirest.repository.CursoRepository;
import br.com.nova.forumapirest.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso){
        if(nomeCurso == null) {
            return TopicoDTO.converter(topicoRepository.findAll());
        }else{
            return TopicoDTO.converter(topicoRepository.findByCursoNome(nomeCurso));
        }
    }

    @PostMapping
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public TopicoDetalhadoDTO detalhar(@PathVariable Long id){
        Topico topico = topicoRepository.getOne(id);
        return new TopicoDetalhadoDTO(topico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoAtualizacaoForm form){

        Topico topico = form.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDTO(topico));

    }
}
