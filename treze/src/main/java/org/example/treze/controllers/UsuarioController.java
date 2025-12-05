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
import org.example.treze.dao.UsuarioDAO;
import org.example.treze.model.Usuario;

import java.sql.SQLException;
import java.util.Optional;

public class UsuarioController {
    
    @FXML private TableView<Usuario> tableUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colCpf;
    @FXML private TableColumn<Usuario, String> colEmail;
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ObservableList<Usuario> usuariosList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        carregarUsuarios();
    }
    
    private void carregarUsuarios() {
        try {
            usuariosList.clear();
            usuariosList.addAll(usuarioDAO.listarTodos());
            tableUsuarios.setItems(usuariosList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar usuários", e.getMessage());
        }
    }
    
    @FXML
    private void novoUsuario() {
        mostrarFormularioUsuario(null);
    }
    
    @FXML
    private void editarUsuario() {
        Usuario selecionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um usuário para editar");
            return;
        }
        mostrarFormularioUsuario(selecionado);
    }
    
    @FXML
    private void excluirUsuario() {
        Usuario selecionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um usuário para excluir");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o usuário?");
        confirmacao.setContentText(selecionado.getNome());
        
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                usuarioDAO.deletar(selecionado.getIdUsuario());
                carregarUsuarios();
                mostrarSucesso("Usuário excluído com sucesso!");
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir usuário", e.getMessage());
            }
        }
    }
    
    private void mostrarFormularioUsuario(Usuario usuario) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(usuario == null ? "Novo Usuário" : "Editar Usuário");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtNome = new TextField(usuario != null ? usuario.getNome() : "");
        TextField txtCpf = new TextField(usuario != null ? usuario.getCpf() : "");
        TextField txtEmail = new TextField(usuario != null ? usuario.getEmail() : "");
        
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);
        grid.add(txtCpf, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        
        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());
        
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                mostrarAlerta("Preencha todos os campos!");
                return;
            }
            
            try {
                Usuario usuarioSalvar = usuario != null ? usuario : new Usuario();
                usuarioSalvar.setNome(txtNome.getText());
                usuarioSalvar.setCpf(txtCpf.getText());
                usuarioSalvar.setEmail(txtEmail.getText());
                
                if (usuario == null) {
                    usuarioDAO.inserir(usuarioSalvar);
                } else {
                    usuarioDAO.atualizar(usuarioSalvar);
                }
                
                carregarUsuarios();
                dialog.close();
                mostrarSucesso("Usuário salvo com sucesso!");
            } catch (SQLException ex) {
                mostrarErro("Erro ao salvar usuário", ex.getMessage());
            }
        });
        
        HBox botoes = new HBox(10, btnSalvar, btnCancelar);
        grid.add(botoes, 1, 3);
        
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
