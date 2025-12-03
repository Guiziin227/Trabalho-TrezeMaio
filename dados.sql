use trezemaio;

INSERT INTO usuario (nome, cpf, email) VALUES
('Zamberlam', '12345678901', 'zamba@email.com'),
('Ricardo', '23456789012', 'ricardo@email.com'),
('Herysson', '34567890123', 'herysson@email.com'),
('Sylvio', '45678901234', 'sylvio@email.com'),
('André', '56789012345', 'andre@email.com');


INSERT INTO doador (nome, telefone) VALUES
('UFN', '(55) 3222-1234'),
('Associação Cultural Afro-Brasileira de Santa Maria', '(55) 3223-4567'),
('UFSM', '(55) 3221-9876'),
('Clube Social Treze de Maio', '(55) 3220-8765'),
('Claudio','(55) 99223-1233');

INSERT INTO categoriaLivro (nome) VALUES
('História Afro-Brasileira'),
('Literatura Africana'),
('Sociologia'),
('Direitos Humanos'),
('Educação Antirracista'),
('Arte e Cultura Negra'),
('Biografias'),
('Literatura Brasileira');

INSERT INTO autor (nome, nacionalidade) VALUES
('Abdias do Nascimento', 'Brasileiro'),
('Lélia Gonzalez', 'Brasileiro'),
('Muniz Sodré', 'Brasileiro'),
('Kabengele Munanga', 'Congolês/Brasileiro'),
('Djamila Ribeiro', 'Brasileiro'),
('Conceição Evaristo', 'Brasileiro'),
('Chimamanda Ngozi Adichie', 'Nigeriana'),
('Chinua Achebe', 'Nigeriano'),
('Machado de Assis', 'Brasileiro'),
('Carolina Maria de Jesus', 'Brasileiro'),
('Silvio Almeida', 'Brasileiro'),
('Angela Davis', 'Estadunidense');

INSERT INTO editora (nome, cnpj, cidade, pais) VALUES
('Companhia das Letras', '60500002000118', 'São Paulo', 'Brasil'),
('Zahar Editora', '42351252000192', 'Rio de Janeiro', 'Brasil'),
('Boitempo Editorial', '01465597000183', 'São Paulo', 'Brasil'),
('Editora Vozes', '30105588000191', 'Petrópolis', 'Brasil'),
('Editora Ática', '90331149000107', 'São Paulo', 'Brasil'),
('Paz e Terra', '31774649000120', 'Rio de Janeiro', 'Brasil'),
('Civilização Brasileira', '31486156000157', 'Rio de Janeiro', 'Brasil');

INSERT INTO livro (titulo, isbn, id_editora, ano_publicacao, edicao, numero_paginas, idioma, quantidade_exemplares, quantidade_disponivel, localizacao, observacoes, id_doador) VALUES
('O Genocídio do Negro Brasileiro: Processo de um Racismo Mascarado', '9788520007457', 1, 2016, '3ª', 232, 'Português', 3, 3, 'A1P2', 'Obra fundamental sobre racismo estrutural no Brasil', 2),
('Por um Feminismo Afro-Latino-Americano', '9788532627551', 2, 2020, '1ª', 160, 'Português', 2, 2, 'A1P3', 'Coletânea de ensaios de Lélia Gonzalez', 2),
('Claros do Enigma', '9788535931082', 3, 2017, '2ª', 176, 'Português', 2, 1, 'A1P4', 'Análise sobre identidade e negritude', 3),
('Rediscutindo a Mestiçagem no Brasil', '9788532603227', 4, 2019, '5ª', 152, 'Português', 2, 2, 'A2P2', 'Discussão sobre construção racial brasileira', 1),
('Quem Tem Medo do Feminismo Negro?', '9788535930191', 1, 2018, '1ª', 144, 'Português', 4, 3, 'A1P1', 'Obra de Djamila Ribeiro sobre interseccionalidade', 2),
('Olhos d\'Água', '9788563560377', 3, 2016, '1ª', 116, 'Português', 3, 3, 'A1P4', 'Contos de Conceição Evaristo', 4),
('Americanah', '9788535927627', 1, 2014, '1ª', 517, 'Português', 2, 1, 'A3P2', 'Romance de Chimamanda sobre racismo e identidade', NULL),
('O Mundo Se Despedaça', '9788535911664', 1, 2009, '1ª', 280, 'Português', 2, 2, 'A3P5', 'Clássico da literatura africana', 3),
('Memórias Póstumas de Brás Cubas', '9788508040070', 5, 2015, '5ª', 368, 'Português', 3, 2, 'A3P1', 'Obra-prima de Machado de Assis', NULL),
('Quarto de Despejo: Diário de uma Favelada', '9788508142453', 5, 2014, '10ª', 200, 'Português', 3, 2, 'A3P4', 'Diário de Carolina Maria de Jesus', 4),
('Pequeno Manual Antirracista', '9788535932171', 1, 2019, '1ª', 136, 'Português', 5, 4, 'E2P2', 'Guia prático de Djamila Ribeiro', 2),
('Racismo Estrutural', '9788595340855', 6, 2020, '2ª', 264, 'Português', 3, 3, 'E1P2', 'Silvio Almeida sobre racismo no Brasil', 2),
('Mulheres, Raça e Classe', '9788575595039', 3, 2016, '1ª', 244, 'Português', 2, 2, 'E2P3', 'Angela Davis sobre interseccionalidade', 3),
('Becos da Memória', '9788574601014', 3, 2017, '3ª', 168, 'Português', 2, 2, 'E3P1', 'Romance de Conceição Evaristo', 4),
('Hibisco Roxo', '9788535928129', 1, 2011, '1ª', 312, 'Português', 2, 2, 'E5P5', 'Romance de Chimamanda sobre Nigéria', NULL);

INSERT INTO livro_autor (id_livro, id_autor) VALUES
(1, 1),   
(3, 3),  
(4, 4),   
(5, 5), 
(6, 6),   
(7, 7),   
(8, 8),  
(9, 9),  
(10, 10), 
(11, 5), 
(12, 11), 
(13, 12),
(14, 6),  
(15, 7); 

INSERT INTO livro_categoria (id_livro, id_categoria) VALUES
(1, 1), (1, 4),  
(2, 1), (2, 3),  
(3, 1), (3, 6),   
(4, 1), (4, 3),  
(5, 1), (5, 4), (5, 5), 
(6, 2), (6, 6), (6, 8),
(7, 2), (7, 8),
(8, 2),       
(9, 8), (9, 7),
(10, 1), (10, 8), (10, 7),
(11, 1), (11, 5),
(12, 1), (12, 3), (12, 5),
(13, 1), (13, 3), (13, 4),
(14, 8), (14, 2),
(15, 2), (15, 6); 

INSERT INTO assunto (descricao) VALUES
('Abolição da Escravatura'),
('Movimento Negro Brasileiro'),
('Racismo e Discriminação Racial'),
('Feminismo Negro'),
('Cultura Afro-Brasileira'),
('Literatura e Identidade'),
('História do Brasil Colonial'),
('Direitos Civis'),
('Educação para Relações Étnico-Raciais'),
('Memória e Patrimônio Cultural');

INSERT INTO livro_assunto (id_livro, id_assunto) VALUES
(1, 3), (1, 2), (1, 8),    
(2, 4), (2, 2),          
(3, 5), (3, 6),             
(4, 3), (4, 5),         
(5, 4), (5, 9),            
(6, 6), (6, 5),           
(7, 3), (7, 6),            
(8, 6), (8, 5),            
(9, 6),                   
(10, 10), (10, 3),        
(11, 9), (11, 3),         
(12, 3), (12, 9),           
(13, 4), (13, 8),       
(14, 10), (14, 6),        
(15, 6), (15, 5);          

INSERT INTO tipo_item (descricao) VALUES
('Jornal'),
('Ata de Reunião'),
('Fotografia'),
('Carta/Correspondência'),
('Relato Oral (Áudio)'),
('Objeto Histórico'),
('Documento Oficial'),
('Revista/Periódico'),
('Certificado/Diploma'),
('Manuscrito');

INSERT INTO item_acervo (tipo_item, titulo, descricao, data_item, data_aquisicao, estado, digitalizado, localizacao_fisica, id_doador) VALUES
(3, 'Celebração do 13 de Maio de 1888', 'Fotografia histórica da celebração da abolição da escravatura na Praça Saldanha Marinho em Santa Maria', '1888-05-13', '2010-03-15', 'Bom', TRUE, 'ARM-01-GAV-A', 1),
(1, 'Diário do Interior - Edição Especial Abolição', 'Jornal local noticiando a abolição da escravatura, com artigos e ilustrações da época', '1888-05-14', '2012-07-20', 'Regular', TRUE, 'ARM-02-GAV-A', 2),
(2, 'Ata de Fundação do Clube 13 de Maio', 'Documento de fundação da sociedade recreativa e cultural Clube 13 de Maio, assinado pelos fundadores', '1901-07-20', '2008-05-13', 'Bom', TRUE, 'ARM-03-GAV-A', 5),
(4, 'Correspondência entre Líderes Comunitários (1925)', 'Cartas trocadas sobre a organização da comunidade negra em Santa Maria no início do século XX', '1925-03-10', '2015-11-02', 'Regular', FALSE, 'ARM-01-GAV-B', 1),
(3, 'Desfile de Carnaval do Clube 13 de Maio - 1950', 'Registro fotográfico do desfile carnavalesco do clube, mostrando fantasias e alegorias', '1950-02-20', '2011-02-28', 'Bom', TRUE, 'ARM-01-GAV-C', 5),
(5, 'Entrevista com Dona Maria José da Silva', 'Relato oral sobre a vida no pós-abolição e memórias da comunidade afro em Santa Maria. Áudio digitalizado de 60 minutos', '2010-08-15', '2010-08-16', 'Excelente', TRUE, 'DIGITAL', 4),
(6, 'Medalha Comemorativa - Centenário da Abolição', 'Medalha em bronze cunhada para o centenário da abolição em Santa Maria, com brasão da cidade', '1988-05-13', '2009-05-13', 'Excelente', TRUE, 'VIT-01-PRATELEIRA-A', 3),
(7, 'Estatuto Social Original do Clube 13 de Maio', 'Estatuto original da entidade, datilografado, com carimbo e assinaturas dos fundadores', '1902-01-15', '2008-05-13', 'Regular', TRUE, 'ARM-03-GAV-B', 5),
(3, 'Festa Junina da Comunidade - Década de 1960', 'Registro fotográfico de festa tradicional da comunidade afro-brasileira local', '1965-06-24', '2013-06-20', 'Bom', FALSE, 'ARM-01-GAV-D', 1),
(1, 'Jornal A Razão - Matéria sobre Movimento Negro', 'Reportagem sobre a organização política do movimento negro em Santa Maria durante a redemocratização', '1978-11-20', '2016-11-20', 'Bom', TRUE, 'ARM-02-GAV-B', 2),
(2, 'Ata da Reunião sobre Criação do Museu', 'Documento histórico da reunião que propôs a criação do Museu Treze de Maio', '1995-05-13', '1995-05-14', 'Excelente', TRUE, 'ARM-03-GAV-C', 3),
(7, 'Carta de Apoio das Autoridades Municipais', 'Correspondência oficial de apoio à criação do museu, assinada pelo prefeito e vereadores', '1996-03-20', '1996-03-21', 'Excelente', TRUE, 'ARM-01-GAV-E', 3);

INSERT INTO emprestimo (id_usuario, id_livro, data_emprestimo, data_devolucao, situacao) VALUES
(1, 5, '2025-11-10', '2025-11-24', 'Devolvido'),
(2, 7, '2025-11-20', NULL, 'Em andamento'),
(3, 1, '2025-11-25', NULL, 'Em andamento'),
(4, 11, '2025-12-01', NULL, 'Em andamento'),
(5, 9, '2025-11-15', '2025-11-29', 'Devolvido'),
(1, 6, '2025-12-02', NULL, 'Em andamento'),
(2, 12, '2025-11-18', '2025-12-02', 'Devolvido'),
(3, 3, '2025-12-03', NULL, 'Em andamento');
