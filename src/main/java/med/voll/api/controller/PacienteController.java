package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

//Classe de mapeamento da classe Paciente
@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dadosPaciente,
                                    UriComponentsBuilder uriBuilder){

        Paciente paciente = new Paciente(dadosPaciente);
        repository.save(paciente);

        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(Pageable paginacao){

        Page<DadosListagemPaciente> page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemPaciente::new);

        return ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dadosAtualizacaoPaciente){

        var paciente = repository.getReferenceById(dadosAtualizacaoPaciente.id());
        paciente.atualizarPaciente(dadosAtualizacaoPaciente);

        //Mostra no body os dados
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));

    }

    @GetMapping("/{id}")
    public ResponseEntity exibir(@PathVariable Long id){

        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity desativar(@PathVariable Long id){

        var paciente = repository.getReferenceById(id);
        paciente.desativar();

        return ResponseEntity.noContent().build();

    }

}
