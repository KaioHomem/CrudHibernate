-- ============================================================
-- Script do banco de dados (MySQL)
-- Atividade: Cadastro em Duas Tabelas com Swing e Hibernate
-- Autores: Kaio Felipe Homem e Jefferson Machado
-- ============================================================
-- Observação: se "hibernate.hbm2ddl.auto=update" estiver ativo no
-- persistence.xml, o Hibernate cria estas tabelas automaticamente.
-- Este script serve para criar/entender o banco manualmente.

CREATE DATABASE IF NOT EXISTS crudhibernate
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE crudhibernate;

-- Tabela de categorias
CREATE TABLE IF NOT EXISTS categorias (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

-- Tabela de produtos (cada produto pertence a uma categoria)
CREATE TABLE IF NOT EXISTS produtos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    descricao    VARCHAR(255),
    quantidade   INT,
    preco        DOUBLE,
    categoria_id INT,
    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);

-- Dados de exemplo (opcional)
INSERT INTO categorias (nome, descricao) VALUES
    ('Bebidas', 'Refrigerantes, sucos e águas'),
    ('Limpeza', 'Produtos de limpeza em geral');

INSERT INTO produtos (nome, descricao, quantidade, preco, categoria_id) VALUES
    ('Refrigerante Cola 2L', 'Garrafa 2 litros', 50, 8.99, 1),
    ('Suco de Laranja 1L',   'Caixa 1 litro',    30, 6.50, 1),
    ('Detergente 500ml',     'Neutro',           80, 2.49, 2);
