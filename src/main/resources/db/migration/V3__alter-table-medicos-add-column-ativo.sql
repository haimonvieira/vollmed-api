alter table medicos add column ativo smallint;
update medicos set ativo = 1;
alter table medicos alter column ativo set not null;