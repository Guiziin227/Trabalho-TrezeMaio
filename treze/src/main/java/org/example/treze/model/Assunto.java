package org.example.treze.model;

public class Assunto {
    private int idAssunto;
    private String descricao;

    public Assunto() {
    }

    public Assunto(int idAssunto, String descricao) {
        this.idAssunto = idAssunto;
        this.descricao = descricao;
    }

    public int getIdAssunto() {
        return idAssunto;
    }

    public void setIdAssunto(int idAssunto) {
        this.idAssunto = idAssunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
