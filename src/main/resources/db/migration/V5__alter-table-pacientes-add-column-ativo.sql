alter table pacientes add column ativo boolean;
update pacientes set ativo = true;
ALTER TABLE pacientes ALTER COLUMN ativo SET DEFAULT true;
alter table pacientes alter column ativo set not null;