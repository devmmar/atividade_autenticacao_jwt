-- Estrutura da aplicação da aula05 (MySQL/MariaDB).
CREATE DATABASE IF NOT EXISTS projetoimobiliaria
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE projetoimobiliaria;

CREATE TABLE IF NOT EXISTS bairros (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255),
  cidade VARCHAR(255),
  estado VARCHAR(255),
  cep_inicial VARCHAR(255),
  cep_final VARCHAR(255),
  created_at DATETIME(6),
  updated_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipos_imoveis (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255),
  descricao TEXT,
  created_at DATETIME(6),
  updated_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150),
  email VARCHAR(150) UNIQUE,
  senha VARCHAR(255),
  tipo ENUM('ADMIN', 'COMUM'),
  created_at DATETIME(6),
  updated_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS imoveis (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255),
  descricao TEXT,
  preco_venda DECIMAL(15,2),
  preco_aluguel DECIMAL(15,2),
  finalidade VARCHAR(255),
  status VARCHAR(255),
  dormitorios INT,
  banheiros INT,
  garagem INT,
  area_total DECIMAL(15,2),
  area_construida DECIMAL(15,2),
  endereco VARCHAR(255),
  numero VARCHAR(255),
  complemento VARCHAR(255),
  cep VARCHAR(255),
  caracteristicas TEXT,
  destaque BIT(1),
  bairro_id INT,
  tipo_imovel_id INT,
  CONSTRAINT fk_imoveis_bairro FOREIGN KEY (bairro_id) REFERENCES bairros(id),
  CONSTRAINT fk_imoveis_tipo FOREIGN KEY (tipo_imovel_id) REFERENCES tipos_imoveis(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fotos_imoveis (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  caminho VARCHAR(255),
  capa BIT(1),
  nome_arquivo VARCHAR(255),
  ordem INT,
  imovel_id INT NOT NULL,
  CONSTRAINT fk_fotos_imovel FOREIGN KEY (imovel_id) REFERENCES imoveis(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
