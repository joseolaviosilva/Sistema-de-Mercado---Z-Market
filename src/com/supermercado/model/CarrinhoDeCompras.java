package com.supermercado.model;

import java.util.HashMap;
import java.util.Map;

public class CarrinhoDeCompras {

    private final Map<Produto, Integer> itens;

    public CarrinhoDeCompras() {
        this.itens = new HashMap<>();
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            return;
        }
        this.itens.put(produto, this.itens.getOrDefault(produto, 0) + quantidade);
    }

    public void removerItem(Produto produto) {
        if (produto != null) {
            this.itens.remove(produto);
        }
    }

    public Map<Produto, Integer> getItens() {
        return new HashMap<>(this.itens);
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            total += entry.getKey().getPreco() * entry.getValue();
        }
        return total;
    }
    
    public void limparCarrinho() {
        this.itens.clear();
    }
}