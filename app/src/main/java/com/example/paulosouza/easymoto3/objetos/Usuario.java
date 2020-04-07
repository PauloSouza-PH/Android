package com.example.paulosouza.easymoto3.objetos;

import java.io.Serializable;

/**
 * Created by Paulo on 26/08/2017.
 * New File
 */

public class Usuario implements Serializable{
    private String nome;
    private String foto;
    private String email;
    private String Uid;


    public Usuario(String nome, String email, String uid,String foto){
        this.nome = nome;
        this.email = email;
        this.Uid = uid;
        this.foto = foto;
    }

    public String getNome() {return nome;}
    public String getFoto() {
        return foto;
    }
    public String getEmail() {
        return email;
    }
    public String getUid() {
        return Uid;
    }
}
