package com.supermercado.model;

public class Usuario {

    private String nome;
    private String cpf;
    private boolean isAdmin;

    public Usuario(String nome, String cpf, boolean isAdmin) {
        this.nome = nome;
        this.cpf = cpf;
        this.isAdmin = isAdmin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}