package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.Assunto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuntoDAO {

    public void inserir(Assunto assunto) throws SQLException {
        String sql = "INSERT INTO assunto (descricao) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, assunto.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                assunto.setIdAssunto(rs.getInt(1));
            }
        }
    }

    public void atualizar(Assunto assunto) throws SQLException {
        String sql = "UPDATE assunto SET descricao = ? WHERE id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assunto.getDescricao());
            stmt.setInt(2, assunto.getIdAssunto());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idAssunto) throws SQLException {
        String sql = "DELETE FROM assunto WHERE id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAssunto);
            stmt.executeUpdate();
        }
    }

    public Assunto buscarPorId(int idAssunto) throws SQLException {
        String sql = "SELECT * FROM assunto WHERE id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAssunto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairAssunto(rs);
            }
        }
        return null;
    }

    public List<Assunto> listarTodos() throws SQLException {
        List<Assunto> assuntos = new ArrayList<>();
        String sql = "SELECT * FROM assunto ORDER BY descricao";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assuntos.add(extrairAssunto(rs));
            }
        }
        return assuntos;
    }

    private Assunto extrairAssunto(ResultSet rs) throws SQLException {
        Assunto assunto = new Assunto();
        assunto.setIdAssunto(rs.getInt("id_assunto"));
        assunto.setDescricao(rs.getString("descricao"));
        return assunto;
    }
}
