package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

//Spring carrega automaticamente
public interface ValidadorAgendamentoDeConsulta {

    void validar(DadosAgendamentoConsulta dados);

}
