package org.example.treze.model;

import java.time.LocalDate;

public class ItemAcervo {
    private int idItem;
    private int tipoItem;
    private String tipoItemDescricao;
    private String titulo;
    private String descricao;
    private LocalDate dataItem;
    private LocalDate dataAquisicao;
    private String estado;
    private boolean digitalizado;
    private String localizacaoFisica;
    private Integer idDoador;
    private String nomeDoador;

    public ItemAcervo() {
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(int tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getTipoItemDescricao() {
        return tipoItemDescricao;
    }

    public void setTipoItemDescricao(String tipoItemDescricao) {
        this.tipoItemDescricao = tipoItemDescricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataItem() {
        return dataItem;
    }

    public void setDataItem(LocalDate dataItem) {
        this.dataItem = dataItem;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isDigitalizado() {
        return digitalizado;
    }

    public void setDigitalizado(boolean digitalizado) {
        this.digitalizado = digitalizado;
    }

    public String getLocalizacaoFisica() {
        return localizacaoFisica;
    }

    public void setLocalizacaoFisica(String localizacaoFisica) {
        this.localizacaoFisica = localizacaoFisica;
    }

    public Integer getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(Integer idDoador) {
        this.idDoador = idDoador;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
