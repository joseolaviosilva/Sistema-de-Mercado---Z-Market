package com.supermercado.controller;

import com.supermercado.dao.ProdutoDAO;
import com.supermercado.model.CarrinhoDeCompras;
import com.supermercado.model.Produto;
import com.supermercado.model.Usuario;

import java.util.Map;

public class CompraController {

    private final ProdutoDAO produtoDAO;
    private final CarrinhoDeCompras carrinho;

    public CompraController(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
        this.carrinho = new CarrinhoDeCompras();
    }

    public void adicionarAoCarrinho(Produto produto, int quantidade) {
        if (produto != null && quantidade > 0 && produto.getQuantidadeEstoque() >= quantidade) {
            carrinho.adicionarItem(produto, quantidade);
        }
    }
    
    public void removerDoCarrinho(Produto produto) {
        carrinho.removerItem(produto);
    }

    public CarrinhoDeCompras getCarrinho() {
        return carrinho;
    }

    public boolean finalizarCompra() {
        Map<Produto, Integer> itens = carrinho.getItens();
        if (itens.isEmpty()) {
            return false;
        }

        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            Produto produto = entry.getKey();
            int quantidadeComprada = entry.getValue();
            
            if(produto.getQuantidadeEstoque() < quantidadeComprada) {
                return false; 
            }
            
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeComprada);
            produtoDAO.atualizar(produto);
        }
        
        carrinho.limparCarrinho();
        return true;
    }

    public String gerarNotaFiscal(Usuario usuario) {
        StringBuilder nota = new StringBuilder();
        nota.append("---- NOTA FISCAL - Zé Market ----\n");
        nota.append("Cliente: ").append(usuario.getNome()).append("\n");
        nota.append("CPF: ").append(usuario.getCpf()).append("\n\n");
        nota.append("---- PRODUTOS ----\n");

        Map<Produto, Integer> itens = carrinho.getItens();
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            Produto p = entry.getKey();
            int qtd = entry.getValue();
            nota.append(String.format("%s - %d un. x R$ %.2f = R$ %.2f\n",
                    p.getNome(), qtd, p.getPreco(), (p.getPreco() * qtd)));
        }

        nota.append("\n---- TOTAL ----\n");
        nota.append(String.format("TOTAL A PAGAR: R$ %.2f\n", carrinho.calcularTotal()));
        nota.append("-----------------------------------\n");
        
        return nota.toString();
    }
}