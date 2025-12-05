package org.example.treze.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InicialController {
    
    public void abrirAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/treze/admin-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            Stage stage = new Stage();
            stage.setTitle("Sistema - Administrador");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void abrirCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/treze/cliente-view.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("Sistema - Cliente");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
