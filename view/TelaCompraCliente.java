package com.supermercado.view;

import com.supermercado.controller.CompraController;
import com.supermercado.controller.LoginController;
import com.supermercado.controller.ProdutoController;
import com.supermercado.dao.ProdutoDAO;
import com.supermercado.model.Produto;
import com.supermercado.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class TelaCompraCliente extends JFrame {

    private final ProdutoController produtoController;
    private final CompraController compraController;
    private JTable tabelaProdutos, tabelaCarrinho;
    private DefaultTableModel modelProdutos, modelCarrinho;
    private JLabel totalLabel;

    public TelaCompraCliente() {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        this.produtoController = new ProdutoController(produtoDAO);
        this.compraController = new CompraController(produtoDAO);
        
        setTitle("Zé Market - Compras - Cliente: " + LoginController.getUsuarioLogado().getNome());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        atualizarTabelaProdutos();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Bem-vindo ao Zé Market!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);

        JPanel painelProdutos = criarPainelProdutos();
        JPanel painelCarrinho = criarPainelCarrinho();

        splitPane.setLeftComponent(painelProdutos);
        splitPane.setRightComponent(painelCarrinho);

        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelProdutos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));
        
        modelProdutos = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(modelProdutos);
        painel.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        
        JButton adicionarButton = new JButton("Adicionar ao Carrinho");
        adicionarButton.addActionListener(e -> adicionarAoCarrinho());
        painel.add(adicionarButton, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelCarrinho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Meu Carrinho"));
        
        modelCarrinho = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCarrinho = new JTable(modelCarrinho);
        painel.add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);
        
        JPanel painelSul = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: R$ 0.00");
        totalLabel.setFont(new Font("Serif", Font.BOLD, 18));
        painelSul.add(totalLabel, BorderLayout.WEST);
        
        JButton removerButton = new JButton("Remover do Carrinho");
        removerButton.addActionListener(e -> removerDoCarrinho());
        
        JButton finalizarButton = new JButton("Finalizar Compra");
        finalizarButton.addActionListener(e -> finalizarCompra());
        
        JButton logoutButton = new JButton("Deslogar");
        logoutButton.addActionListener(e -> deslogar());
        
        JPanel botoes = new JPanel();
        botoes.add(removerButton);
        botoes.add(finalizarButton);
        botoes.add(logoutButton);
        
        painelSul.add(botoes, BorderLayout.EAST);
        painel.add(painelSul, BorderLayout.SOUTH);
        
        return painel;
    }

    private void atualizarTabelaProdutos() {
        modelProdutos.setRowCount(0);
        for (Produto p : produtoController.listarProdutos()) {
            if (p.getQuantidadeEstoque() > 0) {
                modelProdutos.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidadeEstoque()});
            }
        }
    }

    private void atualizarTabelaCarrinho() {
        modelCarrinho.setRowCount(0);
        Map<Produto, Integer> itens = compraController.getCarrinho().getItens();
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            Produto p = entry.getKey();
            int qtd = entry.getValue();
            modelCarrinho.addRow(new Object[]{p.getNome(), qtd, p.getPreco() * qtd});
        }
        totalLabel.setText(String.format("Total: R$ %.2f", compraController.getCarrinho().calcularTotal()));
    }
    
    private void adicionarAoCarrinho() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para adicionar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modelProdutos.getValueAt(selectedRow, 0);
        String nome = (String) modelProdutos.getValueAt(selectedRow, 1);
        double preco = (double) modelProdutos.getValueAt(selectedRow, 2);
        int estoque = (int) modelProdutos.getValueAt(selectedRow, 3);
        
        Produto produtoSelecionado = new Produto(id, nome, preco, estoque);
        
        String qtdStr = JOptionPane.showInputDialog(this, "Digite a quantidade:", "Adicionar ao Carrinho", JOptionPane.PLAIN_MESSAGE);
        try {
            int quantidade = Integer.parseInt(qtdStr);
            if (quantidade > 0 && quantidade <= estoque) {
                compraController.adicionarAoCarrinho(produtoSelecionado, quantidade);
                atualizarTabelaCarrinho();
            } else {
                JOptionPane.showMessageDialog(this, "Quantidade inválida ou maior que o estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removerDoCarrinho() {
        int selectedRow = tabelaCarrinho.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item do carrinho para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nomeProduto = (String) modelCarrinho.getValueAt(selectedRow, 0);
        
        Produto produtoParaRemover = null;
        for (Produto p : compraController.getCarrinho().getItens().keySet()) {
            if (p.getNome().equals(nomeProduto)) {
                produtoParaRemover = p;
                break;
            }
        }
        
        if (produtoParaRemover != null) {
            compraController.removerDoCarrinho(produtoParaRemover);
            atualizarTabelaCarrinho();
        }
    }

    private void finalizarCompra() {
        Usuario usuarioLogado = LoginController.getUsuarioLogado();
        String notaFiscal = compraController.gerarNotaFiscal(usuarioLogado);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja finalizar a compra?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = compraController.finalizarCompra();
            if (sucesso) {
                JTextArea textArea = new JTextArea(notaFiscal);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(this, "Compra Realizada com Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                
                atualizarTabelaProdutos();
                atualizarTabelaCarrinho();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível finalizar a compra. Verifique o estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deslogar() {
        new LoginController(null).deslogar();
        dispose();
        new TelaLogin().setVisible(true);
    }
}