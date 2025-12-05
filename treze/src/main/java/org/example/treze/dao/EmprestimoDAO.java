package org.example.treze.dao;

import org.example.treze.conexao.DatabaseConnection;
import org.example.treze.model.Emprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public void inserir(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (id_usuario, id_livro, data_emprestimo, data_devolucao, situacao) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprestimo.getIdUsuario());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            stmt.setString(5, emprestimo.getSituacao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emprestimo.setIdEmprestimo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET id_usuario = ?, id_livro = ?, data_emprestimo = ?, " +
                "data_devolucao = ?, situacao = ? WHERE id_emprestimo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdUsuario());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            stmt.setString(5, emprestimo.getSituacao());
            stmt.setInt(6, emprestimo.getIdEmprestimo());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idEmprestimo) throws SQLException {
        String sql = "DELETE FROM emprestimo WHERE id_emprestimo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);
            stmt.executeUpdate();
        }
    }

    public Emprestimo buscarPorId(int idEmprestimo) throws SQLException {
        String sql = "SELECT e.*, u.nome as nome_usuario, l.titulo as titulo_livro " +
                "FROM emprestimo e " +
                "INNER JOIN usuario u ON e.id_usuario = u.id_usuario " +
                "INNER JOIN livro l ON e.id_livro = l.id_livro " +
                "WHERE e.id_emprestimo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extrairEmprestimo(rs);
            }
        }
        return null;
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, u.nome as nome_usuario, l.titulo as titulo_livro " +
                "FROM emprestimo e " +
                "INNER JOIN usuario u ON e.id_usuario = u.id_usuario " +
                "INNER JOIN livro l ON e.id_livro = l.id_livro " +
                "ORDER BY e.data_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                emprestimos.add(extrairEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    public List<Emprestimo> buscarPorSituacao(String situacao) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, u.nome as nome_usuario, l.titulo as titulo_livro " +
                "FROM emprestimo e " +
                "INNER JOIN usuario u ON e.id_usuario = u.id_usuario " +
                "INNER JOIN livro l ON e.id_livro = l.id_livro " +
                "WHERE e.situacao = ? " +
                "ORDER BY e.data_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, situacao);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                emprestimos.add(extrairEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    private Emprestimo extrairEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setIdEmprestimo(rs.getInt("id_emprestimo"));
        emprestimo.setIdUsuario(rs.getInt("id_usuario"));
        emprestimo.setNomeUsuario(rs.getString("nome_usuario"));
        emprestimo.setIdLivro(rs.getInt("id_livro"));
        emprestimo.setTituloLivro(rs.getString("titulo_livro"));
        Date dataEmprestimo = rs.getDate("data_emprestimo");
        if (dataEmprestimo != null) emprestimo.setDataEmprestimo(dataEmprestimo.toLocalDate());
        Date dataDevolucao = rs.getDate("data_devolucao");
        if (dataDevolucao != null) emprestimo.setDataDevolucao(dataDevolucao.toLocalDate());
        emprestimo.setSituacao(rs.getString("situacao"));
        return emprestimo;
    }
}
