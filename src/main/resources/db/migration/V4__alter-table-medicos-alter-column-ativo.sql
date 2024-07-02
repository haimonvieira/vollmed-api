ALTER TABLE medicos ADD COLUMN ativo_temp BOOLEAN;
UPDATE medicos SET ativo_temp = (ativo = 1);
ALTER TABLE medicos DROP COLUMN ativo;
ALTER TABLE medicos RENAME COLUMN ativo_temp TO ativo;
ALTER TABLE medicos ALTER COLUMN ativo SET NOT NULL;
ALTER TABLE medicos ALTER COLUMN ativo SET DEFAULT TRUE;
