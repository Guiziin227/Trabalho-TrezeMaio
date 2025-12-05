package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, isbn, id_editora, ano_publicacao, edicao, numero_paginas, " +
                "idioma, quantidade_exemplares, quantidade_disponivel, localizacao, observacoes, id_doador) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setObject(2, (livro.getIsbn() != null && !livro.getIsbn().trim().isEmpty()) ? livro.getIsbn() : null);
            stmt.setObject(3, livro.getIdEditora() > 0 ? livro.getIdEditora() : null);
            stmt.setObject(4, livro.getAnoPublicacao() > 0 ? livro.getAnoPublicacao() : null);
            stmt.setString(5, livro.getEdicao());
            stmt.setObject(6, livro.getNumeroPaginas() > 0 ? livro.getNumeroPaginas() : null);
            stmt.setString(7, livro.getIdioma());
            stmt.setInt(8, livro.getQuantidadeExemplares());
            stmt.setInt(9, livro.getQuantidadeDisponivel());
            stmt.setString(10, livro.getLocalizacao());
            stmt.setString(11, livro.getObservacoes());
            stmt.setObject(12, livro.getIdDoador());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                livro.setIdLivro(rs.getInt(1));
            }
        }
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, isbn = ?, id_editora = ?, ano_publicacao = ?, " +
                "edicao = ?, numero_paginas = ?, idioma = ?, quantidade_exemplares = ?, " +
                "quantidade_disponivel = ?, localizacao = ?, observacoes = ?, id_doador = ? WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setObject(2, (livro.getIsbn() != null && !livro.getIsbn().trim().isEmpty()) ? livro.getIsbn() : null);
            stmt.setObject(3, livro.getIdEditora() > 0 ? livro.getIdEditora() : null);
            stmt.setObject(4, livro.getAnoPublicacao() > 0 ? livro.getAnoPublicacao() : null);
            stmt.setString(5, livro.getEdicao());
            stmt.setObject(6, livro.getNumeroPaginas() > 0 ? livro.getNumeroPaginas() : null);
            stmt.setString(7, livro.getIdioma());
            stmt.setInt(8, livro.getQuantidadeExemplares());
            stmt.setInt(9, livro.getQuantidadeDisponivel());
            stmt.setString(10, livro.getLocalizacao());
            stmt.setString(11, livro.getObservacoes());
            stmt.setObject(12, livro.getIdDoador());
            stmt.setInt(13, livro.getIdLivro());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public Livro buscarPorId(int idLivro) throws SQLException {
        String sql = "SELECT l.*, e.nome as nome_editora, d.nome as nome_doador, " +
                "GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') as autores, " +
                "GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') as categorias " +
                "FROM livro l " +
                "LEFT JOIN editora e ON l.id_editora = e.id_editora " +
                "LEFT JOIN doador d ON l.id_doador = d.id_doador " +
                "LEFT JOIN livro_autor la ON l.id_livro = la.id_livro " +
                "LEFT JOIN autor a ON la.id_autor = a.id_autor " +
                "LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro " +
                "LEFT JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria " +
                "WHERE l.id_livro = ? " +
                "GROUP BY l.id_livro";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairLivro(rs);
            }
        }
        return null;
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.*, e.nome as nome_editora, d.nome as nome_doador, " +
                "GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') as autores, " +
                "GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') as categorias " +
                "FROM livro l " +
                "LEFT JOIN editora e ON l.id_editora = e.id_editora " +
                "LEFT JOIN doador d ON l.id_doador = d.id_doador " +
                "LEFT JOIN livro_autor la ON l.id_livro = la.id_livro " +
                "LEFT JOIN autor a ON la.id_autor = a.id_autor " +
                "LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro " +
                "LEFT JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria " +
                "GROUP BY l.id_livro " +
                "ORDER BY l.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(extrairLivro(rs));
            }
        }
        return livros;
    }

    public List<Livro> buscarPorTitulo(String titulo) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.*, e.nome as nome_editora, d.nome as nome_doador, " +
                "GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') as autores, " +
                "GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') as categorias " +
                "FROM livro l " +
                "LEFT JOIN editora e ON l.id_editora = e.id_editora " +
                "LEFT JOIN doador d ON l.id_doador = d.id_doador " +
                "LEFT JOIN livro_autor la ON l.id_livro = la.id_livro " +
                "LEFT JOIN autor a ON la.id_autor = a.id_autor " +
                "LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro " +
                "LEFT JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria " +
                "WHERE l.titulo LIKE ? " +
                "GROUP BY l.id_livro " +
                "ORDER BY l.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(extrairLivro(rs));
            }
        }
        return livros;
    }

    public List<Livro> buscarPorAutor(String nomeAutor) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.*, e.nome as nome_editora, d.nome as nome_doador, " +
                "GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') as autores, " +
                "GROUP_CONCAT(DISTINCT c.nome SEPARATOR ', ') as categorias " +
                "FROM livro l " +
                "LEFT JOIN editora e ON l.id_editora = e.id_editora " +
                "LEFT JOIN doador d ON l.id_doador = d.id_doador " +
                "LEFT JOIN livro_autor la ON l.id_livro = la.id_livro " +
                "LEFT JOIN autor a ON la.id_autor = a.id_autor " +
                "LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro " +
                "LEFT JOIN categoriaLivro c ON lc.id_categoria = c.id_categoria " +
                "WHERE l.id_livro IN (SELECT la2.id_livro FROM livro_autor la2 " +
                "INNER JOIN autor a2 ON la2.id_autor = a2.id_autor WHERE a2.nome LIKE ?) " +
                "GROUP BY l.id_livro " +
                "ORDER BY l.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeAutor + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(extrairLivro(rs));
            }
        }
        return livros;
    }

    public void associarAutor(int idLivro, int idAutor) throws SQLException {
        String sql = "INSERT INTO livro_autor (id_livro, id_autor) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAutor);
            stmt.executeUpdate();
        }
    }

    public void removerAutor(int idLivro, int idAutor) throws SQLException {
        String sql = "DELETE FROM livro_autor WHERE id_livro = ? AND id_autor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAutor);
            stmt.executeUpdate();
        }
    }

    public void associarCategoria(int idLivro, int idCategoria) throws SQLException {
        String sql = "INSERT INTO livro_categoria (id_livro, id_categoria) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
        }
    }

    public void removerCategoria(int idLivro, int idCategoria) throws SQLException {
        String sql = "DELETE FROM livro_categoria WHERE id_livro = ? AND id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
        }
    }

    public void associarAssunto(int idLivro, int idAssunto) throws SQLException {
        String sql = "INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAssunto);
            stmt.executeUpdate();
        }
    }

    public void removerAssunto(int idLivro, int idAssunto) throws SQLException {
        String sql = "DELETE FROM livro_assunto WHERE id_livro = ? AND id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAssunto);
            stmt.executeUpdate();
        }
    }

    public void decrementarQuantidadeDisponivel(int idLivro) throws SQLException {
        String sql = "UPDATE livro SET quantidade_disponivel = quantidade_disponivel - 1 WHERE id_livro = ? AND quantidade_disponivel > 0";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public void incrementarQuantidadeDisponivel(int idLivro) throws SQLException {
        String sql = "UPDATE livro SET quantidade_disponivel = quantidade_disponivel + 1 WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public List<Integer> buscarAutoresDoLivro(int idLivro) throws SQLException {
        List<Integer> autores = new ArrayList<>();
        String sql = "SELECT id_autor FROM livro_autor WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                autores.add(rs.getInt("id_autor"));
            }
        }
        return autores;
    }

    public List<Integer> buscarCategoriasDoLivro(int idLivro) throws SQLException {
        List<Integer> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria FROM livro_categoria WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categorias.add(rs.getInt("id_categoria"));
            }
        }
        return categorias;
    }

    public List<Integer> buscarAssuntosDoLivro(int idLivro) throws SQLException {
        List<Integer> assuntos = new ArrayList<>();
        String sql = "SELECT id_assunto FROM livro_assunto WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assuntos.add(rs.getInt("id_assunto"));
            }
        }
        return assuntos;
    }

    public void removerTodosAutores(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro_autor WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public void removerTodasCategorias(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro_categoria WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public void removerTodosAssuntos(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro_assunto WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    private Livro extrairLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setIdLivro(rs.getInt("id_livro"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setIsbn(rs.getString("isbn"));
        livro.setIdEditora(rs.getInt("id_editora"));
        livro.setNomeEditora(rs.getString("nome_editora"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        livro.setEdicao(rs.getString("edicao"));
        livro.setNumeroPaginas(rs.getInt("numero_paginas"));
        livro.setIdioma(rs.getString("idioma"));
        livro.setQuantidadeExemplares(rs.getInt("quantidade_exemplares"));
        livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        livro.setLocalizacao(rs.getString("localizacao"));
        livro.setObservacoes(rs.getString("observacoes"));
        livro.setIdDoador((Integer) rs.getObject("id_doador"));
        livro.setNomeDoador(rs.getString("nome_doador"));
        livro.setAutores(rs.getString("autores"));
        livro.setCategorias(rs.getString("categorias"));
        return livro;
    }
}
