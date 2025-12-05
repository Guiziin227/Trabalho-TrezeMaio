package org.example.treze.model;

public class TipoItem {
    private int idTipo;
    private String descricao;

    public TipoItem() {
    }

    public TipoItem(int idTipo, String descricao) {
        this.idTipo = idTipo;
        this.descricao = descricao;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
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
