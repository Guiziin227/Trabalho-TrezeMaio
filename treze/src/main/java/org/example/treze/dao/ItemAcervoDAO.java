package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.ItemAcervo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemAcervoDAO {

    public void inserir(ItemAcervo item) throws SQLException {
        String sql = "INSERT INTO item_acervo (tipo_item, titulo, descricao, data_item, data_aquisicao, " +
                "estado, digitalizado, localizacao_fisica, id_doador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getTipoItem());
            stmt.setString(2, item.getTitulo());
            stmt.setString(3, item.getDescricao());
            stmt.setDate(4, item.getDataItem() != null ? Date.valueOf(item.getDataItem()) : null);
            stmt.setDate(5, item.getDataAquisicao() != null ? Date.valueOf(item.getDataAquisicao()) : null);
            stmt.setString(6, item.getEstado());
            stmt.setBoolean(7, item.isDigitalizado());
            stmt.setString(8, item.getLocalizacaoFisica());
            stmt.setObject(9, item.getIdDoador());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setIdItem(rs.getInt(1));
            }
        }
    }

    public void atualizar(ItemAcervo item) throws SQLException {
        String sql = "UPDATE item_acervo SET tipo_item = ?, titulo = ?, descricao = ?, data_item = ?, " +
                "data_aquisicao = ?, estado = ?, digitalizado = ?, localizacao_fisica = ?, id_doador = ? " +
                "WHERE id_item = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getTipoItem());
            stmt.setString(2, item.getTitulo());
            stmt.setString(3, item.getDescricao());
            stmt.setDate(4, item.getDataItem() != null ? Date.valueOf(item.getDataItem()) : null);
            stmt.setDate(5, item.getDataAquisicao() != null ? Date.valueOf(item.getDataAquisicao()) : null);
            stmt.setString(6, item.getEstado());
            stmt.setBoolean(7, item.isDigitalizado());
            stmt.setString(8, item.getLocalizacaoFisica());
            stmt.setObject(9, item.getIdDoador());
            stmt.setInt(10, item.getIdItem());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idItem) throws SQLException {
        String sql = "DELETE FROM item_acervo WHERE id_item = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItem);
            stmt.executeUpdate();
        }
    }

    public ItemAcervo buscarPorId(int idItem) throws SQLException {
        String sql = "SELECT i.*, t.descricao as tipo_descricao, d.nome as nome_doador " +
                "FROM item_acervo i " +
                "LEFT JOIN tipo_item t ON i.tipo_item = t.id_tipo " +
                "LEFT JOIN doador d ON i.id_doador = d.id_doador " +
                "WHERE i.id_item = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItem);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairItem(rs);
            }
        }
        return null;
    }

    public List<ItemAcervo> listarTodos() throws SQLException {
        List<ItemAcervo> itens = new ArrayList<>();
        String sql = "SELECT i.*, t.descricao as tipo_descricao, d.nome as nome_doador " +
                "FROM item_acervo i " +
                "LEFT JOIN tipo_item t ON i.tipo_item = t.id_tipo " +
                "LEFT JOIN doador d ON i.id_doador = d.id_doador " +
                "ORDER BY i.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                itens.add(extrairItem(rs));
            }
        }
        return itens;
    }

    public List<ItemAcervo> buscarPorTitulo(String titulo) throws SQLException {
        List<ItemAcervo> itens = new ArrayList<>();
        String sql = "SELECT i.*, t.descricao as tipo_descricao, d.nome as nome_doador " +
                "FROM item_acervo i " +
                "LEFT JOIN tipo_item t ON i.tipo_item = t.id_tipo " +
                "LEFT JOIN doador d ON i.id_doador = d.id_doador " +
                "WHERE i.titulo LIKE ? " +
                "ORDER BY i.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                itens.add(extrairItem(rs));
            }
        }
        return itens;
    }

    public List<ItemAcervo> buscarPorTipo(int tipoItem) throws SQLException {
        List<ItemAcervo> itens = new ArrayList<>();
        String sql = "SELECT i.*, t.descricao as tipo_descricao, d.nome as nome_doador " +
                "FROM item_acervo i " +
                "LEFT JOIN tipo_item t ON i.tipo_item = t.id_tipo " +
                "LEFT JOIN doador d ON i.id_doador = d.id_doador " +
                "WHERE i.tipo_item = ? " +
                "ORDER BY i.titulo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tipoItem);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                itens.add(extrairItem(rs));
            }
        }
        return itens;
    }

    private ItemAcervo extrairItem(ResultSet rs) throws SQLException {
        ItemAcervo item = new ItemAcervo();
        item.setIdItem(rs.getInt("id_item"));
        item.setTipoItem(rs.getInt("tipo_item"));
        item.setTipoItemDescricao(rs.getString("tipo_descricao"));
        item.setTitulo(rs.getString("titulo"));
        item.setDescricao(rs.getString("descricao"));
        Date dataItem = rs.getDate("data_item");
        if (dataItem != null) item.setDataItem(dataItem.toLocalDate());
        Date dataAquisicao = rs.getDate("data_aquisicao");
        if (dataAquisicao != null) item.setDataAquisicao(dataAquisicao.toLocalDate());
        item.setEstado(rs.getString("estado"));
        item.setDigitalizado(rs.getBoolean("digitalizado"));
        item.setLocalizacaoFisica(rs.getString("localizacao_fisica"));
        item.setIdDoador((Integer) rs.getObject("id_doador"));
        item.setNomeDoador(rs.getString("nome_doador"));
        return item;
    }
}
