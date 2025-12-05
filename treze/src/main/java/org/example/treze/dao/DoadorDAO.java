package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.Doador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoadorDAO {

    public void inserir(Doador doador) throws SQLException {
        String sql = "INSERT INTO doador (nome, telefone) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doador.getNome());
            stmt.setString(2, doador.getTelefone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                doador.setIdDoador(rs.getInt(1));
            }
        }
    }

    public void atualizar(Doador doador) throws SQLException {
        String sql = "UPDATE doador SET nome = ?, telefone = ? WHERE id_doador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doador.getNome());
            stmt.setString(2, doador.getTelefone());
            stmt.setInt(3, doador.getIdDoador());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idDoador) throws SQLException {
        String sql = "DELETE FROM doador WHERE id_doador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDoador);
            stmt.executeUpdate();
        }
    }

    public Doador buscarPorId(int idDoador) throws SQLException {
        String sql = "SELECT * FROM doador WHERE id_doador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDoador);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairDoador(rs);
            }
        }
        return null;
    }

    public List<Doador> listarTodos() throws SQLException {
        List<Doador> doadores = new ArrayList<>();
        String sql = "SELECT * FROM doador ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doadores.add(extrairDoador(rs));
            }
        }
        return doadores;
    }

    private Doador extrairDoador(ResultSet rs) throws SQLException {
        Doador doador = new Doador();
        doador.setIdDoador(rs.getInt("id_doador"));
        doador.setNome(rs.getString("nome"));
        doador.setTelefone(rs.getString("telefone"));
        return doador;
    }
}
