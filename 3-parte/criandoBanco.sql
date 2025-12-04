use trezemaio;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE doador (
    id_doador INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    telefone VARCHAR(20) NOT NULL
);


CREATE TABLE categoriaLivro (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE autor (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    nacionalidade VARCHAR(50)
);

CREATE TABLE editora (
    id_editora INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj varchar(14) NOT NULL,
    cidade VARCHAR(100),
    pais VARCHAR(50)
);


CREATE TABLE livro (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    id_editora INT,
    ano_publicacao INT,
    edicao VARCHAR(20),
    numero_paginas INT,
    idioma VARCHAR(30),
    quantidade_exemplares INT DEFAULT 1,
    quantidade_disponivel INT DEFAULT 1,
    localizacao VARCHAR(50),
    observacoes TEXT,
    id_doador INT NULL,
    CONSTRAINT fk_livro_editora FOREIGN KEY (id_editora) REFERENCES editora(id_editora),
    CONSTRAINT fk_livro_doador FOREIGN KEY (id_doador) REFERENCES doador(id_doador)
);

CREATE TABLE livro_autor (
    id_livro INT,
    id_autor INT,
    PRIMARY KEY (id_livro, id_autor),
    CONSTRAINT fk_livro_autor_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE CASCADE,
    CONSTRAINT fk_livro_autor_autor FOREIGN KEY (id_autor) REFERENCES autor(id_autor) ON DELETE CASCADE
);

CREATE TABLE livro_categoria (
    id_livro INT,
    id_categoria INT,
    PRIMARY KEY (id_livro, id_categoria),
    CONSTRAINT fk_livro_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE CASCADE,
    CONSTRAINT fk_livro_categoria FOREIGN KEY (id_categoria) REFERENCES categoriaLivro(id_categoria) ON DELETE CASCADE
);

CREATE TABLE tipo_item (
	id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    descricao varchar(100)
);

CREATE TABLE item_acervo (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    tipo_item INT,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_item DATE,
    data_aquisicao DATE DEFAULT (CURRENT_DATE),
    estado varchar(30),
    digitalizado boolean,
    localizacao_fisica VARCHAR(100),
    id_doador INT NULL,
    CONSTRAINT fk_tipo_item FOREIGN KEY (tipo_item) REFERENCES tipo_item(id_tipo),
    CONSTRAINT fk_item_acervo_doador FOREIGN KEY (id_doador) REFERENCES doador(id_doador)
);

CREATE TABLE emprestimo (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,
    situacao VARCHAR(20),
    CONSTRAINT fk_emprestimo_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro)
);

CREATE TABLE assunto (
    id_assunto INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE livro_assunto (
    id_livro INT NOT NULL,
    id_assunto INT NOT NULL,
    PRIMARY KEY (id_livro, id_assunto),
    FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE CASCADE,
    FOREIGN KEY (id_assunto) REFERENCES assunto(id_assunto) ON DELETE CASCADE
);





