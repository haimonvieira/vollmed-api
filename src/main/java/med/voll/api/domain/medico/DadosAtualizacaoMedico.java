package med.voll.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.endereco.DadosEndereco;

//novo DTO somente para os campos obrigatórios que devem ser inseridos,
//caso tivesse somente algum dado obrigatório no DadosEndereco, teria que criar um
//DTO so para esses campos
public record DadosAtualizacaoMedico(

        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco

) {
}
