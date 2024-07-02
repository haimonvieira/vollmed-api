package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//Aqui são inseridas as regras de negócios da consulta
@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    //O Spring procura todas as classes que implementam a interface e cria uma lista com
    //essas classes
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente())){

            throw new ValidacaoException("Id do paciente informado não existe.");

        }

        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){

            throw new ValidacaoException("Id do médico informado não existe.");

        }

        //Validando os dados conforme as regras de negócio
        //Aplicando SOD do SOLID
        //Single Responsability Principle
        //Open Close Principle
        //Dependency Inversion Principle
        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);

        if(medico == null){
            throw new ValidacaoException("Sem médico disponível nesta data.");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);

    }

    public void cancelar(DadosCancelamentoConsulta dados){

        if(!consultaRepository.existsById(dados.idConsulta())){

            throw new ValidacaoException("Id da consulta informada não existe.");

        }

        validadoresCancelamento.forEach(vc -> vc.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());

    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {

        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória " +
                    "quando médico não é escolhido");
        }

        return medicoRepository
                .escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());


    }

}
