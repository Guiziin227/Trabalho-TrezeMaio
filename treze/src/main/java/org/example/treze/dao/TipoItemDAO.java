package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.TipoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoItemDAO {

    public void inserir(TipoItem tipoItem) throws SQLException {
        String sql = "INSERT INTO tipo_item (descricao) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipoItem.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                tipoItem.setIdTipo(rs.getInt(1));
            }
        }
    }

    public void atualizar(TipoItem tipoItem) throws SQLException {
        String sql = "UPDATE tipo_item SET descricao = ? WHERE id_tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipoItem.getDescricao());
            stmt.setInt(2, tipoItem.getIdTipo());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idTipo) throws SQLException {
        String sql = "DELETE FROM tipo_item WHERE id_tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTipo);
            stmt.executeUpdate();
        }
    }

    public TipoItem buscarPorId(int idTipo) throws SQLException {
        String sql = "SELECT * FROM tipo_item WHERE id_tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairTipoItem(rs);
            }
        }
        return null;
    }

    public List<TipoItem> listarTodos() throws SQLException {
        List<TipoItem> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipo_item ORDER BY descricao";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tipos.add(extrairTipoItem(rs));
            }
        }
        return tipos;
    }

    private TipoItem extrairTipoItem(ResultSet rs) throws SQLException {
        TipoItem tipoItem = new TipoItem();
        tipoItem.setIdTipo(rs.getInt("id_tipo"));
        tipoItem.setDescricao(rs.getString("descricao"));
        return tipoItem;
    }
}
