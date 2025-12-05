package org.example.treze.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.treze.dao.ItemAcervoDAO;
import org.example.treze.dao.LivroDAO;
import org.example.treze.dao.TipoItemDAO;
import org.example.treze.model.ItemAcervo;
import org.example.treze.model.Livro;
import org.example.treze.model.TipoItem;

import java.sql.SQLException;
import java.time.LocalDate;

public class ClienteController {

    @FXML
    private RadioButton rbTitulo;
    @FXML
    private RadioButton rbAutor;
    @FXML
    private TextField txtBuscaLivro;
    @FXML
    private TableView<Livro> tableLivros;
    @FXML
    private TableColumn<Livro, String> colLivroTitulo;
    @FXML
    private TableColumn<Livro, String> colLivroAutores;
    @FXML
    private TableColumn<Livro, String> colLivroEditora;
    @FXML
    private TableColumn<Livro, Integer> colLivroAno;
    @FXML
    private TableColumn<Livro, Integer> colLivroDisponivel;

    @FXML
    private RadioButton rbNome;
    @FXML
    private RadioButton rbTipo;
    @FXML
    private TextField txtBuscaAcervo;
    @FXML
    private ComboBox<TipoItem> cbTipoAcervo;
    @FXML
    private TableView<ItemAcervo> tableItens;
    @FXML
    private TableColumn<ItemAcervo, String> colItemTitulo;
    @FXML
    private TableColumn<ItemAcervo, String> colItemTipo;
    @FXML
    private TableColumn<ItemAcervo, LocalDate> colItemData;
    @FXML
    private TableColumn<ItemAcervo, String> colItemEstado;
    @FXML
    private TableColumn<ItemAcervo, String> colItemLocalizacao;

    private LivroDAO livroDAO = new LivroDAO();
    private ItemAcervoDAO itemDAO = new ItemAcervoDAO();
    private TipoItemDAO tipoItemDAO = new TipoItemDAO();

    private ObservableList<Livro> livrosList = FXCollections.observableArrayList();
    private ObservableList<ItemAcervo> itensList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colLivroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLivroAutores.setCellValueFactory(new PropertyValueFactory<>("autores"));
        colLivroEditora.setCellValueFactory(new PropertyValueFactory<>("nomeEditora"));
        colLivroAno.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colLivroDisponivel.setCellValueFactory(new PropertyValueFactory<>("quantidadeDisponivel"));

        colItemTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colItemTipo.setCellValueFactory(new PropertyValueFactory<>("tipoItemDescricao"));
        colItemData.setCellValueFactory(new PropertyValueFactory<>("dataItem"));
        colItemEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colItemLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacaoFisica"));

        rbTipo.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtBuscaAcervo.setVisible(false);
                cbTipoAcervo.setVisible(true);
                try {
                    cbTipoAcervo.setItems(FXCollections.observableArrayList(tipoItemDAO.listarTodos()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                txtBuscaAcervo.setVisible(true);
                cbTipoAcervo.setVisible(false);
            }
        });

        listarTodosLivros();
        listarTodosItens();
    }

    @FXML
    private void pesquisarLivros() {
        String busca = txtBuscaLivro.getText().trim();
        if (busca.isEmpty()) {
            mostrarAlerta("Digite algo para pesquisar");
            return;
        }

        try {
            livrosList.clear();
            if (rbTitulo.isSelected()) {
                livrosList.addAll(livroDAO.buscarPorTitulo(busca));
            } else {
                livrosList.addAll(livroDAO.buscarPorAutor(busca));
            }
            tableLivros.setItems(livrosList);

            if (livrosList.isEmpty()) {
                mostrarInfo("Nenhum livro encontrado");
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar livros", e.getMessage());
        }
    }

    @FXML
    private void listarTodosLivros() {
        try {
            livrosList.clear();
            livrosList.addAll(livroDAO.listarTodos());
            tableLivros.setItems(livrosList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar livros", e.getMessage());
        }
    }

    @FXML
    private void pesquisarAcervo() {
        try {
            itensList.clear();
            if (rbNome.isSelected()) {
                String busca = txtBuscaAcervo.getText().trim();
                if (busca.isEmpty()) {
                    mostrarAlerta("Digite algo para pesquisar");
                    return;
                }
                itensList.addAll(itemDAO.buscarPorTitulo(busca));
            } else {
                TipoItem tipo = cbTipoAcervo.getValue();
                if (tipo == null) {
                    mostrarAlerta("Selecione um tipo");
                    return;
                }
                itensList.addAll(itemDAO.buscarPorTipo(tipo.getIdTipo()));
            }
            tableItens.setItems(itensList);

            if (itensList.isEmpty()) {
                mostrarInfo("Nenhum item encontrado");
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar acervo", e.getMessage());
        }
    }

    @FXML
    private void listarTodosItens() {
        try {
            itensList.clear();
            itensList.addAll(itemDAO.listarTodos());
            tableItens.setItems(itensList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar itens", e.getMessage());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
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
