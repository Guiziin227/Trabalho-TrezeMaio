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
import org.example.treze.dao.DoadorDAO;
import org.example.treze.dao.ItemAcervoDAO;
import org.example.treze.dao.TipoItemDAO;
import org.example.treze.model.Doador;
import org.example.treze.model.ItemAcervo;
import org.example.treze.model.TipoItem;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AcervoController {

    @FXML
    private TableView<ItemAcervo> tableItens;
    @FXML
    private TableColumn<ItemAcervo, Integer> colId;
    @FXML
    private TableColumn<ItemAcervo, String> colTitulo;
    @FXML
    private TableColumn<ItemAcervo, String> colTipo;
    @FXML
    private TableColumn<ItemAcervo, LocalDate> colDataItem;
    @FXML
    private TableColumn<ItemAcervo, String> colEstado;
    @FXML
    private TableColumn<ItemAcervo, Boolean> colDigitalizado;
    @FXML
    private TableColumn<ItemAcervo, String> colLocalizacao;
    @FXML
    private RadioButton rbTitulo;
    @FXML
    private RadioButton rbTipo;
    @FXML
    private TextField txtBusca;
    @FXML
    private ComboBox<TipoItem> cbTipoBusca;

    private ItemAcervoDAO itemDAO = new ItemAcervoDAO();
    private TipoItemDAO tipoItemDAO = new TipoItemDAO();
    private DoadorDAO doadorDAO = new DoadorDAO();

    private ObservableList<ItemAcervo> itensList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoItemDescricao"));
        colDataItem.setCellValueFactory(new PropertyValueFactory<>("dataItem"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDigitalizado.setCellValueFactory(new PropertyValueFactory<>("digitalizado"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacaoFisica"));

        rbTipo.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtBusca.setVisible(false);
                cbTipoBusca.setVisible(true);
                try {
                    cbTipoBusca.setItems(FXCollections.observableArrayList(tipoItemDAO.listarTodos()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                txtBusca.setVisible(true);
                cbTipoBusca.setVisible(false);
            }
        });

        carregarItens();
    }

    private void carregarItens() {
        try {
            itensList.clear();
            itensList.addAll(itemDAO.listarTodos());
            tableItens.setItems(itensList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar itens", e.getMessage());
        }
    }

    @FXML
    private void novoItem() {
        mostrarFormularioItem(null);
    }

    @FXML
    private void editarItem() {
        ItemAcervo selecionado = tableItens.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um item para editar");
            return;
        }
        mostrarFormularioItem(selecionado);
    }

    @FXML
    private void excluirItem() {
        ItemAcervo selecionado = tableItens.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um item para excluir");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o item?");
        confirmacao.setContentText(selecionado.getTitulo());

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                itemDAO.deletar(selecionado.getIdItem());
                carregarItens();
                mostrarSucesso("Item excluído com sucesso!");
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir item", e.getMessage());
            }
        }
    }

    @FXML
    private void pesquisar() {
        try {
            itensList.clear();
            if (rbTitulo.isSelected()) {
                String busca = txtBusca.getText().trim();
                if (busca.isEmpty()) {
                    mostrarAlerta("Digite algo para pesquisar");
                    return;
                }
                itensList.addAll(itemDAO.buscarPorTitulo(busca));
            } else {
                TipoItem tipo = cbTipoBusca.getValue();
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
            mostrarErro("Erro ao pesquisar itens", e.getMessage());
        }
    }

    @FXML
    private void listarTodos() {
        carregarItens();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarFormularioItem(ItemAcervo item) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(item == null ? "Novo Item" : "Editar Item");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<TipoItem> cbTipo = new ComboBox<>();
        Button btnAddTipo = new Button("+");
        btnAddTipo.setOnAction(e -> adicionarTipo(cbTipo));

        TextField txtTitulo = new TextField(item != null ? item.getTitulo() : "");
        TextArea txtDescricao = new TextArea(item != null ? item.getDescricao() : "");
        txtDescricao.setPrefRowCount(3);
        DatePicker dpDataItem = new DatePicker(item != null ? item.getDataItem() : null);
        DatePicker dpDataAquisicao = new DatePicker(item != null ? item.getDataAquisicao() : LocalDate.now());
        TextField txtEstado = new TextField(item != null ? item.getEstado() : "");
        CheckBox chkDigitalizado = new CheckBox();
        if (item != null)
            chkDigitalizado.setSelected(item.isDigitalizado());
        TextField txtLocalizacao = new TextField(item != null ? item.getLocalizacaoFisica() : "");

        ComboBox<Doador> cbDoador = new ComboBox<>();
        Button btnAddDoador = new Button("+");
        btnAddDoador.setOnAction(e -> adicionarDoador(cbDoador));

        try {
            cbTipo.setItems(FXCollections.observableArrayList(tipoItemDAO.listarTodos()));
            cbDoador.setItems(FXCollections.observableArrayList(doadorDAO.listarTodos()));
            if (item != null && item.getTipoItem() > 0) {
                cbTipo.setValue(tipoItemDAO.buscarPorId(item.getTipoItem()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grid.add(new Label("Tipo:"), 0, 0);
        HBox tipoBox = new HBox(5, cbTipo, btnAddTipo);
        grid.add(tipoBox, 1, 0);
        grid.add(new Label("Título:"), 0, 1);
        grid.add(txtTitulo, 1, 1);
        grid.add(new Label("Descrição:"), 0, 2);
        grid.add(txtDescricao, 1, 2);
        grid.add(new Label("Data do Item:"), 0, 3);
        grid.add(dpDataItem, 1, 3);
        grid.add(new Label("Data Aquisição:"), 0, 4);
        grid.add(dpDataAquisicao, 1, 4);
        grid.add(new Label("Estado:"), 0, 5);
        grid.add(txtEstado, 1, 5);
        grid.add(new Label("Digitalizado:"), 0, 6);
        grid.add(chkDigitalizado, 1, 6);
        grid.add(new Label("Localização:"), 0, 7);
        grid.add(txtLocalizacao, 1, 7);
        grid.add(new Label("Doador:"), 0, 8);
        HBox doadorBox = new HBox(5, cbDoador, btnAddDoador);
        grid.add(doadorBox, 1, 8);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {
            try {
                ItemAcervo itemSalvar = item != null ? item : new ItemAcervo();
                itemSalvar.setTipoItem(cbTipo.getValue() != null ? cbTipo.getValue().getIdTipo() : 0);
                itemSalvar.setTitulo(txtTitulo.getText());
                itemSalvar.setDescricao(txtDescricao.getText());
                itemSalvar.setDataItem(dpDataItem.getValue());
                itemSalvar.setDataAquisicao(dpDataAquisicao.getValue());
                itemSalvar.setEstado(txtEstado.getText());
                itemSalvar.setDigitalizado(chkDigitalizado.isSelected());
                itemSalvar.setLocalizacaoFisica(txtLocalizacao.getText());
                itemSalvar.setIdDoador(cbDoador.getValue() != null ? cbDoador.getValue().getIdDoador() : null);

                if (item == null) {
                    itemDAO.inserir(itemSalvar);
                } else {
                    itemDAO.atualizar(itemSalvar);
                }

                carregarItens();
                dialog.close();
                mostrarSucesso("Item salvo com sucesso!");
            } catch (SQLException ex) {
                mostrarErro("Erro ao salvar item", ex.getMessage());
            }
        });

        HBox botoes = new HBox(10, btnSalvar, btnCancelar);
        grid.add(botoes, 1, 9);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/org/example/treze/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void adicionarTipo(ComboBox<TipoItem> cb) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Tipo");
        dialog.setHeaderText("Adicionar novo tipo de item");
        dialog.setContentText("Descrição:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(desc -> {
            if (!desc.trim().isEmpty()) {
                try {
                    TipoItem tipo = new TipoItem();
                    tipo.setDescricao(desc.trim());
                    tipoItemDAO.inserir(tipo);
                    cb.setItems(FXCollections.observableArrayList(tipoItemDAO.listarTodos()));
                    cb.setValue(tipo);
                    mostrarSucesso("Tipo adicionado!");
                } catch (SQLException e) {
                    mostrarErro("Erro", e.getMessage());
                }
            }
        });
    }

    private void adicionarDoador(ComboBox<Doador> cb) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Novo Doador");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        TextField txtNome = new TextField();
        TextField txtTelefone = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Telefone:"), 0, 1);
        grid.add(txtTelefone, 1, 1);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty()) {
                mostrarAlerta("Nome é obrigatório!");
                return;
            }
            try {
                Doador doador = new Doador();
                doador.setNome(txtNome.getText().trim());
                doador.setTelefone(txtTelefone.getText().trim());
                doadorDAO.inserir(doador);
                cb.setItems(FXCollections.observableArrayList(doadorDAO.listarTodos()));
                cb.setValue(doador);
                dialog.close();
                mostrarSucesso("Doador adicionado!");
            } catch (SQLException ex) {
                mostrarErro("Erro", ex.getMessage());
            }
        });

        grid.add(new HBox(10, btnSalvar, btnCancelar), 1, 2);

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
