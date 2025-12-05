package org.example.treze.model;

public class Editora {
    private int idEditora;
    private String nome;
    private String cnpj;
    private String cidade;
    private String pais;

    public Editora() {
    }

    public Editora(int idEditora, String nome, String cnpj, String cidade, String pais) {
        this.idEditora = idEditora;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.pais = pais;
    }

    public int getIdEditora() {
        return idEditora;
    }

    public void setIdEditora(int idEditora) {
        this.idEditora = idEditora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return nome;
    }
}
