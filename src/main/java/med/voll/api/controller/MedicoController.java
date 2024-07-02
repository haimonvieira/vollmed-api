package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

//Classe de mapeamento da classe Medico
@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired//injecao de dependencias
    private MedicoRepository repository;

    @PostMapping//requisicao do tipo POST
    @Transactional//escrita no BD, inserir dados
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dadosMedico,
                                    UriComponentsBuilder uriBuilder){//Indica que é para buscar no corpo da requisicao
        var medico = new Medico(dadosMedico);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    @GetMapping
    //Irá retornar no maximo 10 elementos ordenados pelo nome
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){

        //Nao precisa ter stream e nem toList, pois o findAll devolve o Page com o metodo map nele
        //Devolve um Page com listas
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemMedico::new);//Precisa de um construtor no enum

        return ResponseEntity.ok(page);


    }

    @PutMapping
    @Transactional
    //JPA atualiza os dados sem necessidade de chamar o repository
    //ResponseEntity retorna os codigos https equivalentes
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dadosAtualizacaoMedico){

        var medico = repository.getReferenceById(dadosAtualizacaoMedico.id());
        medico.atualizarInformacoes(dadosAtualizacaoMedico);


        //Retorna os dados atualizados e completos do médico do novo DTO
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));

    }


//    //Exluindo do BD
//    @DeleteMapping("/{id}")
//    @Transactional
//    public void excluir(@PathVariable Long id){
//
//        repository.deleteById(id);
//
//    }

    //Desativando do BD
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity desativar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.desativar();//Este método apenas troca o estado do atributo 'ativo' para 'false'

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity exibir(@PathVariable Long id){

        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }


}
