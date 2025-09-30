package com.supermercado.controller;

import com.supermercado.dao.ProdutoDAO;
import com.supermercado.model.Produto;

import java.util.List;

public class ProdutoController {

    private final ProdutoDAO produtoDAO;

    public ProdutoController(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public boolean cadastrarProduto(String nome, double preco, int quantidade) {
        if (nome == null || nome.trim().isEmpty() || preco < 0 || quantidade < 0) {
            return false;
        }
        Produto produto = new Produto(0, nome, preco, quantidade);
        return produtoDAO.salvar(produto);
    }

    public boolean editarProduto(int id, String nome, double preco, int quantidade) {
        if (id <= 0 || nome == null || nome.trim().isEmpty() || preco < 0 || quantidade < 0) {
            return false;
        }
        Produto produto = new Produto(id, nome, preco, quantidade);
        return produtoDAO.atualizar(produto);
    }

    public boolean removerProduto(int id) {
        if (id <= 0) {
            return false;
        }
        return produtoDAO.remover(id);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.listarTodos();
    }
}