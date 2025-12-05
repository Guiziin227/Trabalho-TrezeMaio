package org.example.treze.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.treze.dao.EmprestimoDAO;
import org.example.treze.dao.LivroDAO;
import org.example.treze.dao.UsuarioDAO;
import org.example.treze.model.Emprestimo;
import org.example.treze.model.Livro;
import org.example.treze.model.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class EmprestimoController {

    @FXML
    private TableView<Emprestimo> tableEmprestimos;
    @FXML
    private TableColumn<Emprestimo, Integer> colId;
    @FXML
    private TableColumn<Emprestimo, String> colUsuario;
    @FXML
    private TableColumn<Emprestimo, String> colLivro;
    @FXML
    private TableColumn<Emprestimo, LocalDate> colDataEmprestimo;
    @FXML
    private TableColumn<Emprestimo, LocalDate> colDataDevolucao;
    @FXML
    private TableColumn<Emprestimo, String> colSituacao;

    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LivroDAO livroDAO = new LivroDAO();

    private ObservableList<Emprestimo> emprestimosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmprestimo"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colLivro.setCellValueFactory(new PropertyValueFactory<>("tituloLivro"));
        colDataEmprestimo.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));
        colDataDevolucao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));
        colSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));

        carregarEmprestimos();
    }

    private void carregarEmprestimos() {
        try {
            emprestimosList.clear();
            emprestimosList.addAll(emprestimoDAO.listarTodos());
            tableEmprestimos.setItems(emprestimosList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar empréstimos", e.getMessage());
        }
    }

    @FXML
    private void novoEmprestimo() {
        mostrarFormularioEmprestimo(null);
    }

    @FXML
    private void editarEmprestimo() {
        Emprestimo selecionado = tableEmprestimos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um empréstimo para editar");
            return;
        }
        mostrarFormularioEmprestimo(selecionado);
    }

    @FXML
    private void excluirEmprestimo() {
        Emprestimo selecionado = tableEmprestimos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um empréstimo para excluir");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o empréstimo?");
        confirmacao.setContentText("ID: " + selecionado.getIdEmprestimo());

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                emprestimoDAO.deletar(selecionado.getIdEmprestimo());
                carregarEmprestimos();
                mostrarSucesso("Empréstimo excluído com sucesso!");
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir empréstimo", e.getMessage());
            }
        }
    }

    @FXML
    private void registrarDevolucao() {
        Emprestimo selecionado = tableEmprestimos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um empréstimo para registrar devolução");
            return;
        }

        if ("Devolvido".equals(selecionado.getSituacao())) {
            mostrarAlerta("Este empréstimo já foi devolvido!");
            return;
        }

        selecionado.setDataDevolucao(LocalDate.now());
        selecionado.setSituacao("Devolvido");

        try {
            emprestimoDAO.atualizar(selecionado);
            livroDAO.incrementarQuantidadeDisponivel(selecionado.getIdLivro());
            carregarEmprestimos();
            mostrarSucesso("Devolução registrada com sucesso!");
        } catch (SQLException e) {
            mostrarErro("Erro ao registrar devolução", e.getMessage());
        }
    }

    private void mostrarFormularioEmprestimo(Emprestimo emprestimo) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(emprestimo == null ? "Novo Empréstimo" : "Editar Empréstimo");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Usuario> cbUsuario = new ComboBox<>();
        ComboBox<Livro> cbLivro = new ComboBox<>();

        DatePicker dpDataEmprestimo = new DatePicker(
                emprestimo != null ? emprestimo.getDataEmprestimo() : LocalDate.now());
        DatePicker dpDataDevolucao = new DatePicker(emprestimo != null ? emprestimo.getDataDevolucao() : null);
        ComboBox<String> cbSituacao = new ComboBox<>();
        cbSituacao.setItems(FXCollections.observableArrayList("Em andamento", "Devolvido"));
        if (emprestimo != null) {
            cbSituacao.setValue(emprestimo.getSituacao());
        } else {
            cbSituacao.setValue("Em andamento");
        }

        try {
            cbUsuario.setItems(FXCollections.observableArrayList(usuarioDAO.listarTodos()));
            cbLivro.setItems(FXCollections.observableArrayList(livroDAO.listarTodos()));

            if (emprestimo != null) {
                Usuario usuario = usuarioDAO.buscarPorId(emprestimo.getIdUsuario());
                Livro livro = livroDAO.buscarPorId(emprestimo.getIdLivro());
                cbUsuario.setValue(usuario);
                cbLivro.setValue(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grid.add(new Label("Usuário:"), 0, 0);
        grid.add(cbUsuario, 1, 0);
        grid.add(new Label("Livro:"), 0, 1);
        grid.add(cbLivro, 1, 1);
        grid.add(new Label("Data Empréstimo:"), 0, 2);
        grid.add(dpDataEmprestimo, 1, 2);
        grid.add(new Label("Data Devolução:"), 0, 3);
        grid.add(dpDataDevolucao, 1, 3);
        grid.add(new Label("Situação:"), 0, 4);
        grid.add(cbSituacao, 1, 4);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {
            if (cbUsuario.getValue() == null || cbLivro.getValue() == null) {
                mostrarAlerta("Selecione um usuário e um livro!");
                return;
            }

            Livro livroSelecionado = cbLivro.getValue();
            if (emprestimo == null && livroSelecionado.getQuantidadeDisponivel() == 0) {
                mostrarAlerta("Este livro não possui exemplares disponíveis para empréstimo!");
                return;
            }

            try {
                boolean criarNovo = emprestimo == null;
                String situacaoAnterior = emprestimo != null ? emprestimo.getSituacao() : null;
                String situacaoNova = cbSituacao.getValue();

                Emprestimo emprestimoSalvar = emprestimo != null ? emprestimo : new Emprestimo();
                emprestimoSalvar.setIdUsuario(cbUsuario.getValue().getIdUsuario());
                emprestimoSalvar.setIdLivro(cbLivro.getValue().getIdLivro());
                emprestimoSalvar.setDataEmprestimo(dpDataEmprestimo.getValue());
                emprestimoSalvar.setDataDevolucao(dpDataDevolucao.getValue());
                emprestimoSalvar.setSituacao(situacaoNova);

                if (criarNovo) {
                    emprestimoDAO.inserir(emprestimoSalvar);
                    livroDAO.decrementarQuantidadeDisponivel(livroSelecionado.getIdLivro());
                } else {
                    emprestimoDAO.atualizar(emprestimoSalvar);
                    if ("Em andamento".equals(situacaoAnterior) && "Devolvido".equals(situacaoNova)) {
                        livroDAO.incrementarQuantidadeDisponivel(emprestimoSalvar.getIdLivro());
                    } else if ("Devolvido".equals(situacaoAnterior) && "Em andamento".equals(situacaoNova)) {
                        livroDAO.decrementarQuantidadeDisponivel(emprestimoSalvar.getIdLivro());
                    }
                }

                carregarEmprestimos();
                dialog.close();
                mostrarSucesso("Empréstimo salvo com sucesso!");
            } catch (SQLException ex) {
                mostrarErro("Erro ao salvar empréstimo", ex.getMessage());
            }
        });

        HBox botoes = new HBox(10, btnSalvar, btnCancelar);
        grid.add(botoes, 1, 5);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/org/example/treze/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
