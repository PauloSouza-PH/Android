package com.example.paulosouza.easymoto3.objetos;

import java.io.Serializable;

/**
 * Created by paulo.souza on 08/08/2017.
 * Projeto Moto Facil
 */

public class Contas implements Serializable{
    private String sequencial;
    private String cliente;
    private String data;
    private String hora;
    private Double valor;
    private String descricao;
    private int tipo;

    public Contas(String sequencial, String cliente, Double valor, int tipo, String descricao){
        this.sequencial = sequencial;
        this.cliente = cliente;
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;

        Data_atual data_atual = new Data_atual();
        data = data_atual.getData();
        hora = data_atual.getHora();
    }

    public Contas(){

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getSequencial() {
        return sequencial;
    }

    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
