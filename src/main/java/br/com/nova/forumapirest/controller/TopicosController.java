package br.com.nova.forumapirest.controller;

import br.com.nova.forumapirest.controller.dto.TopicoDTO;
import br.com.nova.forumapirest.controller.dto.TopicoDetalhadoDTO;
import br.com.nova.forumapirest.controller.form.TopicoAtualizacaoForm;
import br.com.nova.forumapirest.controller.form.TopicoForm;
import br.com.nova.forumapirest.modelo.Topico;
import br.com.nova.forumapirest.repository.CursoRepository;
import br.com.nova.forumapirest.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private Optional<Topico> topicoOptional;
    private Topico topico;

    @GetMapping
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, @RequestParam int pagina, @RequestParam int quantidade){

        Pageable paginacao = PageRequest.of(pagina, quantidade);

        if(nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        }else{
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){
        topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetalhadoDTO> detalhar(@PathVariable Long id){
        topicoOptional = topicoRepository.findById(id);

        return topicoOptional.isPresent() ?  ResponseEntity.ok(new TopicoDetalhadoDTO(topicoOptional.get()))
                : ResponseEntity.notFound().build();

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoAtualizacaoForm form){

        topicoOptional = topicoRepository.findById(id);

        if(topicoOptional.isPresent()){
            topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topico));
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id){

        topicoOptional = topicoRepository.findById(id);
        if(topicoOptional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
