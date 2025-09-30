package com.supermercado.controller;

import com.supermercado.dao.UsuarioDAO;
import com.supermercado.model.Usuario;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean cadastrarUsuario(String nome, String cpf, boolean isAdmin) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        if (usuarioDAO.buscarPorCpf(cpf) != null) {
            return false;
        }

        Usuario novoUsuario = new Usuario(nome, cpf, isAdmin);
        return usuarioDAO.salvar(novoUsuario);
    }
}