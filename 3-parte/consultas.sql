use trezemaio;

# Saber sobre o sistema geral, registros, total exemplares
SELECT 
    'Livros' AS tipo_acervo,
    COUNT(*) AS quantidade_registros,
    SUM(quantidade_exemplares) AS total_exemplares,
    SUM(quantidade_disponivel) AS disponiveis,
    (SUM(quantidade_exemplares) - SUM(quantidade_disponivel)) AS em_emprestimo
FROM livro

UNION ALL

SELECT 
    'Itens Históricos' AS tipo_acervo,
    COUNT(*) AS quantidade_registros,
    COUNT(*) AS total_exemplares,
    SUM(CASE WHEN digitalizado = TRUE THEN 1 ELSE 0 END) AS disponiveis,
    SUM(CASE WHEN digitalizado = FALSE THEN 1 ELSE 0 END) AS em_emprestimo
FROM item_acervo;

#MOSTRAR POR TOTAL DE LIVRO POR CATEGORIA
SELECT 
    c.nome AS categoria,
    COUNT(DISTINCT lc.id_livro) AS total_livros,
    GROUP_CONCAT(DISTINCT l.titulo ORDER BY l.titulo SEPARATOR ' | ') AS titulos
FROM categoriaLivro c
LEFT JOIN livro_categoria lc ON c.id_categoria = lc.id_categoria
LEFT JOIN livro l ON lc.id_livro = l.id_livro
GROUP BY c.id_categoria, c.nome
ORDER BY total_livros DESC;

#COISAS DOADAS POR ALGUEM ESPECIFICO
SELECT 
    ia.id_item,
    ti.descricao AS tipo_documento,
    ia.titulo,
    ia.descricao,
    DATE_FORMAT(ia.data_item, '%d/%m/%Y') AS data_documento,
    ia.estado AS estado_conservacao,
    ia.digitalizado,
    ia.localizacao_fisica,
    d.nome AS doador,
    d.telefone AS contato_doador
FROM item_acervo ia
INNER JOIN tipo_item ti ON ia.tipo_item = ti.id_tipo
INNER JOIN doador d ON ia.id_doador = d.id_doador
WHERE (ti.descricao LIKE '%Carta%' OR ti.descricao LIKE '%Fotografia%')
  AND d.nome LIKE '%Família Oliveira%'
ORDER BY ia.data_item;

# Livros publicados após 2010 sobre História Afro-Brasileira
SELECT 
    l.id_livro,
    l.titulo,
    GROUP_CONCAT(DISTINCT a.nome ORDER BY a.nome SEPARATOR ', ') AS autores,
    e.nome AS editora,
    l.ano_publicacao,
    l.isbn,
    l.quantidade_disponivel,
    GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') AS categorias
FROM livro l
INNER JOIN livro_categoria lc ON l.id_livro = lc.id_livro
INNER JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria
LEFT JOIN livro_autor la ON l.id_livro = la.id_livro
LEFT JOIN autor a ON la.id_autor = a.id_autor
LEFT JOIN editora e ON l.id_editora = e.id_editora
WHERE c.nome LIKE '%História Afro-Brasileira%'
  AND l.ano_publicacao > 2010
GROUP BY l.id_livro, l.titulo, e.nome, l.ano_publicacao, l.isbn, l.quantidade_disponivel
ORDER BY l.ano_publicacao DESC, l.titulo;


# Autor com mais livros na biblioteca
SELECT 
    a.id_autor,
    a.nome AS autor,
    a.nacionalidade,
    COUNT(DISTINCT la.id_livro) AS total_livros_cadastrados,
    SUM(l.quantidade_exemplares) AS total_exemplares_no_acervo,
    GROUP_CONCAT(DISTINCT l.titulo ORDER BY l.titulo SEPARATOR ' | ') AS livros
FROM autor a
INNER JOIN livro_autor la ON a.id_autor = la.id_autor
INNER JOIN livro l ON la.id_livro = l.id_livro
GROUP BY a.id_autor, a.nome, a.nacionalidade
ORDER BY total_livros_cadastrados DESC, total_exemplares_no_acervo DESC
LIMIT 1;

# nao digitalizad
SELECT 
    ia.id_item,
    ti.descricao AS tipo_item,
    ia.titulo,
    ia.descricao,
    DATE_FORMAT(ia.data_item, '%d/%m/%Y') AS data_documento,
    ia.estado AS estado_conservacao,
    ia.localizacao_fisica,
    d.nome AS doador
FROM item_acervo ia
INNER JOIN tipo_item ti ON ia.tipo_item = ti.id_tipo
LEFT JOIN doador d ON ia.id_doador = d.id_doador
WHERE ia.digitalizado = FALSE
ORDER BY 
    ia.estado ASC,
    ia.data_item ASC;
    
# Qauntos emprestimos em cada mes
SELECT 
    DATE_FORMAT(emp.data_emprestimo, '%Y-%m') AS mes_ano,
    COUNT(*) AS total_emprestimos,
    COUNT(DISTINCT emp.id_usuario) AS usuarios_diferentes,
    COUNT(DISTINCT emp.id_livro) AS livros_diferentes,
    SUM(CASE WHEN emp.situacao = 'Devolvido' THEN 1 ELSE 0 END) AS devolvidos,
    SUM(CASE WHEN emp.situacao = 'Em andamento' THEN 1 ELSE 0 END) AS em_andamento
FROM emprestimo emp
GROUP BY DATE_FORMAT(emp.data_emprestimo, '%Y-%m')
ORDER BY mes_ano DESC;


# Itens mais antigos
SELECT 
    ia.id_item,
    ti.descricao AS tipo,
    ia.titulo,
    ia.data_item,
    YEAR(ia.data_item) AS ano,
    TIMESTAMPDIFF(YEAR, ia.data_item, CURDATE()) AS anos_desde_documento,
    ia.estado,
    ia.digitalizado,
    d.nome AS doador
FROM item_acervo ia
INNER JOIN tipo_item ti ON ia.tipo_item = ti.id_tipo
LEFT JOIN doador d ON ia.id_doador = d.id_doador
WHERE ia.data_item IS NOT NULL
ORDER BY ia.data_item ASC
LIMIT 15;


SELECT 
    a.nome AS autor,
    a.nacionalidade,
    COUNT(DISTINCT la.id_livro) AS total_livros,
    GROUP_CONCAT(DISTINCT l.titulo ORDER BY l.titulo SEPARATOR ' , ') AS obras,
    SUM(l.quantidade_exemplares) AS total_exemplares_acervo
FROM autor a
INNER JOIN livro_autor la ON a.id_autor = la.id_autor
INNER JOIN livro l ON la.id_livro = l.id_livro
GROUP BY a.id_autor, a.nome, a.nacionalidade
ORDER BY total_livros DESC, total_exemplares_acervo DESC;

# livro com mais emprestimo
SELECT 
    l.titulo,
    GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') AS autores,
    COUNT(emp.id_emprestimo) AS total_emprestimos,
    l.quantidade_exemplares
FROM livro l
LEFT JOIN livro_autor la ON l.id_livro = la.id_livro
LEFT JOIN autor a ON la.id_autor = a.id_autor
LEFT JOIN emprestimo emp ON l.id_livro = emp.id_livro
GROUP BY l.id_livro, l.titulo, l.quantidade_exemplares
HAVING COUNT(emp.id_emprestimo) > 0
ORDER BY total_emprestimos DESC
LIMIT 10;

# Livros disponiveis de autor especifico

SELECT 
    l.id_livro,
    l.titulo,
    a.nome AS autor,
    e.nome AS editora,
    l.ano_publicacao,
    l.quantidade_disponivel,
    l.localizacao,
    GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') AS categorias
FROM livro l
INNER JOIN livro_autor la ON l.id_livro = la.id_livro
INNER JOIN autor a ON la.id_autor = a.id_autor
LEFT JOIN editora e ON l.id_editora = e.id_editora
LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro
LEFT JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria
WHERE a.nome LIKE '%Djamila Ribeiro%'
  AND l.quantidade_disponivel > 0
GROUP BY l.id_livro, l.titulo, a.nome, e.nome, l.ano_publicacao, 
         l.quantidade_disponivel, l.localizacao
ORDER BY l.ano_publicacao DESC;