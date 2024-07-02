package med.voll.api.domain.consulta;

public enum MotivoCancelamento {

    PACIENTE_DESISTIU("paciente desistiu"),
    MEDICO_CANCELOU("medico cancelou"),
    OUTROS("outros");

    MotivoCancelamento(String motivo){

    }

}
