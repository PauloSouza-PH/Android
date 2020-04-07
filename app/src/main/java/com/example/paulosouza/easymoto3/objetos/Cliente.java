package com.example.paulosouza.easymoto3.objetos;

import java.io.Serializable;

/**
 * Created by paulo.souza on 04/08/2017.
 */

public class Cliente implements Serializable{
    private String id;
    private String nome;
    private String numero;
    private Double valor;
    private boolean favorito;

    public Cliente(){}

    public Cliente(String id,String nome,String numero,Double valor,boolean favorito){
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.valor = valor;
        this.favorito = favorito;
    }

    public String getId(){ return id; }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public Double getValor() {
        return valor;
    }

    public boolean getFavorito(){ return favorito; }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
