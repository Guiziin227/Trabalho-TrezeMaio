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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.treze.dao.*;
import org.example.treze.model.*;
import org.controlsfx.control.CheckComboBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BibliotecaController {

    @FXML
    private TableView<Livro> tableLivros;
    @FXML
    private TableColumn<Livro, Integer> colId;
    @FXML
    private TableColumn<Livro, String> colTitulo;
    @FXML
    private TableColumn<Livro, String> colAutores;
    @FXML
    private TableColumn<Livro, String> colEditora;
    @FXML
    private TableColumn<Livro, Integer> colAno;
    @FXML
    private TableColumn<Livro, String> colIsbn;
    @FXML
    private TableColumn<Livro, Integer> colDisponivel;
    @FXML
    private TableColumn<Livro, Integer> colTotal;
    @FXML
    private RadioButton rbTitulo;
    @FXML
    private RadioButton rbAutor;
    @FXML
    private TextField txtBusca;

    private LivroDAO livroDAO = new LivroDAO();
    private AutorDAO autorDAO = new AutorDAO();
    private EditoraDAO editoraDAO = new EditoraDAO();
    private CategoriaLivroDAO categoriaDAO = new CategoriaLivroDAO();
    private AssuntoDAO assuntoDAO = new AssuntoDAO();
    private DoadorDAO doadorDAO = new DoadorDAO();

    private ObservableList<Livro> livrosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idLivro"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutores.setCellValueFactory(new PropertyValueFactory<>("autores"));
        colEditora.setCellValueFactory(new PropertyValueFactory<>("nomeEditora"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisponivel.setCellValueFactory(new PropertyValueFactory<>("quantidadeDisponivel"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("quantidadeExemplares"));

        carregarLivros();
    }

    private void carregarLivros() {
        try {
            livrosList.clear();
            livrosList.addAll(livroDAO.listarTodos());
            tableLivros.setItems(livrosList);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar livros", e.getMessage());
        }
    }

    @FXML
    private void novoLivro() {
        mostrarFormularioLivro(null);
    }

    @FXML
    private void editarLivro() {
        Livro selecionado = tableLivros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um livro para editar");
            return;
        }
        mostrarFormularioLivro(selecionado);
    }

    @FXML
    private void excluirLivro() {
        Livro selecionado = tableLivros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um livro para excluir");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir o livro?");
        confirmacao.setContentText(selecionado.getTitulo());

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                livroDAO.deletar(selecionado.getIdLivro());
                carregarLivros();
                mostrarSucesso("Livro excluído com sucesso!");
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir livro", e.getMessage());
            }
        }
    }

    @FXML
    private void pesquisar() {
        String busca = txtBusca.getText().trim();
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
    private void listarTodos() {
        carregarLivros();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarFormularioLivro(Livro livro) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(livro == null ? "Novo Livro" : "Editar Livro");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabBasico = new Tab("Informações Básicas");
        GridPane gridBasico = new GridPane();
        gridBasico.setHgap(10);
        gridBasico.setVgap(10);
        gridBasico.setPadding(new Insets(15));

        TextField txtTitulo = new TextField(livro != null ? livro.getTitulo() : "");
        txtTitulo.setPrefWidth(300);
        TextField txtIsbn = new TextField(livro != null ? livro.getIsbn() : "");
        TextField txtAno = new TextField(livro != null ? String.valueOf(livro.getAnoPublicacao()) : "");
        TextField txtEdicao = new TextField(livro != null ? livro.getEdicao() : "");
        TextField txtPaginas = new TextField(livro != null ? String.valueOf(livro.getNumeroPaginas()) : "");
        TextField txtIdioma = new TextField(livro != null ? livro.getIdioma() : "");

        int row = 0;
        gridBasico.add(new Label("*Título:"), 0, row);
        gridBasico.add(txtTitulo, 1, row++);
        gridBasico.add(new Label("ISBN:"), 0, row);
        gridBasico.add(txtIsbn, 1, row++);
        gridBasico.add(new Label("*Ano Publicação:"), 0, row);
        gridBasico.add(txtAno, 1, row++);
        gridBasico.add(new Label("*Edição:"), 0, row);
        gridBasico.add(txtEdicao, 1, row++);
        gridBasico.add(new Label("*Número de Páginas:"), 0, row);
        gridBasico.add(txtPaginas, 1, row++);
        gridBasico.add(new Label("Idioma:"), 0, row);
        gridBasico.add(txtIdioma, 1, row++);

        tabBasico.setContent(gridBasico);

        Tab tabEditorial = new Tab("Editora");
        GridPane gridEditorial = new GridPane();
        gridEditorial.setHgap(10);
        gridEditorial.setVgap(10);
        gridEditorial.setPadding(new Insets(15));

        ComboBox<Editora> cbEditora = new ComboBox<>();
        cbEditora.setPrefWidth(300);
        Button btnAddEditora = new Button("+ Nova Editora");
        btnAddEditora.setOnAction(e -> adicionarEditora(cbEditora));

        gridEditorial.add(new Label("*Editora:"), 0, 0);
        gridEditorial.add(cbEditora, 1, 0);
        gridEditorial.add(btnAddEditora, 1, 1);

        tabEditorial.setContent(gridEditorial);

        Tab tabInventario = new Tab("Localização");
        GridPane gridInventario = new GridPane();
        gridInventario.setHgap(10);
        gridInventario.setVgap(10);
        gridInventario.setPadding(new Insets(15));

        TextField txtQtdTotal = new TextField(livro != null ? String.valueOf(livro.getQuantidadeExemplares()) : "1");
        TextField txtQtdDisp = new TextField(livro != null ? String.valueOf(livro.getQuantidadeDisponivel()) : "1");
        TextField txtLocalizacao = new TextField(livro != null ? livro.getLocalizacao() : "");

        ComboBox<Doador> cbDoador = new ComboBox<>();
        cbDoador.setPrefWidth(300);
        Button btnAddDoador = new Button("+ Novo Doador");
        btnAddDoador.setOnAction(e -> adicionarDoador(cbDoador));

        TextArea txtObservacoes = new TextArea(livro != null ? livro.getObservacoes() : "");
        txtObservacoes.setPrefRowCount(4);
        txtObservacoes.setPrefWidth(300);

        row = 0;
        gridInventario.add(new Label("Quantidade Total:"), 0, row);
        gridInventario.add(txtQtdTotal, 1, row++);
        gridInventario.add(new Label("Quantidade Disponível:"), 0, row);
        gridInventario.add(txtQtdDisp, 1, row++);
        gridInventario.add(new Label("Localização:"), 0, row);
        gridInventario.add(txtLocalizacao, 1, row++);
        gridInventario.add(new Label("Doador:"), 0, row);
        gridInventario.add(cbDoador, 1, row++);
        gridInventario.add(btnAddDoador, 1, row++);
        gridInventario.add(new Label("Observações:"), 0, row);
        gridInventario.add(txtObservacoes, 1, row++);

        tabInventario.setContent(gridInventario);

        Tab tabClassificacoes = new Tab("Autores & Categorias");
        VBox vboxClass = new VBox(15);
        vboxClass.setPadding(new Insets(15));

        Label lblAutores = new Label("*AUTORES");
        lblAutores.setStyle("-fx-font-weight: bold;");
        ComboBox<Autor> cbAutor = new ComboBox<>();
        cbAutor.setPrefWidth(250);
        Button btnAddAutor = new Button("+");
        btnAddAutor.setOnAction(e -> adicionarAutor(cbAutor));
        Button btnAddAutorToList = new Button("Adicionar à Lista");
        ListView<Autor> listAutores = new ListView<>();
        listAutores.setPrefHeight(100);
        Button btnRemoveAutor = new Button("Remover Selecionado");
        btnRemoveAutor.setOnAction(e -> {
            Autor selected = listAutores.getSelectionModel().getSelectedItem();
            if (selected != null)
                listAutores.getItems().remove(selected);
        });

        btnAddAutorToList.setOnAction(e -> {
            if (cbAutor.getValue() != null && !listAutores.getItems().contains(cbAutor.getValue())) {
                listAutores.getItems().add(cbAutor.getValue());
                cbAutor.setValue(null);
            }
        });

        VBox autoresBox = new VBox(5, lblAutores, new HBox(5, cbAutor, btnAddAutor, btnAddAutorToList),
                listAutores, btnRemoveAutor);

        Label lblCategorias = new Label("*CATEGORIAS");
        lblCategorias.setStyle("-fx-font-weight: bold;");
        ComboBox<CategoriaLivro> cbCategoria = new ComboBox<>();
        cbCategoria.setPrefWidth(250);
        Button btnAddCategoria = new Button("+");
        btnAddCategoria.setOnAction(e -> adicionarCategoria(cbCategoria));
        Button btnAddCategoriaToList = new Button("Adicionar à Lista");
        ListView<CategoriaLivro> listCategorias = new ListView<>();
        listCategorias.setPrefHeight(80);
        Button btnRemoveCategoria = new Button("Remover Selecionado");
        btnRemoveCategoria.setOnAction(e -> {
            CategoriaLivro selected = listCategorias.getSelectionModel().getSelectedItem();
            if (selected != null)
                listCategorias.getItems().remove(selected);
        });

        btnAddCategoriaToList.setOnAction(e -> {
            if (cbCategoria.getValue() != null && !listCategorias.getItems().contains(cbCategoria.getValue())) {
                listCategorias.getItems().add(cbCategoria.getValue());
                cbCategoria.setValue(null);
            }
        });

        VBox categoriasBox = new VBox(5, lblCategorias,
                new HBox(5, cbCategoria, btnAddCategoria, btnAddCategoriaToList),
                listCategorias, btnRemoveCategoria);

        Label lblAssuntos = new Label("ASSUNTOS (Opcional)");
        lblAssuntos.setStyle("-fx-font-weight: bold;");
        ComboBox<Assunto> cbAssunto = new ComboBox<>();
        cbAssunto.setPrefWidth(250);
        Button btnAddAssunto = new Button("+");
        btnAddAssunto.setOnAction(e -> adicionarAssunto(cbAssunto));
        Button btnAddAssuntoToList = new Button("Adicionar à Lista");
        ListView<Assunto> listAssuntos = new ListView<>();
        listAssuntos.setPrefHeight(80);
        Button btnRemoveAssunto = new Button("Remover Selecionado");
        btnRemoveAssunto.setOnAction(e -> {
            Assunto selected = listAssuntos.getSelectionModel().getSelectedItem();
            if (selected != null)
                listAssuntos.getItems().remove(selected);
        });

        btnAddAssuntoToList.setOnAction(e -> {
            if (cbAssunto.getValue() != null && !listAssuntos.getItems().contains(cbAssunto.getValue())) {
                listAssuntos.getItems().add(cbAssunto.getValue());
                cbAssunto.setValue(null);
            }
        });

        VBox assuntosBox = new VBox(5, lblAssuntos, new HBox(5, cbAssunto, btnAddAssunto, btnAddAssuntoToList),
                listAssuntos, btnRemoveAssunto);

        vboxClass.getChildren().addAll(autoresBox, new Separator(), categoriasBox, new Separator(), assuntosBox);

        ScrollPane scrollClass = new ScrollPane(vboxClass);
        scrollClass.setFitToWidth(true);
        tabClassificacoes.setContent(scrollClass);

        tabPane.getTabs().addAll(tabBasico, tabEditorial, tabInventario, tabClassificacoes);

        try {
            cbEditora.setItems(FXCollections.observableArrayList(editoraDAO.listarTodos()));
            cbAutor.setItems(FXCollections.observableArrayList(autorDAO.listarTodos()));
            cbCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listarTodos()));
            cbAssunto.setItems(FXCollections.observableArrayList(assuntoDAO.listarTodos()));
            cbDoador.setItems(FXCollections.observableArrayList(doadorDAO.listarTodos()));

            if (livro != null) {
                if (livro.getIdEditora() > 0) {
                    cbEditora.setValue(editoraDAO.buscarPorId(livro.getIdEditora()));
                }

                if (livro.getIdDoador() != null && livro.getIdDoador() > 0) {
                    cbDoador.setValue(doadorDAO.buscarPorId(livro.getIdDoador()));
                }

                List<Integer> autoresIds = livroDAO.buscarAutoresDoLivro(livro.getIdLivro());
                for (Integer idAutor : autoresIds) {
                    Autor autor = autorDAO.buscarPorId(idAutor);
                    if (autor != null) {
                        listAutores.getItems().add(autor);
                    }
                }

                List<Integer> categoriasIds = livroDAO.buscarCategoriasDoLivro(livro.getIdLivro());
                for (Integer idCategoria : categoriasIds) {
                    CategoriaLivro categoria = categoriaDAO.buscarPorId(idCategoria);
                    if (categoria != null) {
                        listCategorias.getItems().add(categoria);
                    }
                }

                List<Integer> assuntosIds = livroDAO.buscarAssuntosDoLivro(livro.getIdLivro());
                for (Integer idAssunto : assuntosIds) {
                    Assunto assunto = assuntoDAO.buscarPorId(idAssunto);
                    if (assunto != null) {
                        listAssuntos.getItems().add(assunto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {

            if (txtTitulo.getText().trim().isEmpty()) {
                tabPane.getSelectionModel().select(tabBasico);
                mostrarAlerta("O título é obrigatório!");
                return;
            }
            if (txtAno.getText().trim().isEmpty()) {
                tabPane.getSelectionModel().select(tabBasico);
                mostrarAlerta("O ano é obrigatório!");
                return;
            }
            if (txtPaginas.getText().trim().isEmpty()) {
                tabPane.getSelectionModel().select(tabBasico);
                mostrarAlerta("O número de páginas é obrigatório!");
                return;
            }
            if (txtEdicao.getText().trim().isEmpty()) {
                tabPane.getSelectionModel().select(tabBasico);
                mostrarAlerta("A edição é obrigatória!");
                return;
            }
            if (cbEditora.getValue() == null) {
                tabPane.getSelectionModel().select(tabEditorial);
                mostrarAlerta("Selecione uma editora!");
                return;
            }
            if (listAutores.getItems().isEmpty()) {
                tabPane.getSelectionModel().select(tabClassificacoes);
                mostrarAlerta("Adicione pelo menos um autor!");
                return;
            }
            if (listCategorias.getItems().isEmpty()) {
                tabPane.getSelectionModel().select(tabClassificacoes);
                mostrarAlerta("Adicione pelo menos uma categoria!");
                return;
            }

            try {
                Livro livroSalvar = livro != null ? livro : new Livro();
                livroSalvar.setTitulo(txtTitulo.getText().trim());
                livroSalvar.setIsbn(txtIsbn.getText().trim());
                livroSalvar.setIdEditora(cbEditora.getValue().getIdEditora());
                livroSalvar.setAnoPublicacao(Integer.parseInt(txtAno.getText().trim()));
                livroSalvar.setEdicao(txtEdicao.getText().trim());
                livroSalvar.setNumeroPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                livroSalvar.setIdioma(txtIdioma.getText().trim());
                livroSalvar.setQuantidadeExemplares(Integer.parseInt(txtQtdTotal.getText()));
                livroSalvar.setQuantidadeDisponivel(Integer.parseInt(txtQtdDisp.getText()));
                livroSalvar.setLocalizacao(txtLocalizacao.getText().trim());
                livroSalvar.setObservacoes(txtObservacoes.getText().trim());
                livroSalvar.setIdDoador(cbDoador.getValue() != null ? cbDoador.getValue().getIdDoador() : null);

                if (livro == null) {
                    livroDAO.inserir(livroSalvar);
                } else {
                    livroDAO.atualizar(livroSalvar);
                    livroDAO.removerTodosAutores(livroSalvar.getIdLivro());
                    livroDAO.removerTodasCategorias(livroSalvar.getIdLivro());
                    livroDAO.removerTodosAssuntos(livroSalvar.getIdLivro());
                }

                for (Autor autor : listAutores.getItems()) {
                    try {
                        livroDAO.associarAutor(livroSalvar.getIdLivro(), autor.getIdAutor());
                    } catch (SQLException ex) {
                    }
                }

                for (CategoriaLivro cat : listCategorias.getItems()) {
                    try {
                        livroDAO.associarCategoria(livroSalvar.getIdLivro(), cat.getIdCategoria());
                    } catch (SQLException ex) {
                    }
                }

                for (Assunto ass : listAssuntos.getItems()) {
                    try {
                        livroDAO.associarAssunto(livroSalvar.getIdLivro(), ass.getIdAssunto());
                    } catch (SQLException ex) {
                    }
                }

                carregarLivros();
                dialog.close();
                mostrarSucesso("Livro salvo com sucesso!");
            } catch (SQLException | NumberFormatException ex) {
                mostrarErro("Erro ao salvar livro", ex.getMessage());
            }
        });

        VBox mainBox = new VBox(10);
        mainBox.setPadding(new Insets(10));

        Label lblObrigatorio = new Label("* Campos obrigatórios");
        lblObrigatorio.setStyle("-fx-font-style: italic; -fx-text-fill: #666666;");

        HBox botoes = new HBox(10, btnSalvar, btnCancelar);
        botoes.setPadding(new Insets(10, 0, 0, 0));

        mainBox.getChildren().addAll(lblObrigatorio, tabPane, botoes);

        Scene scene = new Scene(mainBox, 600, 500);
        scene.getStylesheets().add(getClass().getResource("/org/example/treze/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void adicionarAutor(ComboBox<Autor> cb) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Autor");
        dialog.setHeaderText("Adicionar novo autor");
        dialog.setContentText("Nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nome -> {
            if (!nome.trim().isEmpty()) {
                TextInputDialog dialog2 = new TextInputDialog();
                dialog2.setTitle("Nacionalidade");
                dialog2.setHeaderText("Nacionalidade do autor");
                dialog2.setContentText("Nacionalidade:");

                Optional<String> result2 = dialog2.showAndWait();
                String nacionalidade = result2.orElse("");

                try {
                    Autor autor = new Autor();
                    autor.setNome(nome.trim());
                    autor.setNacionalidade(nacionalidade.trim());
                    autorDAO.inserir(autor);
                    cb.setItems(FXCollections.observableArrayList(autorDAO.listarTodos()));
                    cb.setValue(autor);
                    mostrarSucesso("Autor adicionado!");
                } catch (SQLException e) {
                    mostrarErro("Erro", e.getMessage());
                }
            }
        });
    }

    private void adicionarEditora(ComboBox<Editora> cb) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nova Editora");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        TextField txtNome = new TextField();
        TextField txtCnpj = new TextField();
        TextField txtCidade = new TextField();
        TextField txtPais = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("CNPJ:"), 0, 1);
        grid.add(txtCnpj, 1, 1);
        grid.add(new Label("Cidade:"), 0, 2);
        grid.add(txtCidade, 1, 2);
        grid.add(new Label("País:"), 0, 3);
        grid.add(txtPais, 1, 3);

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().trim().isEmpty() || txtCnpj.getText().trim().isEmpty()) {
                mostrarAlerta("Nome e CNPJ são obrigatórios!");
                return;
            }
            try {
                Editora editora = new Editora();
                editora.setNome(txtNome.getText().trim());
                editora.setCnpj(txtCnpj.getText().trim());
                editora.setCidade(txtCidade.getText().trim());
                editora.setPais(txtPais.getText().trim());
                editoraDAO.inserir(editora);
                cb.setItems(FXCollections.observableArrayList(editoraDAO.listarTodos()));
                cb.setValue(editora);
                dialog.close();
                mostrarSucesso("Editora adicionada!");
            } catch (SQLException ex) {
                mostrarErro("Erro", ex.getMessage());
            }
        });

        grid.add(new HBox(10, btnSalvar, btnCancelar), 1, 4);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/org/example/treze/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void adicionarCategoria(ComboBox<CategoriaLivro> cb) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nova Categoria");
        dialog.setHeaderText("Adicionar nova categoria");
        dialog.setContentText("Nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nome -> {
            if (!nome.trim().isEmpty()) {
                try {
                    CategoriaLivro cat = new CategoriaLivro();
                    cat.setNome(nome.trim());
                    categoriaDAO.inserir(cat);
                    cb.setItems(FXCollections.observableArrayList(categoriaDAO.listarTodos()));
                    cb.setValue(cat);
                    mostrarSucesso("Categoria adicionada!");
                } catch (SQLException e) {
                    mostrarErro("Erro", e.getMessage());
                }
            }
        });
    }

    private void adicionarAssunto(ComboBox<Assunto> cb) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Assunto");
        dialog.setHeaderText("Adicionar novo assunto");
        dialog.setContentText("Descrição:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(desc -> {
            if (!desc.trim().isEmpty()) {
                try {
                    Assunto ass = new Assunto();
                    ass.setDescricao(desc.trim());
                    assuntoDAO.inserir(ass);
                    cb.setItems(FXCollections.observableArrayList(assuntoDAO.listarTodos()));
                    cb.setValue(ass);
                    mostrarSucesso("Assunto adicionado!");
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
