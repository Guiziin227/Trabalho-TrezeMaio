package org.example.treze.model;

public class Doador {
    private int idDoador;
    private String nome;
    private String telefone;

    public Doador() {
    }

    public Doador(int idDoador, String nome, String telefone) {
        this.idDoador = idDoador;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(int idDoador) {
        this.idDoador = idDoador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }
}
