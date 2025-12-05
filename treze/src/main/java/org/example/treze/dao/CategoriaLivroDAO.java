package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.CategoriaLivro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaLivroDAO {

    public void inserir(CategoriaLivro categoria) throws SQLException {
        String sql = "INSERT INTO categoriaLivro (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                categoria.setIdCategoria(rs.getInt(1));
            }
        }
    }

    public void atualizar(CategoriaLivro categoria) throws SQLException {
        String sql = "UPDATE categoriaLivro SET nome = ? WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idCategoria) throws SQLException {
        String sql = "DELETE FROM categoriaLivro WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.executeUpdate();
        }
    }

    public CategoriaLivro buscarPorId(int idCategoria) throws SQLException {
        String sql = "SELECT * FROM categoriaLivro WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairCategoria(rs);
            }
        }
        return null;
    }

    public List<CategoriaLivro> listarTodos() throws SQLException {
        List<CategoriaLivro> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoriaLivro ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(extrairCategoria(rs));
            }
        }
        return categorias;
    }

    private CategoriaLivro extrairCategoria(ResultSet rs) throws SQLException {
        CategoriaLivro categoria = new CategoriaLivro();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome"));
        return categoria;
    }
}
