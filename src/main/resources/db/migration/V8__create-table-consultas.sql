CREATE TABLE consultas (
    id serial primary key,
    medico_id serial not null,
    paciente_id serial not null,
    data timestamp not null,
    constraint fk_consultas_medico_id foreign key (medico_id) references medicos (id),
    constraint fk_consultas_paciente_id foreign key (paciente_id) references pacientes (id)
);
