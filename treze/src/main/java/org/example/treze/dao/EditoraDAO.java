package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.Editora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditoraDAO {

    public void inserir(Editora editora) throws SQLException {
        String sql = "INSERT INTO editora (nome, cnpj, cidade, pais) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCnpj());
            stmt.setString(3, editora.getCidade());
            stmt.setString(4, editora.getPais());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                editora.setIdEditora(rs.getInt(1));
            }
        }
    }

    public void atualizar(Editora editora) throws SQLException {
        String sql = "UPDATE editora SET nome = ?, cnpj = ?, cidade = ?, pais = ? WHERE id_editora = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCnpj());
            stmt.setString(3, editora.getCidade());
            stmt.setString(4, editora.getPais());
            stmt.setInt(5, editora.getIdEditora());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idEditora) throws SQLException {
        String sql = "DELETE FROM editora WHERE id_editora = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEditora);
            stmt.executeUpdate();
        }
    }

    public Editora buscarPorId(int idEditora) throws SQLException {
        String sql = "SELECT * FROM editora WHERE id_editora = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEditora);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairEditora(rs);
            }
        }
        return null;
    }

    public List<Editora> listarTodos() throws SQLException {
        List<Editora> editoras = new ArrayList<>();
        String sql = "SELECT * FROM editora ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                editoras.add(extrairEditora(rs));
            }
        }
        return editoras;
    }

    private Editora extrairEditora(ResultSet rs) throws SQLException {
        Editora editora = new Editora();
        editora.setIdEditora(rs.getInt("id_editora"));
        editora.setNome(rs.getString("nome"));
        editora.setCnpj(rs.getString("cnpj"));
        editora.setCidade(rs.getString("cidade"));
        editora.setPais(rs.getString("pais"));
        return editora;
    }
}
