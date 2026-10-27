-- Dados ilustrativos para desenvolvimento local da aula05.
-- Pode ser executado novamente sem duplicar estes registros.
USE projetoimobiliaria;

INSERT INTO bairros (nome, cidade, estado, cep_inicial, cep_final, created_at, updated_at)
SELECT 'Centro', 'Santa Maria', 'RS', '97010000', '97019999', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bairros WHERE nome = 'Centro' AND cidade = 'Santa Maria');

INSERT INTO bairros (nome, cidade, estado, cep_inicial, cep_final, created_at, updated_at)
SELECT 'Camobi', 'Santa Maria', 'RS', '97105000', '97119999', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bairros WHERE nome = 'Camobi' AND cidade = 'Santa Maria');

INSERT INTO tipos_imoveis (nome, descricao, created_at, updated_at)
SELECT 'Casa', 'Imovel residencial do tipo casa', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tipos_imoveis WHERE nome = 'Casa');

INSERT INTO tipos_imoveis (nome, descricao, created_at, updated_at)
SELECT 'Apartamento', 'Unidade residencial em edificio', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tipos_imoveis WHERE nome = 'Apartamento');

INSERT INTO imoveis (titulo, descricao, preco_venda, finalidade, status, dormitorios, banheiros,
                     garagem, area_total, area_construida, endereco, numero, cep, destaque,
                     bairro_id, tipo_imovel_id)
SELECT 'Casa de exemplo no Centro', 'Registro demonstrativo para testar a API', 450000.00,
       'Residencial', 'Disponivel', 3, 2, 1, 180.00, 120.00, 'Rua Exemplo', '100', '97010000', 1,
       b.id, t.id
FROM bairros b CROSS JOIN tipos_imoveis t
WHERE b.nome = 'Centro' AND b.cidade = 'Santa Maria' AND t.nome = 'Casa'
  AND NOT EXISTS (SELECT 1 FROM imoveis WHERE titulo = 'Casa de exemplo no Centro');

INSERT INTO imoveis (titulo, descricao, preco_aluguel, finalidade, status, dormitorios, banheiros,
                     garagem, area_total, area_construida, endereco, numero, cep, destaque,
                     bairro_id, tipo_imovel_id)
SELECT 'Apartamento de exemplo em Camobi', 'Registro demonstrativo para testar a API', 2200.00,
       'Residencial', 'Disponivel', 2, 1, 1, 75.00, 68.00, 'Avenida Exemplo', '200', '97105000', 0,
       b.id, t.id
FROM bairros b CROSS JOIN tipos_imoveis t
WHERE b.nome = 'Camobi' AND b.cidade = 'Santa Maria' AND t.nome = 'Apartamento'
  AND NOT EXISTS (SELECT 1 FROM imoveis WHERE titulo = 'Apartamento de exemplo em Camobi');
