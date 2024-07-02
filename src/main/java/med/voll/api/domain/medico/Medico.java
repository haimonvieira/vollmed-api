package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor//construtor padrao
@AllArgsConstructor
@EqualsAndHashCode(of = "id")//equals e hashCode do id
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;
    private boolean ativo;

    public Medico(DadosCadastroMedico dadosMedico) {
        this.ativo = true;
        this.nome = dadosMedico.nome();
        this.email = dadosMedico.email();
        this.telefone = dadosMedico.telefone();
        this.crm = dadosMedico.crm();
        this.especialidade = dadosMedico.especialidade();
        this.endereco = new Endereco(dadosMedico.endereco());
    }

    //Atualiza informacoes do usuário
    public void atualizarInformacoes(DadosAtualizacaoMedico dadosAtualizacaoMedico){

        if(dadosAtualizacaoMedico.nome() != null) {
            this.nome = dadosAtualizacaoMedico.nome();
        }
        if(dadosAtualizacaoMedico.telefone() != null) {
            this.telefone = dadosAtualizacaoMedico.telefone();
        }
        if(dadosAtualizacaoMedico.endereco() != null){
            this.endereco.atualizarEndereco(dadosAtualizacaoMedico.endereco());
        }
    }

    public void desativar() {
        this.ativo = false;
    }
}
